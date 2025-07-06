package cl.perfulandia.ms_pagos_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResponse {
    private String paymentId;
    private String orderId;
    private String transbankToken;
    private String transbankUrl;
    private Long amount;
    private String status;
    private String returnUrl;
}