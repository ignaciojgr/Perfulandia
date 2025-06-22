package cl.perfulandia.ms_orders_db.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String orderId;
    private String userId;
    private BigDecimal totalAmount;
    private String currency;
    private String returnUrl;
    private List<OrderItemDTO> items;
    private String customerEmail;
    private String shippingAddress;
    private String paymentMethod;
    private Long statusCode; 
    private String status;
    private String paymentStatus;
    private String paymentToken;
    private String paymentUrl;
    private String paymentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
