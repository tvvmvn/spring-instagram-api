package com.example.boilerplate.dto;

import org.springframework.web.multipart.MultipartFile;

// 💡 [변경] 파일 전송 처리를 위해 선언한 MultipartForm 대응 DTO
public class InstagramPostFormDto {

  private MultipartFile imageFile; // Swagger 화면에서 파일 선택창으로 변함
  private String caption;
  private String hashtags;

  public InstagramPostFormDto() {}

  public MultipartFile getImageFile() {
    return imageFile;
  }

  public void setImageFile(MultipartFile imageFile) {
    this.imageFile = imageFile;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public String getHashtags() {
    return hashtags;
  }

  public void setHashtags(String hashtags) {
    this.hashtags = hashtags;
  }
}