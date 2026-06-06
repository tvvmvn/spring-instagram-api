package com.example.boilerplate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
  
  Optional<Member> findByUsername(String username);

  // 💡 [추가] 검색어가 포함된 유저네임을 가진 회원 목록 조회 (LIKE %keyword% 효과)
  List<Member> findByUsernameContaining(String keyword);
}