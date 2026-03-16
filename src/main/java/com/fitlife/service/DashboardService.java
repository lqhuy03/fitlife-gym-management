package com.fitlife.service;

import com.fitlife.dto.DashboardResponse;
import org.springframework.transaction.annotation.Transactional;

public interface DashboardService {
    @Transactional(readOnly = true)
    DashboardResponse getMemberDashboard(Long memberId);
}
