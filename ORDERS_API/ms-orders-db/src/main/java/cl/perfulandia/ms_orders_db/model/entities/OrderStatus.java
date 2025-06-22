package cl.perfulandia.ms_orders_db.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_statuses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatus {
    
    @Id
    private Long statusCode; 
    
    @Column(name = "status_name", nullable = false)
    private String statusName;
    
    @Column(name = "description")
    private String description;
}
