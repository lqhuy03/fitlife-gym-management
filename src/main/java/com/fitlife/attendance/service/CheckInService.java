package com.fitlife.attendance;

import com.fitlife.attendance.dto.CheckInResponse;

public interface CheckInService {
        // Đảm bảo an toàn dữ liệu
    CheckInResponse processCheckIn(Long memberId, String actorUsername);
}
