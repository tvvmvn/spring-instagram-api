package com.example.boilerplate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.domain.InstagramPost;
import com.example.boilerplate.domain.Member;
import com.example.boilerplate.domain.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
  
  // 이미 좋아요를 눌렀는지 확인하는 용도
  Optional<PostLike> findByMemberAndPost(Member member, InstagramPost post);

  // 특정 게시물의 총 좋아요 개수 집계
  int countByPost(InstagramPost post);
}
