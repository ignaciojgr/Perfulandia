package cl.perfulandia.ms_orders_bff.model.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderItemDTO {
    private String productId;  
    private Integer quantity;  
    
}