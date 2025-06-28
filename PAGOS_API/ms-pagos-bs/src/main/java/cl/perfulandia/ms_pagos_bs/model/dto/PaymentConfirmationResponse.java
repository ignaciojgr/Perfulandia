package cl.perfulandia.ms_pagos_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentConfirmationResponse {
    private String orderId;
    private String status;
    private String message;
    private String transactionId;
    private String token;
}
