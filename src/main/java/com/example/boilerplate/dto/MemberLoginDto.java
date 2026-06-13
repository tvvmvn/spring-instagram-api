package com.example.boilerplate.dto;

import jakarta.validation.constraints.NotBlank;

public class MemberLoginDto {
    
  @NotBlank(message = "아이디를 입력해주세요.")
  private String username;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  //
  public MemberLoginDto() {}

  public MemberLoginDto(String username, String password) {
    this.username = username;
    this.password = password;
  }

  // Getter
  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
