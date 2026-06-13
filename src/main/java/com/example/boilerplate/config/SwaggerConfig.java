package com.example.boilerplate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {

    String securitySchemeName = "BearerAuth";

    // 1. JWT 인증 헤더 스키마 정의 (Authorization: Bearer <토큰>)
    SecurityScheme securityScheme = new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT")
        .in(SecurityScheme.In.HEADER)
        .name("Authorization");

    // 2. 모든 API 문서에 이 인증 제약조건을 기본 적용
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

    return new OpenAPI()
        .info(new Info()
            .title("Spring Boot 4.0 JWT Auth API Docs")
            .description("회원가입, JPA 로그인 및 JWT 검증이 포함된 REST API 명세서입니다.")
            .version("v1.0.0"))
        .addSecurityItem(securityRequirement)
        .components(new Components().addSecuritySchemes(securitySchemeName, securityScheme));
  }
}