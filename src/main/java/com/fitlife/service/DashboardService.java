package com.fitlife.service;

import com.fitlife.dto.DashboardResponse;
import com.fitlife.entity.HealthMetric;
import com.fitlife.entity.Member;
import com.fitlife.entity.Subscription;
import com.fitlife.entity.WorkoutPlan;
import com.fitlife.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final HealthMetricRepository healthMetricRepository;
    private final CheckInHistoryRepository checkInHistoryRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutDetailRepository workoutDetailRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getMemberDashboard(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Hội viên không tồn tại"));

        DashboardResponse.DashboardResponseBuilder response = DashboardResponse.builder()
                .memberName(member.getFullName());

        // Check package still valid
        Optional<Subscription> activeSub = subscriptionRepository.findFirstByMemberAndStatus(member, "ACTIVE");
        if (activeSub.isPresent()) {
            Subscription sub = activeSub.get();
            response.currentPackageName(sub.getGymPackage().getName());
            long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), sub.getEndDate());
            response.daysRemaining(Math.max(daysLeft, 0));
        } else {
            response.currentPackageName("Chưa có gói tập");
            response.daysRemaining(0L);
        }

        // Read latest health metric
        Optional<HealthMetric> latestHealth = healthMetricRepository.findFirstByMemberOrderByRecordedAtDesc(member);
        if (latestHealth.isPresent()) {
            HealthMetric hm = latestHealth.get();
            response.currentWeight(hm.getWeight())
                    .currentHeight(hm.getHeight())
                    .bmi(hm.getBmi())
                    .bmiCategory(evaluateBMI(hm.getBmi()));
        } else {
            response.bmiCategory("Chưa đo BMI");
        }

        // Count check-ins this month
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(LocalTime.MAX);
        int checkins = checkInHistoryRepository.countCheckinsInPeriod(memberId, startOfMonth, endOfMonth);
        response.totalCheckinsThisMonth(checkins);

        // Count completed exercises and estimate calories burned
        Optional<WorkoutPlan> currentPlan = workoutPlanRepository.findByMemberAndStatus(member, WorkoutPlan.PlanStatus.ACTIVE);
        if (currentPlan.isPresent()) {
            int completedEx = workoutDetailRepository.countCompletedExercisesByPlanId(currentPlan.get().getId());
            response.completedExercises(completedEx);

            // FORMULA: On average, 1 completed exercise (Including 3 sets, 10 reps) burns about 45 Kcal.
            response.estimatedCaloriesBurned(completedEx * 45);
        } else {
            response.completedExercises(0);
            response.estimatedCaloriesBurned(0);
        }

        return response.build();
    }

    // AI of automatic index evaluation system
    private String evaluateBMI(Double bmi) {
        if (bmi == null) return "Chưa đo";
        if (bmi < 18.5) return "Thiếu cân (Ăn thêm Tinh bột/Protein)";
        if (bmi >= 18.5 && bmi < 24.9) return "Bình thường (Body quá đẹp!)";
        if (bmi >= 25 && bmi < 29.9) return "Thừa cân (Cần đẩy mạnh Cardio)";
        return "Béo phì (Nguy hiểm - Cần PT kèm riêng)";
    }
}