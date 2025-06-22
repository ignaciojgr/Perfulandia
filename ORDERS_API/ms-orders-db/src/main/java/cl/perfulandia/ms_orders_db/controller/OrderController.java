package cl.perfulandia.ms_orders_db.controller;

import cl.perfulandia.ms_orders_db.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_db.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderService orderService;    
    
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        log.info("Fetching order with ID: {}", orderId);
        OrderDTO order = orderService.findOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable String userId) {
        log.info("Fetching orders for user: {}", userId);
        List<OrderDTO> orders = orderService.findOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }    
    
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        log.info("Creating new order for user: {}", orderDTO.getUserId());
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        if (createdOrder != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        }
        return ResponseEntity.badRequest().build();
    }    
    
    @PutMapping("/{orderId}/status/{statusCode}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable String orderId, @PathVariable Long statusCode) {
        log.info("Updating order {} status to: {}", orderId, statusCode);
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, statusCode);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{statusCode}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable Long statusCode) {
        log.info("Fetching orders with status: {}", statusCode);
        List<OrderDTO> orders = orderService.findOrdersByStatus(statusCode);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/user/{userId}/status/{statusCode}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserIdAndStatus(@PathVariable String userId, @PathVariable Long statusCode) {
        log.info("Fetching orders for user {} with status: {}", userId, statusCode);
        List<OrderDTO> orders = orderService.findOrdersByUserIdAndStatus(userId, statusCode);
        return ResponseEntity.ok(orders);
    }
}
