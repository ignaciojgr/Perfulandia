package cl.perfulandia.ms_pagos_bs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @JsonProperty("orderId")
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @JsonProperty("userId")
    private String userId;
    
    @JsonProperty("amount")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("returnUrl")
    @NotBlank(message = "Return URL is required")
    private String returnUrl;
    
    @JsonProperty("customerEmail")
    private String customerEmail;
    
    @JsonProperty("description")
    private String description;

    
    public PaymentRequest(String orderId, BigDecimal amount, String returnUrl) {
        this.orderId = orderId;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.currency = "CLP";
    }

    
    public boolean isValid() {
        return orderId != null && !orderId.trim().isEmpty() &&
               amount != null && amount.compareTo(BigDecimal.ZERO) > 0 &&
               returnUrl != null && !returnUrl.trim().isEmpty();
    }
}