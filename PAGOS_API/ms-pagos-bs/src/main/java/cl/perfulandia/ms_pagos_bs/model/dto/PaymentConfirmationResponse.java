package cl.perfulandia.ms_pagos_bs.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentConfirmationResponse {
    private String vci;
    private BigDecimal amount;
    private String status;
    private String orderId;
    private String transactionId;
    private String token;
}
