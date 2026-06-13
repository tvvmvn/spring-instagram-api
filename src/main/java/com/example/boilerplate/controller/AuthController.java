package com.example.boilerplate.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.dto.MemberJoinDto;
import com.example.boilerplate.dto.MemberLoginDto;
import com.example.boilerplate.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "1. 인증 계정 관리 (Auth)", description = "회원가입 및 JWT 기반 데이터베이스 로그인을 제어합니다.")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  // 가입하기
  @Operation(summary = "신규 회원가입", description = "아이디와 패스워드를 받아 BCrypt 암호화 후 H2 DB에 적재합니다.")
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody MemberJoinDto dto, BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();

      // 에러가 발생한 모든 필드와 메시지를 맵에 담음
      for (var error : bindingResult.getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
      }

      // { "username": "아이디는 4~15자입니다.", "password": "비밀번호 형식이 틀렸습니다." }
      return ResponseEntity.badRequest().body(errors);
    }

    try {
      String result = authService.register(dto.getUsername(), dto.getPassword());
      
      return ResponseEntity.ok(result);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 로그인
  @Operation(summary = "인증 로그인", description = "DB 정보 일치 시 유저 식별용 JWT Access Token을 발급합니다.")
  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody MemberLoginDto dto, BindingResult bindingResult) {
    
    if (bindingResult.hasErrors()) {
      Map<String, String> errors = new HashMap<>();

      // 에러가 발생한 모든 필드와 메시지를 맵에 담음
      for (var error : bindingResult.getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
      }

      // { "username": "아이디는 4~15자입니다.", "password": "비밀번호 형식이 틀렸습니다." }
      return ResponseEntity.badRequest().body(errors);
    }

    try {
      String token = authService.login(dto.getUsername(), dto.getPassword());
      
      return ResponseEntity.ok(Map.of("accessToken", token));

    } catch (IllegalArgumentException e) {
      return ResponseEntity.status(401).body(e.getMessage());
    }
  }
}