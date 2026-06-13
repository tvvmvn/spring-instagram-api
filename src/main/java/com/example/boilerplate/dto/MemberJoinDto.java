package com.example.boilerplate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberJoinDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 15, message = "아이디는 4~15자 사이로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$", 
             message = "비밀번호는 영문, 숫자 조합 8~20자로 입력해주세요.")
    private String password;
    
    // 💡 아무것도 안 해도 됨!
    public MemberJoinDto() {}

    // 값 세팅용 생성자
    public MemberJoinDto(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
    }

    // 💡 Getter: 데이터 조회용
    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }
}