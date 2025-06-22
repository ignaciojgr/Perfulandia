package cl.perfulandia.ms_pagos_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResponse {
    private String paymentId;
    private String orderId;
    private String transbankToken;
    private String transbankUrl;
    private BigDecimal amount;
    private String status;
    private String message;
    private String currency;
    private String token;
    private String redirectUrl;
    private boolean success;
    private LocalDateTime createdAt;
}