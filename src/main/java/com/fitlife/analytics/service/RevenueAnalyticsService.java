package com.fitlife.analytics;

import java.util.Map;

public interface RevenueAnalyticsService {
    Double getTotalRevenue();
    Map<String, Double> getMonthlyRevenue();
}