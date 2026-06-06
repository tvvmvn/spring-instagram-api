package com.example.boilerplate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  // JwtAuthenticationFilter.java 내의 shouldNotFilter 메서드 수정
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

    String path = request.getRequestURI();
    
    // 💡 기존 /api/auth/ 외에 swagger 관련 URI 패턴도 필터 제외 대상으로 결합
    return path.contains("/api/auth/") ||
        path.contains("/v3/api-docs") ||
        path.contains("/swagger-ui");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
    throws ServletException, IOException {

    String token = resolveToken(request);

    if (token != null && jwtTokenProvider.validateToken(token)) {
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      // 인증 완료된 사용자 정보를 전역 범위에 저장합니다
      SecurityContextHolder.getContext().setAuthentication(authentication);
    
    } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing access token.");
      return;
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    
    String bearerToken = request.getHeader("Authorization");
    
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    
    return null;
  }
}