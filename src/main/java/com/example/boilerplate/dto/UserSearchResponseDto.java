package com.example.boilerplate.dto;

public class UserSearchResponseDto {

  private String username;
  private String profileImageUrl;
  private String bio;

  public UserSearchResponseDto(String username, String profileImageUrl, String bio) {
    this.username = username;
    this.profileImageUrl = profileImageUrl;
    this.bio = bio;
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
}