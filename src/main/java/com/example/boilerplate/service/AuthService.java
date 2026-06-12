package com.example.boilerplate.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.boilerplate.domain.Member;
import com.example.boilerplate.repository.MemberRepository;
import com.example.boilerplate.security.JwtTokenProvider;

@Service
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(
    MemberRepository memberRepository, 
    PasswordEncoder passwordEncoder,
    JwtTokenProvider jwtTokenProvider) {
    
    this.memberRepository = memberRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  // 1. 회원가입
  public String register(String username, String password) {
    
    if (memberRepository.findByUsername(username).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
    }

    // 비밀번호를 BCrypt로 암호화하여 저장
    String encodedPassword = passwordEncoder.encode(password);
    
    Member member = new Member(username, encodedPassword);
    
    memberRepository.save(member);

    return "회원가입이 완료되었습니다.";
  }

  // 2. 로그인 검증 및 토큰 발급
  public String login(String username, String password) {
    
    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    // DB에 저장된 암호화 비밀번호와 입력된 비밀번호 매칭 확인
    if (!passwordEncoder.matches(password, member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    // 인증 성공 시 JWT 토큰 생성 후 반환
    return jwtTokenProvider.createToken(username);
  }
}
