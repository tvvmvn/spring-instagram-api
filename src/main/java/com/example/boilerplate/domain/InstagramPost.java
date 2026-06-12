package com.example.boilerplate.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class InstagramPost {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String imageUrl; // 인스타 피드 필수 이미지 주소

  @Column(columnDefinition = "TEXT")
  private String caption; // 피드 본문 내용

  private String hashtags; // 예: "#개발자 #일상 #스프링부트"

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member author; // 작성자 연관관계

  private LocalDateTime createdAt;

  //
  public InstagramPost() {}

  public InstagramPost(String imageUrl, String caption, String hashtags, Member author) {
    this.imageUrl = imageUrl;
    this.caption = caption;
    this.hashtags = hashtags;
    this.author = author;
    this.createdAt = LocalDateTime.now();
  }

  // Getter
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

  public Member getAuthor() {
    return author;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}