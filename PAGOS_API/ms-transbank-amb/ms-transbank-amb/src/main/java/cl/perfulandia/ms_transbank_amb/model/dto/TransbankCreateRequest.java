package cl.perfulandia.ms_transbank_amb.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransbankCreateRequest {
    
    @JsonProperty("buy_order")
    private String buyOrder;
    
    @JsonProperty("session_id")
    private String sessionId;
    
    @JsonProperty("amount")
    private int amount;
    
    @JsonProperty("return_url")
    private String returnUrl;
}
