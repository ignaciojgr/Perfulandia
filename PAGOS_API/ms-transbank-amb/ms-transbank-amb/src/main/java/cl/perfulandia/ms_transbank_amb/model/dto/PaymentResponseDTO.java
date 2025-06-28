package cl.perfulandia.ms_transbank_amb.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentResponseDTO {
    
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
    
}