package com.fitlife.service.impl;

import com.fitlife.dto.DashboardResponse;
import com.fitlife.entity.HealthMetric;
import com.fitlife.entity.Member;
import com.fitlife.entity.Subscription;
import com.fitlife.entity.WorkoutPlan;
import com.fitlife.repository.*;
import com.fitlife.service.DashboardService;
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
public class DashboardServiceImpl implements DashboardService {

    private final MemberRepository memberRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final HealthMetricRepository HealthMetricRepository;
    private final CheckInHistoryRepository checkInHistoryRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutDetailRepository workoutDetailRepository;

    @Override
    public DashboardResponse getMemberDashboard(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Hội viên không tồn tại"));

        DashboardResponse.DashboardResponseBuilder response = DashboardResponse.builder()
                .memberName(member.getFullName());

        // 1. KIỂM TRA GÓI TẬP (Giữ nguyên logic cực xịn của em)
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

        // ==========================================
        // 2. XỬ LÝ SỐ ĐO SỨC KHỎE (BẢN CHỐNG NULL)
        // ==========================================

        // Bước A: Lấy dữ liệu gốc từ bảng Member làm nền tảng
        Double finalHeight = member.getHeight();
        Double finalWeight = member.getWeight();
        Double finalBmi = 0.0;

        // Bước B: Thử tìm dữ liệu mới nhất trong bảng HealthMetric để ghi đè
        Optional<HealthMetric> latestHealth = HealthMetricRepository.findFirstByMemberOrderByRecordedAtDesc(member);
        if (latestHealth.isPresent()) {
            HealthMetric hm = latestHealth.get();
            if (hm.getHeight() != null) finalHeight = hm.getHeight();
            if (hm.getWeight() != null) finalWeight = hm.getWeight();
            if (hm.getBmi() != null) finalBmi = hm.getBmi();
        }

        // Bước C: Tính lại BMI nếu trong DB đang bằng 0 nhưng lại có Chiều cao/Cân nặng
        if ((finalBmi == null || finalBmi == 0.0) && finalHeight != null && finalWeight != null && finalHeight > 0) {
            double heightInMeters = finalHeight / 100.0;
            finalBmi = finalWeight / (heightInMeters * heightInMeters);
            finalBmi = Math.round(finalBmi * 10.0) / 10.0; // Làm tròn 1 số thập phân
        }

        // Bước D: Đưa vào DTO trả về
        response.currentHeight(finalHeight)
                .currentWeight(finalWeight)
                .bmi(finalBmi)
                .bmiCategory(evaluateBMI(finalBmi));

        // ==========================================

        // 3. ĐẾM SỐ LƯỢT CHECK-IN THÁNG NÀY
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(LocalTime.MAX);
        int checkins = checkInHistoryRepository.countCheckinsInPeriod(memberId, startOfMonth, endOfMonth);
        response.totalCheckinsThisMonth(checkins);

        // 4. TÍNH CALO VÀ BÀI TẬP HOÀN THÀNH
        Optional<WorkoutPlan> currentPlan = workoutPlanRepository.findByMemberAndStatus(member, WorkoutPlan.PlanStatus.ACTIVE);
        if (currentPlan.isPresent()) {
            int completedEx = workoutDetailRepository.countCompletedExercisesByPlanId(currentPlan.get().getId());
            response.completedExercises(completedEx);
            // FORMULA: 1 bài = 45 Kcal.
            response.estimatedCaloriesBurned(completedEx * 45);
        } else {
            response.completedExercises(0);
            response.estimatedCaloriesBurned(0);
        }

        return response.build();
    }

    // AI của hệ thống đánh giá tự động
    // LƯU Ý: Anh đã đưa các câu vui vui vào dấu ngoặc. Frontend chỉ bắt chữ cái đầu để đổi màu.
    private String evaluateBMI(Double bmi) {
        if (bmi == null || bmi == 0.0) return "Chưa có dữ liệu";
        if (bmi < 18.5) return "Thiếu cân"; // Nếu muốn hiện thêm text, ở Frontend sửa lại điều kiện bao gồm (includes) chữ "Thiếu cân"
        if (bmi >= 18.5 && bmi < 24.9) return "Bình thường";
        if (bmi >= 25 && bmi < 29.9) return "Thừa cân";
        return "Béo phì";
    }
}