package com.fitlife.analytics;

public interface TrafficAnalyticsService {
    long getTotalMembers();
    long getActiveMembers();
    long getTotalCheckinsToday();
}