package com.fitlife.repository;

import com.fitlife.entity.CheckInHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckInHistoryRepository extends JpaRepository<CheckInHistory, Long> {

    // (Tuỳ chọn sau này) Tìm tất cả lịch sử quẹt thẻ của một người
    // List<CheckInHistory> findByMemberIdOrderByCheckInTimeDesc(Long memberId);
}