package cl.perfulandia.ms_pagos_bs.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentConfirmationResponse {
    private String orderId;
    private String status;
    private String message;
    private String transactionId;
    private String token;
}
