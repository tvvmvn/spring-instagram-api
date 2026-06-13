package com.example.boilerplate.domain;

import jakarta.persistence.*;

@Entity
public class Member {

  @Id//PK
  // PK의 생성전략. AUTO_INCREMENT
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password; // 암호화된 비밀번호가 저장됨

  // 💡 [추가] 프로필 사진 경로 필드 (기본값 세팅)
  private String profileImageUrl = "/uploads/default_profile.png";

  // 💡 [추가] 한 줄 소개 필드
  private String bio = "";

  //
  public Member() {}

  public Member(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getBio() {
    return bio;
  }

  // 💡 [추가] 프로필 업데이트 편의 메서드
  public void updateProfile(String profileImageUrl, String bio) {
    this.profileImageUrl = profileImageUrl;
    this.bio = bio;
  }
}