package cl.perfulandia.ms_orders_db.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.perfulandia.ms_orders_db.model.entities.Order;
import cl.perfulandia.ms_orders_db.model.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    List<OrderItem> findByOrder(Order order);
    void deleteByOrder(Order order);
}
