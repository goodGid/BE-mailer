package com.g25.mailer.user.entity;

import com.g25.mailer.user.entity.User;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "temporary_saves", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})//id로 인덱스 걸기
public class TemporarySave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    // 어떤 사용자가 임시저장했는지 연관관계 설정
    // [] 인덱스 걸기 -> 걸었음
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 임시저장할 내용 (예시: 이메일 작성 중 내용 등)
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 임시저장 시각
    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;
}
