package com.example.boilerplate.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.dto.CommentRequestDto;
import com.example.boilerplate.dto.CommentResponseDto;
import com.example.boilerplate.dto.InstagramPostFormDto;
import com.example.boilerplate.dto.InstagramPostResponseDto;
import com.example.boilerplate.service.InstagramPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2. 피드 제어 (Feed)", description = "인증된 사용자의 사진 업로드 및 피드 게시 기능을 관리합니다.")
@RestController
@RequestMapping("/api/instagram/posts")
public class InstagramPostController {

  private final InstagramPostService instagramPostService;

  public InstagramPostController(InstagramPostService instagramPostService) {
    this.instagramPostService = instagramPostService;
  }

  // 게시물 업로드
  @Operation(summary = "새 피드 게시물 업로드 (진짜 사진 파일)", description = "컴퓨터에 있는 실제 이미지 파일과 본문, 해시태그를 폼 형태로 전송받아 영구 저장합니다.")
  @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createFeed(
      @ModelAttribute InstagramPostFormDto formDto, // 💡 Multipart 전송 폼 객체 바인딩
      @AuthenticationPrincipal UserDetails userDetails) {
    
    try {
      String username = userDetails.getUsername();

      Long postId = instagramPostService.uploadFeed(
          formDto.getImageFile(),
          formDto.getCaption(),
          formDto.getHashtags(),
          username);

      return ResponseEntity.status(HttpStatus.CREATED)
          .body(Map.of(
              "message", "인스타그램 피드가 무사히 게시되었습니다.",
              "postId", postId));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  // 타임라인 보기
  @Operation(summary = "인스타그램 전체 타임라인 조회")
  @GetMapping 
  public ResponseEntity<List<InstagramPostResponseDto>> getTimeline(
    @AuthenticationPrincipal UserDetails userDetails) { // 💡 인증 객체 주입

    // 로그인한 내 유저네임 획득
    String username = userDetails.getUsername();

    List<InstagramPostResponseDto> timeline = instagramPostService.getTimelineWithLikeCount(username);
    
    return ResponseEntity.ok(timeline);
  }

  // 게시물 좋아요 토글하기
  @Operation(summary = "게시물 좋아요 토글", description = "특정 게시글의 식별값(ID)을 넘겨 좋아요 상태를 등록하거나 취소합니다.")
  @PostMapping("/{postId}/like")
  public ResponseEntity<?> toggleLike(
      @PathVariable("postId") Long postId,
      @AuthenticationPrincipal UserDetails userDetails
  ) {
    
    try {
      String username = userDetails.getUsername();
      String message = instagramPostService.toggleLike(postId, username);

      return ResponseEntity.ok(Map.of("message", message));
    
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

  // 댓글 목록
  @Operation(summary = "특정 게시글의 댓글 목록 조회", description = "특정 게시글에 달린 모든 댓글 목록을 순서대로 불러옵니다.")
  @GetMapping("/{postId}/comments")
  public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable("postId") Long postId) {

    List<CommentResponseDto> comments = instagramPostService.getComments(postId);
    
    return ResponseEntity.ok(comments);
  }

  // 댓글 쓰기
  @Operation(summary = "댓글 작성", description = "특정 게시글 ID를 지정하여 인증된 계정 명의로 댓글을 등록합니다.")
  @PostMapping("/{postId}/comments")
  public ResponseEntity<?> createComment(
      @PathVariable("postId") Long postId,
      @RequestBody CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetails userDetails
  ) {

    try {
      String username = userDetails.getUsername();
      
      CommentResponseDto response = instagramPostService.createComment(postId, requestDto.getContent(), username);
      
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
  }

}