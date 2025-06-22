package cl.perfulandia.ms_orders_bff.clients;

import cl.perfulandia.ms_orders_bff.config.FeignConfig;
import cl.perfulandia.ms_orders_bff.model.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-orders-bs", url = "${ms-orders-bs.url}", configuration = FeignConfig.class)
public interface OrdersBusinessClient {

    @PostMapping("/api/v1/orders")
    ResponseEntity<OrderDTO> createOrderWithPayment(@RequestBody OrderDTO orderDTO);

    @GetMapping("/api/v1/orders/{orderId}")
    ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId);

    @GetMapping("/api/v1/orders/user/{userId}")
    ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId);
}
