package com.example.boilerplate.dto;

// 💡 [추가] 인스타 피드 응답 전용 DTO
public class InstagramPostResponseDto {

  private Long id;
  private String imageUrl;
  private String caption;
  private String hashtags;
  private String author;
  private String createdAt;
  private int likeCount; // 💡 추가됨

  public InstagramPostResponseDto(Long id, String imageUrl, String caption, String hashtags, String author, String createdAt, int likeCount) {
    this.id = id;
    this.imageUrl = imageUrl;
    this.caption = caption;
    this.hashtags = hashtags;
    this.author = author;
    this.createdAt = createdAt;
    this.likeCount = likeCount;
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

  public String getAuthor() {
    return author;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public int getLikeCount() {// 💡 추가됨
    return likeCount;
  } 
}
