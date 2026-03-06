package com.fitlife.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriptionCreationRequest {

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotNull(message = "Package ID is required")
    private Long packageId;
}