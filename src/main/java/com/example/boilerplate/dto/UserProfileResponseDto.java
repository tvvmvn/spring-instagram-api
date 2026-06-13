package com.example.boilerplate.dto;

import java.util.List;

// 💡 [추가] 계정 상세보기 최종 응답을 위한 상위 DTO
public class UserProfileResponseDto {

  private String username;
  private String profileImageUrl; // 💡 추가
  private String bio; // 💡 추가
  private int postCount; // 총 게시물 개수
  private int followingCount; // 💡 추가
  private int followerCount; // 💡 추가
  private List<ProfilePostDto> posts;

  //
  public UserProfileResponseDto() {}

  public UserProfileResponseDto(String username, String profileImageUrl, String bio, List<ProfilePostDto> posts, int followingCount, int followerCount) {
    this.username = username;
    this.profileImageUrl = profileImageUrl;
    this.bio = bio;
    this.posts = posts;
    this.postCount = posts.size(); // 리스트 크기를 기반으로 글 개수 자동 셋업
    this.followingCount = followingCount;
    this.followerCount = followerCount;
  }

  public String getUsername() {
    return username;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getBio() {
    return bio;
  }

  public int getPostCount() {
    return postCount;
  }

  public int getFollowingCount() {
    return followingCount;
  }

  public int getFollowerCount() {
    return followerCount;
  }

  public List<ProfilePostDto> getPosts() {
    return posts;
  }

  // 💡 [추가] 프로필 피드 격자에 뿌려질 개별 게시물 요약 DTO (작성자 중복 노출 제거)
  public static class ProfilePostDto {

    private Long id;
    private String imageUrl;
    private String caption;
    private String hashtags;
    private String createdAt;

    //
    public ProfilePostDto() {}

    public ProfilePostDto(Long id, String imageUrl, String caption, String hashtags, String createdAt) {
      this.id = id;
      this.imageUrl = imageUrl;
      this.caption = caption;
      this.hashtags = hashtags;
      this.createdAt = createdAt;
    }

    public Long getId() {
      return id;
    }

    public String getImageUrl() {
      return imageUrl;
    }

    public String getCaption() {
      return caption;
    }

    public String getHashtags() {
      return hashtags;
    }

    public String getCreatedAt() {
      return createdAt;
    }
  }
}
