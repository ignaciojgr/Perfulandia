package cl.perfulandia.ms_orders_bs.clients;

import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-orders-db", url = "${ms-orders-db.url}")
public interface OrdersDbClient {

    @GetMapping("/api/v1/orders/{orderId}")
    ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId);

    @GetMapping("/api/v1/orders/user/{userId}")
    ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId);

    @PostMapping("/api/v1/orders")
    ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO);    @PutMapping("/api/v1/orders/{orderId}")
    ResponseEntity<OrderDTO> updateOrder(@PathVariable String orderId, @RequestBody OrderDTO orderDTO);

    @PutMapping("/api/v1/orders/{orderId}/status/{statusCode}")
    ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId, @PathVariable Long statusCode);

    @DeleteMapping("/api/v1/orders/{orderId}")
    ResponseEntity<Void> deleteOrder(@PathVariable String orderId);
}
