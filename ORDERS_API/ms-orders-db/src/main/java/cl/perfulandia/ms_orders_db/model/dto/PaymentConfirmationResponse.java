package cl.perfulandia.ms_orders_db.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmationResponse {
    private String token;
    private String status;
    private String message;
}
