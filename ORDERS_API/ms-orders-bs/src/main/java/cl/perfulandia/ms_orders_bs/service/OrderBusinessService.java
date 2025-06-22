package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.clients.OrdersDbClient;
import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.model.dto.OrderFlowResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderBusinessService {

    @Autowired
    OrderFlowManager orderFlowManager;
    
    @Autowired
    OrdersDbClient ordersDbClient;

    public OrderDTO createOrderWithPayment(OrderDTO orderDTO) {
        log.info("Creating order with payment for user: {}", orderDTO.getUserId());
        
        try {
            OrderFlowResult flowResult = orderFlowManager.executeCompleteOrderFlow(orderDTO);
            
            if (!flowResult.isSuccess()) {
                log.error("Order flow failed: {}", flowResult.getSummary());
                throw new RuntimeException("Order processing failed: " + flowResult.getErrorMessage());
            }
            
            log.info("Order flow completed successfully: {}", flowResult.getSummary());
            return flowResult.getFinalOrder();
            
        } catch (Exception e) {
            log.error("Error in order creation process for user: {}", orderDTO.getUserId(), e);
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }    
    
    public OrderDTO getOrderById(String orderId) {
        log.debug("Retrieving order by ID: {}", orderId);
        return ordersDbClient.getOrderById(orderId).getBody();
    }

    public List<OrderDTO> getOrdersByUserId(String userId) {
        log.debug("Retrieving orders for user: {}", userId);
        return ordersDbClient.getOrdersByUserId(userId).getBody();
    }    public OrderDTO updateOrderStatus(String orderId, String status) {
        log.info("Updating order status for order: {} to status: {}", orderId, status);
        Long statusCode = mapStatusToCode(status);
        return ordersDbClient.updateOrderStatus(orderId, statusCode).getBody();
    }

    private Long mapStatusToCode(String status) {
        switch (status) {
            case "CREATED": return 1L;
            case "PAYMENT_INITIATED": return 2L;
            case "PAYMENT_CONFIRMED": return 3L;
            case "PROCESSING": return 4L;
            case "SHIPPED": return 5L;
            case "DELIVERED": return 6L;
            case "CANCELLED": return 7L;
            case "FAILED": return 8L;
            default: 
                log.warn("Unknown status: {}, defaulting to CREATED", status);
                return 1L;
        }
    }
}