package com.example.boilerplate.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.boilerplate.domain.Comment;
import com.example.boilerplate.domain.Follow;
import com.example.boilerplate.domain.InstagramPost;
import com.example.boilerplate.domain.Member;
import com.example.boilerplate.domain.PostLike;
import com.example.boilerplate.dto.CommentResponseDto;
import com.example.boilerplate.dto.InstagramPostResponseDto;
import com.example.boilerplate.repository.CommentRepository;
import com.example.boilerplate.repository.FollowRepository;
import com.example.boilerplate.repository.InstagramPostRepository;
import com.example.boilerplate.repository.MemberRepository;
import com.example.boilerplate.repository.PostLikeRepository;

@Service
@Transactional(readOnly = true)
public class InstagramPostService {

  private final InstagramPostRepository instagramPostRepository;
  private final MemberRepository memberRepository;
  private final FollowRepository followRepository; // 💡 생성자 주입에 포함시켜 줍니다.
  private final PostLikeRepository postLikeRepository; // 💡 주입 추가
  private final CommentRepository commentRepository; // 💡 주입 추가

  @Value("${file.upload-dir}")
  private String uploadDir;// 파일 업로드 경로

  //
  public InstagramPostService(
    InstagramPostRepository instagramPostRepository,
    MemberRepository memberRepository,
    FollowRepository followRepository,
    PostLikeRepository postLikeRepository,
    CommentRepository commentRepository) {

    this.instagramPostRepository = instagramPostRepository;
    this.memberRepository = memberRepository;
    this.followRepository = followRepository;
    this.postLikeRepository = postLikeRepository;
    this.commentRepository = commentRepository;
  }

  // 게시물 올리기
  @Transactional
  public Long uploadFeed(MultipartFile imageFile, String caption, String hashtags, String username) 
    throws IOException {

    Member author = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 계정입니다."));

    // 이미지 없음
    if (imageFile == null || imageFile.isEmpty()) {
      throw new IllegalArgumentException("업로드할 사진 파일이 누락되었습니다.");
    }

    // 1. 저장할 디렉토리 생성 검증
    File dir = new File(uploadDir);
    if (!dir.exists()) {
      dir.mkdirs();
    }

    // 2. 파일명 중복을 방지하기 위해 UUID 결합 파일명 생성 (예: uuid_원본이름.jpg)
    String originalFilename = imageFile.getOriginalFilename();
    String savedFilename = UUID.randomUUID().toString() + "_" + originalFilename;
    String fullPath = Paths.get(uploadDir, savedFilename).toString();

    // 3. 물리 하드디스크 위치에 파일 저장 처리
    imageFile.transferTo(new File(fullPath));

    // 4. DB에는 파일명 혹은 접근 가능한 주소값을 저장
    String imageUrlPath = "/uploads/" + savedFilename;

    InstagramPost post = new InstagramPost(imageUrlPath, caption, hashtags, author);
    InstagramPost savedPost = instagramPostRepository.save(post);

    return savedPost.getId();
  }

  // 타임라인 가져오기
  public List<InstagramPostResponseDto> getTimelineWithLikeCount(String username) {

    // 1. 현재 로그인한 내 정보 획득
    Member me = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 계정입니다."));

    // 2. 내가 팔로우하고 있는 사람들의 목록을 수집할 바구니 생성 및 '나' 자신을 먼저 포함
    List<Member> timelineAuthors = new ArrayList<>();
    timelineAuthors.add(me);

    // 3. 내 팔로잉 명단을 데이터베이스에서 긁어와 바구니에 추가 담기
    // DB의 Follow 테이블에서 fromMember가 '나'인 데이터를 찾음
    List<Follow> followings = followRepository.findAll(); // (또는 레포지토리에 특정 조건 쿼리 메서드 사용 가능)

    // 구조적 단순화를 위해 내 팔로잉 리스트 필터링 추출
    for (Follow follow : followings) {
      if (follow.getFromMember().getId().equals(me.getId())) {
        timelineAuthors.add(follow.getToMember());
      }
    }

    // 4. 리포지토리 IN 연산자를 사용해 피드 노출 명단들의 글만 타겟 조회
    List<InstagramPost> posts = instagramPostRepository
        .findAllByAuthorInOrderByCreatedAtDesc(timelineAuthors);

    return posts.stream()
        .map(post -> {
          // 각 포스트별 좋아요 수 카운트 동적 계산
          int likeCount = postLikeRepository.countByPost(post);

          return new InstagramPostResponseDto(
              post.getId(),
              post.getImageUrl(),
              post.getCaption(),
              post.getHashtags(),
              post.getAuthor().getUsername(),
              post.getCreatedAt().toString(),
              likeCount // 💡 DTO 확장 파라미터 전달
          );
        }).collect(Collectors.toList());
  }

  // 💡 좋아요 토글 처리 로직
  @Transactional
  public String toggleLike(Long postId, String username) {

    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 계정입니다."));

    InstagramPost post = instagramPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 인스타그램 게시물입니다."));

    return postLikeRepository.findByMemberAndPost(member, post)
        .map(like -> {
          postLikeRepository.delete(like); // 이미 존재하면 좋아요 취소
          return "좋아요를 취소했습니다.";
        })
        .orElseGet(() -> {
          postLikeRepository.save(new PostLike(member, post)); // 없으면 좋아요 등록
          return "게시물을 좋아합니다.";
        });
  }

  // 💡 특정 게시글의 전체 댓글 조회
  public List<CommentResponseDto> getComments(Long postId) {

    InstagramPost post = instagramPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 인스타그램 게시물입니다."));

    List<Comment> comments = commentRepository.findAllByPostOrderByCreatedAtAsc(post);

    return comments.stream()
        .map(comment -> new CommentResponseDto(
            comment.getId(),
            comment.getContent(),
            comment.getAuthor().getUsername(),
            comment.getCreatedAt().toString()))
        .collect(Collectors.toList());
  }

  // 💡 [추가] 댓글 작성 로직
  @Transactional
  public CommentResponseDto createComment(Long postId, String content, String username) {
    
    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원 계정입니다."));
    
    InstagramPost post = instagramPostRepository.findById(postId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 인스타그램 게시물입니다."));

    Comment comment = new Comment(content, post, member);
    Comment savedComment = commentRepository.save(comment);

    return new CommentResponseDto(
        savedComment.getId(),
        savedComment.getContent(),
        savedComment.getAuthor().getUsername(),
        savedComment.getCreatedAt().toString());
  }
}