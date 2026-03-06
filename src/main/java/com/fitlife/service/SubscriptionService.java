package com.fitlife.service;

import com.fitlife.dto.SubscriptionCreationRequest;
import com.fitlife.dto.SubscriptionResponse;
import com.fitlife.entity.GymPackage;
import com.fitlife.entity.Member;
import com.fitlife.entity.Subscription;
import com.fitlife.repository.GymPackageRepository;
import com.fitlife.repository.MemberRepository;
import com.fitlife.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final GymPackageRepository gymPackageRepository;

    public SubscriptionResponse createSubscription(SubscriptionCreationRequest request) {

        // 1. Tìm Member và Package
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        GymPackage gymPackage = gymPackageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Package not found"));

        // 2. Validate: Khách có đang sở hữu gói ACTIVE nào không?
        boolean hasActiveSub = subscriptionRepository.existsByMemberAndStatus(member, "ACTIVE");
        if (hasActiveSub) {
            throw new RuntimeException("Member already has an ACTIVE subscription.");
        }

        // 3. Xử lý Logic thời gian (Modern Java Time API)
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(gymPackage.getDurationMonths());

        // 4. Map DTO -> Entity
        Subscription newSubscription = Subscription.builder()
                .member(member)
                .gymPackage(gymPackage)
                .startDate(startDate)
                .endDate(endDate)
                .status("ACTIVE")
                .build();

        // 5. Lưu Database
        Subscription savedSub = subscriptionRepository.save(newSubscription);

        // 6. Map Entity -> Response DTO
        return SubscriptionResponse.builder()
                .id(savedSub.getId())
                .memberId(member.getId())
                .packageId(gymPackage.getId())
                .packageName(gymPackage.getName())
                .startDate(savedSub.getStartDate())
                .endDate(savedSub.getEndDate())
                .status(savedSub.getStatus())
                .build();
    }
}