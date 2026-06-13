package com.example.boilerplate.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.dto.ProfileUpdateFormDto;
import com.example.boilerplate.dto.UserProfileResponseDto;
import com.example.boilerplate.dto.UserSearchResponseDto;
import com.example.boilerplate.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "3. 회원 프로필 관리 (Member)", description = "인스타 회원 계정의 프로필 조회 및 편집 API를 제공합니다.")
@RestController
@RequestMapping("/api/members") // prefix
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  // 프로필 수정하기
  @Operation(summary = "프로필 설정 변경", description = "로그인한 유저의 프로필 사진 파일과 한 줄 소개(Bio) 정보를 편집합니다.")
  @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateProfile(
      @ModelAttribute ProfileUpdateFormDto formDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    try {
      String username = userDetails.getUsername();
      //
      memberService.updateProfile(formDto.getProfileFile(), formDto.getBio(), username);

      return ResponseEntity.ok(Map.of("message", "프로필 설정이 성공적으로 업데이트되었습니다."));

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 프로필 페이지
  @Operation(summary = "계정 상세보기 (프로필 페이지)", description = "지정한 유저네임(아이디)의 프로필 정보와 해당 유저가 올린 피드 리스트를 한눈에 보여줍니다.")
  @GetMapping("/profile/{username}")
  public ResponseEntity<UserProfileResponseDto> getUserProfile(@PathVariable("username") String username) {
    
    try {
      UserProfileResponseDto response = memberService.getUserProfile(username);
      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(null);
    }
  }

  // 팔로우/언팔로우 요청
  @Operation(summary = "팔로우 / 언팔로우 토글", description = "상대방의 유저네임을 받아 팔로우 상태이면 언팔로우를, 팔로우 상태가 아니면 팔로우를 진행합니다.")
  @PostMapping("/follow/{username}")
  public ResponseEntity<?> toggleFollow(
      @PathVariable("username") String toUsername,
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    try {
      String fromUsername = userDetails.getUsername();
      
      String message = memberService.toggleFollow(fromUsername, toUsername);

      return ResponseEntity.ok(Map.of("message", message));

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  // 사용자 검색하기
  @Operation(summary = "사용자 검색", description = "유저네임(아이디)의 일부를 입력받아 일치하는 회원 목록을 리스트로 반환합니다.")
  @GetMapping("/search")
  public ResponseEntity<List<UserSearchResponseDto>> searchUsers(
    @RequestParam("keyword") String keyword) {

    List<UserSearchResponseDto> result = memberService.searchUsers(keyword);
    
    return ResponseEntity.ok(result);
  }
}
