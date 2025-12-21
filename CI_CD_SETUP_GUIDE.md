# CI/CD 파이프라인 설정 가이드

## 📋 개요

GitHub Actions를 사용한 자동 배포 파이프라인이 구축되었습니다.

**배포 흐름:**
```
GitHub Push (main)
  → 코드 체크아웃
  → Gradle 빌드
  → 테스트 실행
  → Docker 이미지 빌드
  → Docker Hub 푸시
  → EC2 서버 배포
```

---

## 🔐 1단계: GitHub Secrets 설정

GitHub Repository → Settings → Secrets and variables → Actions → New repository secret

### 필수 Secrets

#### 1. Docker Hub 인증 정보

**`DOCKER_USERNAME`**
- 값: Docker Hub 사용자명 (예: `ohsehun`)
- 확인: https://hub.docker.com/settings/general

**`DOCKER_PASSWORD`**
- 값: Docker Hub Access Token (권장) 또는 비밀번호
- 생성 방법:
  1. https://hub.docker.com/settings/security
  2. "New Access Token" 클릭
  3. 토큰 이름: `github-actions`
  4. 권한: Read, Write, Delete
  5. 생성된 토큰 복사 → GitHub Secrets에 저장

#### 2. EC2 서버 정보

**`EC2_HOST`**
- 값: EC2 Public IP 또는 도메인
- 예: `3.35.123.456` 또는 `api.perfacto.com`
- 확인: AWS Console → EC2 → 인스턴스 → Public IPv4 address

**`EC2_USERNAME`**
- 값: EC2 SSH 사용자명
- Amazon Linux: `ec2-user`
- Ubuntu: `ubuntu`

**`EC2_SSH_KEY`**
- 값: EC2 접속용 SSH Private Key 전체 내용
- 생성/확인 방법:
  ```bash
  # 로컬에서 키 내용 확인
  cat /Users/mac/spring_boot_proj/perfacto_server/perfacto-key.pem

  # 또는 AWS에서 새로 생성
  # AWS Console → EC2 → Key Pairs → Create Key Pair
  ```
- ⚠️ **주의**:
  - `-----BEGIN RSA PRIVATE KEY-----`부터 `-----END RSA PRIVATE KEY-----`까지 **전체** 복사
  - 줄바꿈 포함하여 그대로 복사

### 선택 Secrets (Slack 알림용)

**`SLACK_WEBHOOK`**
- 값: Slack Incoming Webhook URL
- 설정 방법:
  1. https://api.slack.com/messaging/webhooks
  2. Workspace 선택 → 채널 선택
  3. Webhook URL 복사
- 예: `https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXX`

---

## 🖥️ 2단계: EC2 서버 설정

### 1. EC2에 접속

```bash
# 로컬에서 EC2 접속
ssh -i perfacto-key.pem ubuntu@YOUR_EC2_IP

# 또는 AWS Console에서 "Connect" 사용
```

### 2. Docker 설치 (아직 설치 안 된 경우)

```bash
# Docker 설치
sudo apt-get update
sudo apt-get install -y docker.io docker-compose

# Docker 서비스 시작
sudo systemctl start docker
sudo systemctl enable docker

# 현재 사용자를 docker 그룹에 추가
sudo usermod -aG docker $USER

# 재로그인 (그룹 변경 적용)
exit
ssh -i perfacto-key.pem ubuntu@YOUR_EC2_IP
```

### 3. 프로젝트 디렉토리 생성

```bash
# 홈 디렉토리에 프로젝트 폴더 생성
mkdir -p ~/perfacto-server
cd ~/perfacto-server

# 또는 다른 경로 사용 시
mkdir -p /home/ubuntu/perfacto-server
cd /home/ubuntu/perfacto-server
```

### 4. docker-compose.yml 업로드

방법 1: 로컬에서 scp로 전송
```bash
# 로컬 터미널에서
scp -i perfacto-key.pem \
  /Users/mac/spring_boot_proj/perfacto_server/docker-compose.yml \
  ubuntu@YOUR_EC2_IP:~/perfacto-server/

scp -i perfacto-key.pem \
  -r /Users/mac/spring_boot_proj/perfacto_server/nginx \
  ubuntu@YOUR_EC2_IP:~/perfacto-server/
```

방법 2: EC2에서 직접 생성
```bash
# EC2에서
nano ~/perfacto-server/docker-compose.yml
# (내용 복사 붙여넣기)
```

### 5. application-prod.yml 업로드 (환경 설정)

```bash
# 로컬에서 전송
scp -i perfacto-key.pem \
  /Users/mac/spring_boot_proj/perfacto_server/application-prod.yml \
  ubuntu@YOUR_EC2_IP:~/perfacto-server/
```

### 6. 환경 변수 설정 (DB 비밀번호 등)

```bash
# EC2에서
nano ~/perfacto-server/.env

# 내용 예시:
SPRING_DATASOURCE_PASSWORD=your_db_password
SPRING_PROFILES_ACTIVE=prod
```

### 7. docker-compose.yml 수정

```yaml
version: '3.8'

services:
  backend:
    image: ohsehun/spring-backend:latest  # DOCKER_USERNAME과 일치해야 함
    container_name: spring-backend
    restart: always
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
    env_file:
      - .env
    networks:
      - app-network

  redis:
    image: redis:alpine
    container_name: redis
    restart: always
    networks:
      - app-network

  nginx:
    image: nginx:latest
    container_name: nginx
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/default.conf:/etc/nginx/conf.d/default.conf
      - ./nginx/certbot:/etc/letsencrypt
    depends_on:
      - backend
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```

---

## 🚀 3단계: CI/CD 테스트

### 1. GitHub에 변경사항 푸시

```bash
cd /Users/mac/spring_boot_proj/perfacto_server

git add .github/workflows/
git commit -m "feat: CI/CD 파이프라인 구축"
git push origin main
```

### 2. GitHub Actions 확인

1. GitHub Repository 접속
2. "Actions" 탭 클릭
3. "CI/CD Pipeline" 워크플로우 확인
4. 각 단계별 로그 확인

### 3. 배포 확인

```bash
# EC2에서 컨테이너 상태 확인
docker ps

# 로그 확인
docker logs spring-backend

# 서비스 접속 테스트
curl http://localhost:8080/actuator/health
# 또는
curl http://YOUR_EC2_IP:8080/actuator/health
```

---

## 📊 워크플로우 상세 설명

### `deploy.yml` - 메인 배포 파이프라인

**트리거:**
- `main` 브랜치에 push
- `main` 브랜치로 PR

**단계:**
1. ✅ 코드 체크아웃
2. ✅ JDK 17 설정
3. ✅ Gradle 빌드 (`./gradlew clean bootJar`)
4. ✅ 테스트 실행 (`./gradlew test`)
5. 🐳 Docker 이미지 빌드 (main push 시만)
6. 🐳 Docker Hub 푸시 (main push 시만)
7. 🚀 EC2 배포 (main push 시만)
8. 📢 Slack 알림 (선택)

### `pr-check.yml` - PR 검증

**트리거:**
- PR 생성 시

**단계:**
1. ✅ 코드 체크아웃
2. ✅ Gradle 빌드
3. ✅ 테스트 실행
4. 📊 테스트 결과 업로드
5. 💬 PR 코멘트

---

## ⚙️ GitHub Actions 워크플로우 수정 방법

### Docker 이미지 이름 변경

`.github/workflows/deploy.yml` 파일에서:
```yaml
# 현재
docker build -t ${{ secrets.DOCKER_USERNAME }}/spring-backend:latest .

# 변경 예시
docker build -t ${{ secrets.DOCKER_USERNAME }}/perfacto-backend:latest .
```

### EC2 배포 경로 변경

`.github/workflows/deploy.yml` 파일에서:
```yaml
script: |
  cd /home/ubuntu/perfacto-server  # 이 경로를 변경
  docker-compose pull
  docker-compose down
  docker-compose up -d
```

### 테스트 건너뛰기 (권장하지 않음)

```yaml
# 테스트 단계 주석 처리
# - name: Run tests
#   run: ./gradlew test
```

---

## 🔧 트러블슈팅

### 1. Docker Hub 푸시 실패

**에러:** `denied: requested access to the resource is denied`

**해결:**
```bash
# 로컬에서 Docker Hub 로그인 테스트
docker login
# Username: ohsehun
# Password: (Access Token)

# 이미지 이름 확인
docker images | grep spring-backend
```

### 2. EC2 SSH 접속 실패

**에러:** `Permission denied (publickey)`

**해결:**
- `EC2_SSH_KEY` 확인: 전체 키 내용 복사되었는지
- EC2 Security Group: SSH(22) 포트 열려있는지
- EC2 인스턴스: 실행 중인지

```bash
# SSH 키 권한 확인
chmod 400 perfacto-key.pem

# 직접 접속 테스트
ssh -i perfacto-key.pem ubuntu@YOUR_EC2_IP
```

### 3. Docker 컨테이너 실행 실패

**확인:**
```bash
# EC2에서
docker logs spring-backend

# 환경 변수 확인
docker exec spring-backend env | grep SPRING

# docker-compose 재시작
docker-compose down
docker-compose up -d
```

### 4. Gradle 빌드 실패

**에러:** `Could not resolve dependencies`

**해결:**
```bash
# 로컬에서 빌드 테스트
./gradlew clean build --info

# Gradle 캐시 삭제
rm -rf ~/.gradle/caches
./gradlew clean build
```

---

## 📱 배포 후 확인 사항

### 1. API 엔드포인트 테스트

```bash
# Health Check
curl http://YOUR_EC2_IP:8080/actuator/health

# 공개 API 테스트
curl http://YOUR_EC2_IP:8080/api/public/places

# 인증 API 테스트 (로그인)
curl -X POST http://YOUR_EC2_IP:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password"}'
```

### 2. 로그 모니터링

```bash
# 실시간 로그 확인
docker logs -f spring-backend

# 최근 100줄
docker logs --tail 100 spring-backend

# 에러 로그만
docker logs spring-backend 2>&1 | grep ERROR
```

### 3. 리소스 사용량

```bash
# 컨테이너 상태
docker stats

# 디스크 사용량
df -h

# 메모리 사용량
free -m
```

---

## 🎯 다음 단계

### 필수 작업
- [ ] GitHub Secrets 설정 완료
- [ ] EC2 서버 Docker 설치
- [ ] docker-compose.yml 배포
- [ ] 첫 배포 테스트

### 권장 작업
- [ ] Slack 알림 설정
- [ ] 로그 수집 시스템 (CloudWatch, ELK)
- [ ] 모니터링 대시보드 (Grafana)
- [ ] 자동 롤백 설정
- [ ] 스테이징 환경 구축

### 보안 강화
- [ ] SSL/TLS 인증서 설정 (Let's Encrypt)
- [ ] 환경 변수 암호화
- [ ] IAM 역할 기반 접근
- [ ] VPC 네트워크 격리

---

## 📞 문제 발생 시

1. GitHub Actions 로그 확인
2. EC2 서버 로그 확인 (`docker logs spring-backend`)
3. 이 가이드의 트러블슈팅 섹션 참고
4. GitHub Issues에 문제 등록

---

**작성일:** 2024-12-21
**버전:** 1.0
**작성자:** Claude Code
