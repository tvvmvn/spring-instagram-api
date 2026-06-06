package com.example.boilerplate.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "subscribe_uk", columnNames = { "from_member_id", "to_member_id" } // 💡 중복 팔로우 방지 유니크 제약
    )
})
public class Follow {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_member_id", nullable = false)
  private Member fromMember; // 팔로우를 하는 사람 (나)

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_member_id", nullable = false)
  private Member toMember; // 팔로우를 당하는 사람 (상대방)

  private LocalDateTime createdAt;

  public Follow() {
  }

  public Follow(Member fromMember, Member toMember) {
    this.fromMember = fromMember;
    this.toMember = toMember;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public Member getFromMember() {
    return fromMember;
  }

  public Member getToMember() {
    return toMember;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }
}