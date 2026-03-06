package com.fitlife.controller;

import com.fitlife.dto.ApiResponse;
import com.fitlife.dto.SubscriptionCreationRequest;
import com.fitlife.dto.SubscriptionResponse;
import com.fitlife.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    // 1. Dùng @Valid để kích hoạt Input Validation
    // 2. Dùng ApiResponse để chuẩn hóa cấu trúc JSON trả về
    public ResponseEntity<ApiResponse<SubscriptionResponse>> createSubscription(
            @Valid @RequestBody SubscriptionCreationRequest request) {

        SubscriptionResponse result = subscriptionService.createSubscription(request);

        ApiResponse<SubscriptionResponse> response = ApiResponse.<SubscriptionResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Subscription registered successfully")
                .data(result)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}