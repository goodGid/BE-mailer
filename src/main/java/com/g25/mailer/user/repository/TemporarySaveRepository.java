package com.g25.mailer.user.repository;

import com.g25.mailer.user.entity.TemporarySave;
import com.g25.mailer.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemporarySaveRepository extends JpaRepository<TemporarySave, Long> {
    // 특정 사용자에 대한 임시저장 목록 조회
    List<TemporarySave> findByUser(User user);

    // 특정 사용자에 대한 전체 임시 저장 항목 삭제
    List<TemporarySave> deleteByUser(User user);

    // 특정 사용자의 특정 임시 자정 항목 삭제
    List<TemporarySave> deleteByIdAndUser(Long id, User user);

}
