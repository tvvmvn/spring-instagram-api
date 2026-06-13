package com.example.boilerplate.dto;

import org.springframework.web.multipart.MultipartFile;

public class ProfileUpdateFormDto {

  // 폼데이터일때는 setter 필요함..😅
  private MultipartFile profileFile; // 변경할 프로필 이미지 파일
  private String bio; // 변경할 한 줄 소개 내용

  public ProfileUpdateFormDto() {}

  public MultipartFile getProfileFile() {
    return profileFile;
  }

  public void setProfileFile(MultipartFile profileFile) {
    this.profileFile = profileFile;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }
}