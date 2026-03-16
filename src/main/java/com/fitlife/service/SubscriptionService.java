package com.fitlife.service;

import com.fitlife.dto.SubscriptionCreationRequest;
import com.fitlife.dto.SubscriptionResponse;
import org.springframework.transaction.annotation.Transactional;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(String username, SubscriptionCreationRequest request);
}
