package com.example.boilerplate.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
  // 💡 중복 좋아요 방지 조치
  @UniqueConstraint(name = "post_like_uk", columnNames = { "member_id", "post_id" })
})
public class PostLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member; // 좋아요를 누른 사람

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private InstagramPost post; // 좋아요를 받은 게시물

  private LocalDateTime createdAt;

  public PostLike() {}

  public PostLike(Member member, InstagramPost post) {
    this.member = member;
    this.post = post;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public InstagramPost getPost() {
    return post;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}