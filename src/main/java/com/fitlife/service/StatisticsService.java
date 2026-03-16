package com.fitlife.service;

import com.fitlife.dto.DashboardResponse;

public interface StatisticsService {
    DashboardResponse getMemberDashboard(Long memberId);
}
