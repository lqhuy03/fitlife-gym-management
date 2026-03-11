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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final MemberRepository memberRepository;
    private final GymPackageRepository gymPackageRepository;

    @Transactional
    public SubscriptionResponse createSubscription(SubscriptionCreationRequest request) {

        // 1. Tìm Member và Package
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hội viên"));

        GymPackage gymPackage = gymPackageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói tập"));

        // 2. Validate: Khách có đang sở hữu gói ACTIVE nào không?
        boolean hasActiveSub = subscriptionRepository.existsByMemberAndStatus(member, "ACTIVE");
        if (hasActiveSub) {
            throw new RuntimeException("Hội viên này đang có một gói tập đang hoạt động (ACTIVE).");
        }

        // 3. Map DTO -> Entity (Chỉ tạo Đăng ký ở trạng thái PENDING)
        // CHÚ Ý: Không set startDate và endDate lúc này
        Subscription newSubscription = Subscription.builder()
                .member(member)
                .gymPackage(gymPackage)
                .status("PENDING") // CHUẨN NGHIỆP VỤ LÀ ĐÂY!
                .build();

        // 4. Lưu Database
        Subscription savedSub = subscriptionRepository.save(newSubscription);

        // Chú ý: Ta ĐÃ XÓA logic tạo Payment ở đây. Việc tạo Payment là nhiệm vụ của PaymentService.

        // 5. Map Entity -> Response DTO
        return SubscriptionResponse.builder()
                .id(savedSub.getId())
                .memberId(member.getId())
                .packageId(gymPackage.getId())
                .packageName(gymPackage.getName())
                .status(savedSub.getStatus())
                .build();
    }
}