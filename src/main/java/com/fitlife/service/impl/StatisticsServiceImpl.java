package com.fitlife.service.impl;

import com.fitlife.dto.DashboardResponse;
import com.fitlife.entity.HealthMetric;
import com.fitlife.entity.Member;
import com.fitlife.entity.Subscription;
import com.fitlife.repository.CheckInHistoryRepository;
import com.fitlife.repository.HealthMetricRepository; // Import thêm cái này
import com.fitlife.repository.MemberRepository;
import com.fitlife.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final CheckInHistoryRepository checkInHistoryRepository;
    private final MemberRepository memberRepository;
    private final HealthMetricRepository healthMetricRepository; // Tiêm Repo mới vào

    @Override
    public DashboardResponse getMemberDashboard(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hội viên"));

        // --- CHIẾN THUẬT LẤY DỮ LIỆU THÔNG MINH ---
        // 1. Thử lấy chỉ số từ bảng theo dõi sức khỏe mới nhất
        HealthMetric latestMetric = healthMetricRepository.findFirstByMemberOrderByRecordedAtDesc(member)
                .orElse(null);

        // 2. Nếu có metric thì lấy, không thì lấy từ Profile Member, cuối cùng là 0.0
        double h = (latestMetric != null) ? latestMetric.getHeight() : (member.getHeight() != null ? member.getHeight() : 0.0);
        double w = (latestMetric != null) ? latestMetric.getWeight() : (member.getWeight() != null ? member.getWeight() : 0.0);

        System.out.println("===> DASHBOARD LOG <===");
        System.out.println("Nguồn dữ liệu: " + (latestMetric != null ? "Bảng HealthMetrics" : "Bảng Members"));
        System.out.println("Sử dụng Height: " + h + " | Weight: " + w);

        // Tính BMI và các thông số khác (giữ nguyên logic cũ)
        double heightMeters = h / 100.0;
        double bmiValue = (heightMeters > 0) ? w / (heightMeters * heightMeters) : 0;
        double roundedBmi = Math.round(bmiValue * 10.0) / 10.0;

        Subscription activeSub = member.getActiveSubscription();
        long daysRemaining = 0;
        if (activeSub != null && activeSub.getEndDate() != null) {
            daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), activeSub.getEndDate());
        }

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        int checkins = checkInHistoryRepository.countCheckinsInPeriod(memberId, startOfMonth, LocalDateTime.now());

        return DashboardResponse.builder()
                .memberName(member.getFullName())
                .currentPackageName(activeSub != null ? activeSub.getGymPackage().getName() : "Chưa đăng ký")
                .daysRemaining(Math.max(0, daysRemaining))
                .currentWeight(w)
                .currentHeight(h)
                .bmi(roundedBmi)
                .bmiCategory(getBmiCategory(roundedBmi))
                .totalCheckinsThisMonth(checkins)
                .completedExercises(0)
                .estimatedCaloriesBurned(checkins * 300)
                .build();
    }

    private String getBmiCategory(double bmi) {
        if (bmi <= 0) return "Chưa có dữ liệu";
        if (bmi < 18.5) return "Hơi gầy";
        if (bmi < 25) return "Bình thường (Body quá đẹp!)";
        if (bmi < 30) return "Thừa cân";
        return "Béo phì";
    }
}