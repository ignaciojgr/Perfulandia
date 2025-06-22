package cl.perfulandia.ms_pagos_bs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransbankInitiationRequest {
    @JsonProperty("buy_order")
    private String buyOrder;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    @JsonProperty("amount")
    private Long amount;  
    
    @JsonProperty("return_url")
    private String returnUrl;
}
