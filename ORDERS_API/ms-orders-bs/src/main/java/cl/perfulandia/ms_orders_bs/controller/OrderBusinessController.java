package cl.perfulandia.ms_orders_bs.controller;

import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.service.OrderBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderBusinessController {

    private final OrderBusinessService orderBusinessService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrderWithPayment(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderBusinessService.createOrderWithPayment(orderDTO);
        return ResponseEntity.ok(createdOrder);
    }    
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        OrderDTO order = orderBusinessService.getOrderById(orderId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId) {
        List<OrderDTO> orders = orderBusinessService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
