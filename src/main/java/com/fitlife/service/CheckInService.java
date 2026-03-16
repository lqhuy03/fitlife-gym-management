package com.fitlife.service;

import com.fitlife.dto.CheckInResponse;
import org.springframework.transaction.annotation.Transactional;

public interface CheckInService {
        // Đảm bảo an toàn dữ liệu
    CheckInResponse processCheckIn(Long memberId, String actorUsername);
}
