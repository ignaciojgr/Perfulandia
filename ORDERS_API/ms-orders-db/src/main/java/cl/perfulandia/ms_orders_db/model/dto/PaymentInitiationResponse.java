package cl.perfulandia.ms_orders_db.model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInitiationResponse {
    private Long paymentId;
    private String orderId;
    private String transbankToken;
    private String transbankUrl;
    private BigDecimal amount;
    private String status;
    private String message;
}
