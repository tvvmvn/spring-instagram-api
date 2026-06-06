package com.example.boilerplate.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.boilerplate.domain.Comment;
import com.example.boilerplate.domain.Follow;
import com.example.boilerplate.domain.InstagramPost;
import com.example.boilerplate.domain.Member;
import com.example.boilerplate.domain.PostLike;
import com.example.boilerplate.repository.CommentRepository;
import com.example.boilerplate.repository.FollowRepository;
import com.example.boilerplate.repository.InstagramPostRepository;
import com.example.boilerplate.repository.MemberRepository;
import com.example.boilerplate.repository.PostLikeRepository;

@Component
public class DataInitializer implements CommandLineRunner {

  private final MemberRepository memberRepository;
  private final InstagramPostRepository instagramPostRepository;
  private final FollowRepository followRepository;
  private final PostLikeRepository postLikeRepository;
  private final CommentRepository commentRepository;
  private final PasswordEncoder passwordEncoder;

  public DataInitializer(
      MemberRepository memberRepository,
      InstagramPostRepository instagramPostRepository,
      FollowRepository followRepository,
      PostLikeRepository postLikeRepository,
      CommentRepository commentRepository,
      PasswordEncoder passwordEncoder
  ) {
    this.memberRepository = memberRepository;
    this.instagramPostRepository = instagramPostRepository;
    this.followRepository = followRepository;
    this.postLikeRepository = postLikeRepository;
    this.commentRepository = commentRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    // 💡 ddl-auto가 update일 때 데이터가 매번 중복 누적되는 것을 방지하기 위해, 비어있을 때만 실행되도록 방어벽을 칩니다.
    if (memberRepository.count() > 0) {
      return;
    }

    System.out.println("====== 🚀 인스타그램 데모 샘플 데이터 초기화 시작 ======");

    // 1. 샘플 유저 생성 (비밀번호는 시큐리티 규격에 맞게 인코딩)
    String encryptedPwd = passwordEncoder.encode("1234");
    
    Member iu = new Member("dlwlrma", encryptedPwd); // 아이유
    iu.updateProfile("/uploads/iu_profile.png", "지금은 정규 앨범 준비 중.. 🎵");
    
    Member gd = new Member("xxxibgdrgn", encryptedPwd); // 지드래곤
    gd.updateProfile("/uploads/gd_profile.png", "PEACEMINUSONE. 🕊️");
    
    Member son = new Member("hm_son7", encryptedPwd); // 손흥민
    son.updateProfile("/uploads/son_profile.png", "COYS! 🤍 토트넘 핫스퍼 주장 손흥민입니다.");

    memberRepository.saveAll(List.of(iu, gd, son));

    // 2. 인스타 감성 게시물(피드) 등록
    // (※ 톰캣 정적 리소스 매핑 설정에 부합하도록 이미지 경로 세팅)
    InstagramPost post1 = new InstagramPost("/uploads/concert.jpg", "오랜만에 스타디움 투어 콘서트! 관객 여러분 모두 감사드립니다. 💜", "#콘서트 #아이유 #행복", iu);
    InstagramPost post2 = new InstagramPost("/uploads/fashion.jpg", "New Brand Shooting. 📸 Chanel Open.", "#fashion #ootd #GD", gd);
    InstagramPost post3 = new InstagramPost("/uploads/football.jpg", "주말 경기 대승! 응원해주신 팬분들 덕분입니다. 감사합니다 대한민국! 🇰🇷", "#EPL #축구 #토트넘", son);
    InstagramPost post4 = new InstagramPost("/uploads/guitar.jpg", "집에서 취미 생활 중.. 🎸 손가락이 아프네요.", "#일상 #기타 #집순이", iu);

    instagramPostRepository.saveAll(List.of(post1, post2, post3, post4));

    // 3. 스타들 간의 상호 팔로우 관계 형성
    
    // 지디와 흥민이는 아이유를 팔로우함
    followRepository.save(new Follow(gd, iu));
    followRepository.save(new Follow(son, iu));
    // 아이유도 지디를 팔로우함
    followRepository.save(new Follow(iu, gd));

    // 4. 게시물 좋아요(Like) 활성화
    // 아이유의 첫 번째 콘서트 글에 지디와 흥민이가 하트를 누름
    postLikeRepository.save(new PostLike(gd, post1));
    postLikeRepository.save(new PostLike(son, post1));
    // 흥민이의 축구 피드에 아이유가 하트를 누름
    postLikeRepository.save(new PostLike(iu, post3));

    // 5. 실시간 소통 댓글(Comment) 적재
    commentRepository.save(new Comment("콘서트 진짜 역대급이었어요!! 감동입니다 😭", post1, gd));
    commentRepository.save(new Comment("손세이셔널! 오늘 골 멋졌습니다!! 🔥👏", post3, iu));
    commentRepository.save(new Comment("언니 이번 신곡 무한 스트리밍 중이에요 🎧", post4, son));

    System.out.println("====== 🏁 인스타그램 데모 샘플 데이터 초기화 완료 ======");
  }
}