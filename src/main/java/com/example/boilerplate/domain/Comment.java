package com.example.boilerplate.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String content; // 댓글 내용

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private InstagramPost post; // 댓글이 속한 게시물

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member author; // 댓글 작성자

  private LocalDateTime createdAt;

  public Comment() {}

  public Comment(String content, InstagramPost post, Member author) {
    this.content = content;
    this.post = post;
    this.author = author;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public InstagramPost getPost() {
    return post;
  }

  public Member getAuthor() {
    return author;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}