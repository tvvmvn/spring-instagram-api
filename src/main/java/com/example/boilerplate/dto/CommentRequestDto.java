package com.example.boilerplate.dto;

public class CommentRequestDto {
  
  private String content;

  public CommentRequestDto() {}

  public CommentRequestDto(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }
}