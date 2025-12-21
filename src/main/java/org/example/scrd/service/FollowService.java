package org.example.scrd.service;

import lombok.RequiredArgsConstructor;
import org.example.scrd.domain.Follow;
import org.example.scrd.domain.User;
import org.example.scrd.dto.UserProfileResponse;
import org.example.scrd.repo.FollowRepository;
import org.example.scrd.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 팔로우 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    /**
     * 팔로우
     */
    @Transactional
    public void follow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new IllegalArgumentException("팔로워 사용자를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new IllegalArgumentException("팔로잉 사용자를 찾을 수 없습니다."));

        // 이미 팔로우 중인지 확인
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }

        // 팔로우 생성
        Follow follow = Follow.create(follower, following);
        followRepository.save(follow);

        // 카운트 업데이트
        follower.incrementFollowingCount();
        following.incrementFollowerCount();
        userRepository.save(follower);
        userRepository.save(following);
    }

    /**
     * 언팔로우
     */
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new IllegalArgumentException("팔로워 사용자를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new IllegalArgumentException("팔로잉 사용자를 찾을 수 없습니다."));

        // 팔로우 관계 찾기
        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
            .orElseThrow(() -> new IllegalArgumentException("팔로우 관계가 존재하지 않습니다."));

        // 팔로우 삭제
        followRepository.delete(follow);

        // 카운트 업데이트
        follower.decrementFollowingCount();
        following.decrementFollowerCount();
        userRepository.save(follower);
        userRepository.save(following);
    }

    /**
     * 팔로잉 목록 조회
     */
    public List<UserProfileResponse> getFollowingList(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.findAllByFollower(user);

        return follows.stream()
            .map(follow -> UserProfileResponse.from(follow.getFollowing(), true))
            .collect(Collectors.toList());
    }

    /**
     * 팔로워 목록 조회
     */
    public List<UserProfileResponse> getFollowerList(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Follow> follows = followRepository.findAllByFollowing(user);
        Long currentUserId = userId; // 실제로는 SecurityContext에서 가져와야 함

        return follows.stream()
            .map(follow -> {
                User follower = follow.getFollower();
                boolean isFollowedByMe = followRepository.existsByFollowerAndFollowing(user, follower);
                return UserProfileResponse.from(follower, isFollowedByMe);
            })
            .collect(Collectors.toList());
    }

    /**
     * 팔로우 여부 확인
     */
    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
            .orElseThrow(() -> new IllegalArgumentException("팔로워 사용자를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
            .orElseThrow(() -> new IllegalArgumentException("팔로잉 사용자를 찾을 수 없습니다."));

        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    /**
     * 팔로잉 ID 목록 조회
     */
    public List<Long> getFollowingIds(Long userId) {
        return followRepository.findFollowingIdsByUserId(userId);
    }
}
