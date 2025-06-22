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
public class OrderBffService {    @Autowired
    private OrdersBusinessClient ordersBusinessClient;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info("BFF Service: Creating order for user: {}", orderDTO.getUserId());
        log.debug("BFF Service: Order details: {}", orderDTO);
        
        try {
            log.info("BFF Service: Calling orders-bs at: {}", "http://localhost:8081/api/v1/orders");
            ResponseEntity<OrderDTO> response = ordersBusinessClient.createOrderWithPayment(orderDTO);
            OrderDTO createdOrder = response.getBody();
            log.info("BFF Service: Order created successfully with ID: {}", createdOrder != null ? createdOrder.getOrderId() : "null");
            return createdOrder;
        } catch (feign.RetryableException e) {
            log.error("BFF Service: Timeout/Connection error creating order for user: {} - {}", orderDTO.getUserId(), e.getMessage());
            log.error("BFF Service: Check if orders-bs service is running on port 8081");
            throw new RuntimeException("Orders service is unavailable. Please try again later.", e);
        } catch (feign.FeignException.NotFound e) {
            log.error("BFF Service: Orders-BS endpoint not found for user: {}", orderDTO.getUserId());
            throw new RuntimeException("Orders service endpoint not found", e);
        } catch (feign.FeignException e) {
            log.error("BFF Service: Feign client error creating order for user: {} - Status: {}, Message: {}", 
                     orderDTO.getUserId(), e.status(), e.getMessage());
            throw new RuntimeException("Error communicating with orders service", e);
        } catch (Exception e) {
            log.error("BFF Service: Unexpected error creating order for user: {}", orderDTO.getUserId(), e);
            throw new RuntimeException("Failed to create order", e);
        }
    }

    public OrderDTO getOrderById(String orderId) {
        log.info("BFF Service: Fetching order with ID: {}", orderId);
        try {
            ResponseEntity<OrderDTO> response = ordersBusinessClient.getOrderById(orderId);
            OrderDTO order = response.getBody();
            log.info("BFF Service: Order fetched successfully: {}", orderId);
            return order;
        } catch (Exception e) {
            log.error("BFF Service: Error fetching order with ID: {}", orderId, e);
            throw new RuntimeException("Failed to fetch order", e);
        }
    }

    public List<OrderDTO> getUserOrders(String userId) {
        log.info("BFF Service: Fetching orders for user: {}", userId);
        try {
            ResponseEntity<List<OrderDTO>> response = ordersBusinessClient.getOrdersByUserId(userId);
            List<OrderDTO> orders = response.getBody();
            log.info("BFF Service: Fetched {} orders for user: {}", orders != null ? orders.size() : 0, userId);
            return orders;
        } catch (Exception e) {
            log.error("BFF Service: Error fetching orders for user: {}", userId, e);
            throw new RuntimeException("Failed to fetch user orders", e);
        }
    }
}
