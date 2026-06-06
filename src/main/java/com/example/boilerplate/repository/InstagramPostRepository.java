package com.example.boilerplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.domain.InstagramPost;
import com.example.boilerplate.domain.Member;

public interface InstagramPostRepository extends JpaRepository<InstagramPost, Long> {
  
  // 💡 최신 인스타 피드가 상단에 오도록 전체 조회
  List<InstagramPost> findAllByOrderByCreatedAtDesc();

  // 💡 [추가] 특정 작성자(Member)의 게시물만 최신순으로 조회하는 규칙
  List<InstagramPost> findAllByAuthorOrderByCreatedAtDesc(Member author);

  // 💡 [추가] 피드 공급자 목록(나 + 팔로잉 멤버들)에 해당하는 게시글만 최신순으로 조회
  List<InstagramPost> findAllByAuthorInOrderByCreatedAtDesc(List<Member> authors);
}
