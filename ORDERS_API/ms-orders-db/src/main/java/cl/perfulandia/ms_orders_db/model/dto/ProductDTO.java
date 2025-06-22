package cl.perfulandia.ms_orders_db.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer availableStock;
    private Boolean active;
    
    public boolean isAvailableForOrder(Integer requestedQuantity) {
        return active != null && active && 
               availableStock != null && 
               availableStock >= requestedQuantity;
    }
    
    public BigDecimal calculateSubtotal(Integer quantity) {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}