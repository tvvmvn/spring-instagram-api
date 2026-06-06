package com.example.boilerplate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "인증 요청 DTO (로그인 및 회원가입)")
public class AuthRequestDto {

  @Schema(description = "사용자 아이디 (유저네임)", example = "instagram_user")
  private String username;

  @Schema(description = "비밀번호", example = "password1234")
  private String password;

  public AuthRequestDto() {}

  public AuthRequestDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}