package cl.perfulandia.ms_transbank_amb.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {    
    @JsonProperty("buy_order")
    @NotBlank(message = "Buy order is required")
    private String buyOrder;
    
    @JsonProperty("session_id")
    @NotBlank(message = "Session ID is required")
    private String sessionId;
      
    @JsonProperty("amount")
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Long amount;
    
    @JsonProperty("return_url")
    @NotBlank(message = "Return URL is required")
    private String returnUrl;
}

