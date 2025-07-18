package cl.perfulandia.ms_orders_bff.model.dto;

import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@ToString
public class OrderDTO {
    private String userId;
    private String returnUrl;
    private List<OrderItemDTO> items; 
    private String customerEmail;
    private String shippingAddress;
    private String paymentMethod;
    private String orderId;           
    private Long totalAmount;   
    private String status;             
    private String paymentId;
    private String paymentStatus;
    private String paymentToken;
    private String paymentUrl;
}
