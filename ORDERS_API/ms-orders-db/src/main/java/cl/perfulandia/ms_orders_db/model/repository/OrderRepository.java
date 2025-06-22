package cl.perfulandia.ms_orders_db.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.perfulandia.ms_orders_db.model.entities.Order;
import cl.perfulandia.ms_orders_db.model.entities.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long>{
    Optional<Order> findByOrderId(String orderId);
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);    

    @Query("SELECT o FROM Order o WHERE o.status.statusCode = :statusCode")
    List<Order> findByStatusCode(@Param("statusCode") Long statusCode);
    
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status.statusCode = :statusCode")
    List<Order> findByUserIdAndStatusCode(@Param("userId") String userId, @Param("statusCode") Long statusCode);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.orderId = :orderId")
    Optional<Order> findByOrderIdWithItems(@Param("orderId") String orderId);
}
