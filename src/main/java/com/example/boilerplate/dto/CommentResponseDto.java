package com.example.boilerplate.dto;

public class CommentResponseDto {

  private Long id;
  private String content;
  private String author;
  private String createdAt;

  public CommentResponseDto(Long id, String content, String author, String createdAt) {
    this.id = id;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  public String getAuthor() {
    return author;
  }

  public String getCreatedAt() {
    return createdAt;
  }
}