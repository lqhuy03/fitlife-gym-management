package com.fitlife.payment.service;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    String createPaymentUrl(Long subscriptionId, HttpServletRequest request);
    String processPaymentReturn(HttpServletRequest request);
}