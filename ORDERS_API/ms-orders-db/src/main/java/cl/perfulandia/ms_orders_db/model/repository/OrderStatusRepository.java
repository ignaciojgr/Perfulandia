package cl.perfulandia.ms_orders_db.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.perfulandia.ms_orders_db.model.entities.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}