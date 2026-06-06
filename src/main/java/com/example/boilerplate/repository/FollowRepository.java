package com.example.boilerplate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.boilerplate.domain.Follow;
import com.example.boilerplate.domain.Member;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  
  // 팔로우 기록 찾기 (언팔로우나 중복 검증용)
  Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);

  // 팔로잉 수 카운트 (내가 팔로우하는 사람 수)
  int countByFromMember(Member fromMember);

  // 팔로워 수 카운트 (나를 팔로우하는 사람 수)
  int countByToMember(Member toMember);
}