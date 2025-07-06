package cl.perfulandia.ms_pagos_bs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @JsonProperty("order_id")
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    @JsonProperty("amount")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;
    
    @JsonProperty("return_url")
    @NotBlank(message = "Return URL is required")
    private String returnUrl;
}