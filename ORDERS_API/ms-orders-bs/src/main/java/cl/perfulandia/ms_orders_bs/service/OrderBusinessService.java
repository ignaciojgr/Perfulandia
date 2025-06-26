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
        
        try {
            OrderFlowResult flowResult = orderFlowManager.executeCompleteOrderFlow(orderDTO);
            
            if (!flowResult.isSuccess()) {
                throw new RuntimeException("Order processing failed");
            }
            return flowResult.getFinalOrder();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order");
        }
    }    
    
    public OrderDTO getOrderById(String orderId) {
        return ordersDbClient.getOrderById(orderId).getBody();
    }

    public List<OrderDTO> getOrdersByUserId(String userId) {
        return ordersDbClient.getOrdersByUserId(userId).getBody();
    }    
    public OrderDTO updateOrderStatus(String orderId, String status) {
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
                return 1L;
        }
    }
}