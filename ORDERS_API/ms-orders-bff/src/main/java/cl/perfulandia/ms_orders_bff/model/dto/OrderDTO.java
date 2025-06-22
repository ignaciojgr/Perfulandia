package cl.perfulandia.ms_orders_bff.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {
    private String userId;
    private String currency;
    private String returnUrl;
    private List<OrderItemDTO> items; 
    private String customerEmail;
    private String shippingAddress;
    private String paymentMethod;
    private String orderId;           
    private BigDecimal totalAmount;   
    private String status;            
    private LocalDateTime createdAt;  
    private LocalDateTime updatedAt;  
    private String paymentId;
    private String paymentStatus;
    private String paymentToken;
    private String paymentUrl;
}
