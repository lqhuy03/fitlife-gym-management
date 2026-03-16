package com.fitlife.service;

import com.fitlife.entity.WorkoutPlan;
import org.springframework.transaction.annotation.Transactional;

public interface WorkoutService {
    WorkoutPlan getCurrentPlanByUsername(String username);

    void toggleWorkoutDetailStatus(Long detailId);
}
