package cl.perfulandia.ms_orders_bs.controller;

import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.service.OrderBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="OrdersBS", description="Orders Business Service")
public class OrderBusinessController {

    private final OrderBusinessService orderBusinessService;

    @Operation(summary = "Create a new order", description="Creates a new order with payment processing")
    @ApiResponses(value = {@ApiResponse(responseCode="201", description = "Order created sucessfully"),
                        @ApiResponse(responseCode="400", description = "Invalid order data"),
                        @ApiResponse(responseCode="500", description = "Internal server error")})
    @PostMapping
    public ResponseEntity<OrderDTO> createOrderWithPayment(@Parameter(description="Order details", required = true) @Valid @RequestBody OrderDTO orderDTO) {
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
