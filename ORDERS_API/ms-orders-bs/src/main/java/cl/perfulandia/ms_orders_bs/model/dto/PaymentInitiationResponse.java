package cl.perfulandia.ms_orders_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentInitiationResponse {
    private String paymentId;        
    private String orderId;
    private String token;            
    private String redirectUrl;      
    private BigDecimal amount;
    private String currency;
    private String status;
    private String message;
    private boolean success;
    private LocalDateTime createdAt;
}
