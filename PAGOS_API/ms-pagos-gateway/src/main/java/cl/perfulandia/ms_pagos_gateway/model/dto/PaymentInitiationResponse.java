package cl.perfulandia.ms_pagos_gateway.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResponse {
    
    private String paymentId;
    private String transbankToken;
    private String status;
    private String orderId;
    private Long amount;
    private String transbankUrl;
    private String returnUrl;
}
