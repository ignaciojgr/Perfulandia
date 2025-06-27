package cl.perfulandia.ms_orders_bff.controller;

import cl.perfulandia.ms_orders_bff.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bff.service.OrderBffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderBffController {

    @Autowired
    private OrderBffService orderBffService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO createdOrder = orderBffService.createOrder(orderDTO);
            return (createdOrder != null) ? 
                new ResponseEntity<>(createdOrder, HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderId) {
        try {
            OrderDTO order = orderBffService.getOrderById(orderId);
            return (order != null) ? 
                new ResponseEntity<>(order, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable String userId) {
        try {
            List<OrderDTO> orders = orderBffService.getUserOrders(userId);
            return (orders != null) ? 
                new ResponseEntity<>(orders, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
