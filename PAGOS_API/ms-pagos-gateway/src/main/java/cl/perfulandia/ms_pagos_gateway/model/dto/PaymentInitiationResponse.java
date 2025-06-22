package cl.perfulandia.ms_pagos_gateway.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResponse {
    
    private String paymentId;
    private String token;
    private String status;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String redirectUrl;
    private LocalDateTime createdAt;
    private String message;
    private boolean success;
}
