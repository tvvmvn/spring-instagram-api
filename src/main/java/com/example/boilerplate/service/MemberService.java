package com.example.boilerplate.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.boilerplate.domain.Follow;
import com.example.boilerplate.domain.InstagramPost;
import com.example.boilerplate.domain.Member;
//outer and inner class
import com.example.boilerplate.dto.UserProfileResponseDto;
import com.example.boilerplate.dto.UserProfileResponseDto.ProfilePostDto;
import com.example.boilerplate.dto.UserSearchResponseDto;
import com.example.boilerplate.repository.FollowRepository;
import com.example.boilerplate.repository.InstagramPostRepository;
import com.example.boilerplate.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;
  private final InstagramPostRepository instagramPostRepository;
  private final FollowRepository followRepository; // 💡 주입 추가

  @Value("${file.upload-dir}")
  private String uploadDir;

  public MemberService(MemberRepository memberRepository, InstagramPostRepository instagramPostRepository, FollowRepository followRepository) {
    this.memberRepository = memberRepository;
    this.instagramPostRepository = instagramPostRepository;
    this.followRepository = followRepository;
  }

  // 💡 프로필 설정 변경 비즈니스 로직 이관
  @Transactional
  public void updateProfile(MultipartFile profileFile, String bio, String username) throws IOException {
    
    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 계정입니다."));

    String finalProfileImageUrl = member.getProfileImageUrl();

    if (profileFile != null && !profileFile.isEmpty()) {
      File dir = new File(uploadDir);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      String savedFilename = UUID.randomUUID().toString() + "_" + profileFile.getOriginalFilename();
      File targetFile = new File(dir, savedFilename);
      profileFile.transferTo(targetFile);

      finalProfileImageUrl = "/uploads/" + savedFilename;
    }

    member.updateProfile(finalProfileImageUrl, bio);
  }

  // 💡 프로필 및 마이페이지 통합 데이터 조회 로직 이관
  public UserProfileResponseDto getUserProfile(String username) {
    
    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 계정입니다."));

    List<InstagramPost> posts = instagramPostRepository.findAllByAuthorOrderByCreatedAtDesc(member);

    List<ProfilePostDto> postDtos = posts.stream()
        .map(post -> new ProfilePostDto(
            post.getId(),
            post.getImageUrl(),
            post.getCaption(),
            post.getHashtags(),
            post.getCreatedAt().toString()
        )).collect(Collectors.toList());
    
    // 💡 레포지토리에서 팔로우, 팔로워 숫자 동적 집계
    int followingCount = followRepository.countByFromMember(member);
    int followerCount = followRepository.countByToMember(member);

    return new UserProfileResponseDto(
        member.getUsername(),
        member.getProfileImageUrl(),
        member.getBio(),
        postDtos,
        followingCount,
        followerCount
    );
  }

  // 💡 팔로우/언팔로우
  @Transactional//readOnly=false
  public String toggleFollow(String fromUsername, String toUsername) {
    
    if (fromUsername.equals(toUsername)) {
      throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
    }

    Member fromMember = memberRepository.findByUsername(fromUsername)
        .orElseThrow(() -> new IllegalArgumentException("로그인 유저 정보가 존재하지 않습니다."));
    
    Member toMember = memberRepository.findByUsername(toUsername)
        .orElseThrow(() -> new IllegalArgumentException("상대방 계정이 존재하지 않습니다."));

    // 이미 팔로우했는지 확인
    return followRepository.findByFromMemberAndToMember(fromMember, toMember)
        .map(follow -> {
          followRepository.delete(follow); // 이미 있다면 언팔로우(삭제)
          return "언팔로우 하였습니다.";
        })
        .orElseGet(() -> {
          followRepository.save(new Follow(fromMember, toMember)); // 없다면 팔로우(저장)
          return "팔로우 하였습니다.";
        });
  }

  // 💡 사용자 검색 비즈니스 로직
  public List<UserSearchResponseDto> searchUsers(String keyword) {
    // 검색어가 공백이거나 비어있으면 그냥 빈 배열 반환
    if (keyword == null || keyword.trim().isEmpty()) {
      return List.of();
    }

    // 조건에 맞는 회원 엔티티 조회
    List<Member> members = memberRepository.findByUsernameContaining(keyword.trim());

    // 엔티티 -> DTO 변환 파이프라인
    return members.stream()
        .map(member -> new UserSearchResponseDto(
            member.getUsername(),
            member.getProfileImageUrl(),
            member.getBio()
        )).collect(Collectors.toList());
  }
}