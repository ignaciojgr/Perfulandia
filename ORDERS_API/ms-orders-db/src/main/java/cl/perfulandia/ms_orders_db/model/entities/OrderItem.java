package cl.perfulandia.ms_orders_db.model.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "product_id"}))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price_at_order", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceAtOrder;
    
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;
    
    @PrePersist
    @PreUpdate
    protected void calculateTotalPrice() {
        if (unitPriceAtOrder != null && quantity != null) {
            totalPrice = unitPriceAtOrder.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
