package cl.perfulandia.ms_pagos_bs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  
public class TransbankInitiationResponse {
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("buy_order")
    private String buyOrder;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;

    @JsonProperty("payment_token")
    private String paymentToken;
    
    @JsonProperty("redirect_url")
    private String redirectUrl;
    
    @JsonProperty("form_action")
    private String formAction;
    
    
    public String getEffectiveToken() {
        return token != null ? token : paymentToken;
    }
    
    public String getEffectiveUrl() {
        if (url != null) return url;
        if (redirectUrl != null) return redirectUrl;
        return formAction;
    }
}
