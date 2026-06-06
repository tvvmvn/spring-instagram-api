package com.example.boilerplate.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {
   
  // ⚠️ 보안상 최소 32바이트(256비트) 이상의 가로로 긴 무작위 키셋문자열이 지정되어야 에러가 나지 않습니다.
  @Value("${jwt.secret:yourSuperSecretKeyMustBeAtLeast32BytesLongAndSecure!}")
  private String secretKeyString;
  private SecretKey secretKey;
  private final long validityInMilliseconds = 3600000; // 1시간

  @PostConstruct
  protected void init() {
    this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
  }

  // 토큰 생성
  public String createToken(String username) {
    Claims claims = Jwts.claims().subject(username).build();
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .claims(claims)
        .issuedAt(now)
        .expiration(validity)
        .signWith(secretKey)
        .compact();
  }

  // 토큰 정보 추출을 통한 Authentication 객체 생성
  public Authentication getAuthentication(String token) {
    String username = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();

    UserDetails userDetails = new User(username, "", Collections.emptyList());
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 유효성 및 만료 여부 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}