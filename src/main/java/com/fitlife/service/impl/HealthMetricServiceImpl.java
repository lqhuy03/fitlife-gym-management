package com.fitlife.service.impl;

import com.fitlife.dto.HealthMetricRequest;
import com.fitlife.entity.HealthMetric;
import com.fitlife.entity.Member;
import com.fitlife.repository.HealthMetricRepository;
import com.fitlife.repository.MemberRepository;
import com.fitlife.service.HealthMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class HealthMetricServiceImpl implements HealthMetricService {
    private final HealthMetricRepository healthMetricRepository;
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public HealthMetric addHealthMetric(String username, HealthMetricRequest request) {
        // Tìm Member thông qua username của User đang đăng nhập
        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin hội viên"));

        // Logic tính toán BMI
        double heightInMeters = request.getHeight() / 100.0;
        double bmi = request.getWeight() / (heightInMeters * heightInMeters);

        // Làm tròn 2 chữ số thập phân
        bmi = Math.round(bmi * 100.0) / 100.0;

        HealthMetric metric = HealthMetric.builder()
                .member(member)
                .weight(request.getWeight())
                .height(request.getHeight())
                .bmi(bmi)
                .recordedAt(LocalDateTime.now())
                .build();

        return healthMetricRepository.save(metric);
    }

    @Override
    public List<HealthMetric> getMemberHistory(String username) {
        Member member = memberRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Hội viên không tồn tại"));
        return healthMetricRepository.findByMemberIdOrderByRecordedAtDesc(member.getId());
    }
}
