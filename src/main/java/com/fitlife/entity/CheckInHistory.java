package com.fitlife.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_in_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ai là người quẹt thẻ?
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Quẹt thẻ lúc mấy giờ, phút, giây?
    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    // Lưu lại trạng thái quẹt thẻ (Thành công do gói còn hạn, hay Thất bại do hết hạn/chưa mua gói?)
    @Column(name = "status", nullable = false, length = 50)
    private String status; // VD: "SUCCESS", "FAILED_NO_ACTIVE_PACKAGE"
}