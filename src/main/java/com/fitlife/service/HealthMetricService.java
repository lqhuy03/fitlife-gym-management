package com.fitlife.service;

import com.fitlife.dto.HealthMetricRequest;
import com.fitlife.entity.HealthMetric;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HealthMetricService {
    HealthMetric addHealthMetric(String username, HealthMetricRequest request);

    List<HealthMetric> getMemberHistory(String username);
}
