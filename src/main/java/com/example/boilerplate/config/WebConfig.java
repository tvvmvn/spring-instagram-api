package com.example.boilerplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    
    // 💡 /uploads/** 주소로 들어오는 요청을 물리 폴더 file:///C:/instagram_uploads/ 로 연결합니다.
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:" + uploadDir);
  }
}