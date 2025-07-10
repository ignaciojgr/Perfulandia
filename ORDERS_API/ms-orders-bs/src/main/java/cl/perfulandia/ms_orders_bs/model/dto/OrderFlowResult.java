package cl.perfulandia.ms_orders_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFlowResult {
    private String orderId;
    private String userId;
    private OrderDTO finalOrder;
    private PaymentInitiationResponse paymentResponse;

}