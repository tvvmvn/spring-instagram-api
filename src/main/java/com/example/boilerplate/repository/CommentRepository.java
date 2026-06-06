package com.example.boilerplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.domain.Comment;
import com.example.boilerplate.domain.InstagramPost;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  
  // 💡 특정 게시글의 댓글 목록을 등록 순서대로 조회
  List<Comment> findAllByPostOrderByCreatedAtAsc(InstagramPost post);
}