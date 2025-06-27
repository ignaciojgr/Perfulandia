package cl.perfulandia.ms_orders_bff.service;

import cl.perfulandia.ms_orders_bff.clients.OrdersBusinessClient;
import cl.perfulandia.ms_orders_bff.model.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderBffService {    
    
    @Autowired
    private OrdersBusinessClient ordersBusinessClient;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        
        try {
            ResponseEntity<OrderDTO> response = ordersBusinessClient.createOrderWithPayment(orderDTO);
            OrderDTO createdOrder = response.getBody();
            return createdOrder;
        } catch (feign.RetryableException e) {
            throw new RuntimeException("Orders service is unavailable.", e);
        } catch (feign.FeignException.NotFound e) {
            throw new RuntimeException("Orders service endpoint not found", e);
        } catch (feign.FeignException e) {
            throw new RuntimeException("Error communicating with orders service", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    public OrderDTO getOrderById(String orderId) {
        try {
            ResponseEntity<OrderDTO> response = ordersBusinessClient.getOrderById(orderId);
            OrderDTO order = response.getBody();
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch order", e);
        }
    }

    public List<OrderDTO> getUserOrders(String userId) {
        try {
            ResponseEntity<List<OrderDTO>> response = ordersBusinessClient.getOrdersByUserId(userId);
            List<OrderDTO> orders = response.getBody();
            return orders;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user orders", e);
        }
    }
}
