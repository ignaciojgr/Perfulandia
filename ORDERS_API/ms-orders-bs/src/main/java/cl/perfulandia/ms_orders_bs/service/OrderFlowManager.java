package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.clients.OrdersDbClient;
import cl.perfulandia.ms_orders_bs.clients.PagosApiClient;
import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_orders_bs.model.dto.OrderFlowResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Service
public class OrderFlowManager {

    @Autowired
    OrdersDbClient ordersDbClient;
    
    @Autowired
    PagosApiClient pagosApiClient;
    
    @Autowired
    OrderValidationService validationService;
    
    @Autowired
    PaymentCoordinator paymentCoordinator;

    @Autowired
    PedidosIntegrationService pedidosIntegrationService;    

    public OrderFlowResult executeCompleteOrderFlow(OrderDTO orderRequest) {
        log.info("Starting complete order flow for user: {}", orderRequest.getUserId());
        
        OrderFlowResult result = new OrderFlowResult();
        String orderId = generateOrderId();
        orderRequest.setOrderId(orderId);
        result.setOrderId(orderId);
        result.setUserId(orderRequest.getUserId());
        
        try {
            validationService.validateOrderData(orderRequest);
            log.info("Order validation completed for: {}", orderId);
            ResponseEntity<OrderDTO> response = ordersDbClient.createOrder(orderRequest);
            OrderDTO createdOrder = response.getBody();
            
            if (createdOrder == null) {
                throw new RuntimeException("Failed to create order in database");
            }
            log.info("Order created in database: {}", orderId);
            
            pedidosIntegrationService.createPedidosForOrder(createdOrder);
            log.info("Pedidos created for order: {}", orderId);
            PaymentInitiationResponse paymentResponse = paymentCoordinator.initiatePaymentForOrder(createdOrder);
            
            if (paymentResponse == null) {
                throw new RuntimeException("Payment initiation failed");
            }
            createdOrder.setPaymentId(paymentResponse.getPaymentId());
            createdOrder.setPaymentStatus(paymentResponse.getStatus());
            createdOrder.setPaymentToken(paymentResponse.getToken());
            createdOrder.setPaymentUrl(paymentResponse.getRedirectUrl());
            
            Long statusCode = mapStatusToCode("PAYMENT_INITIATED");
            ordersDbClient.updateOrderStatus(createdOrder.getOrderId(), statusCode);

            OrderDTO updatedOrderRequest = new OrderDTO();
            updatedOrderRequest.setOrderId(createdOrder.getOrderId());
            updatedOrderRequest.setPaymentId(paymentResponse.getPaymentId());
            updatedOrderRequest.setPaymentStatus(paymentResponse.getStatus());
            updatedOrderRequest.setPaymentToken(paymentResponse.getToken());
            updatedOrderRequest.setPaymentUrl(paymentResponse.getRedirectUrl());

            ResponseEntity<OrderDTO> freshOrderResponse = ordersDbClient.getOrderById(createdOrder.getOrderId());
            if (freshOrderResponse.getBody() != null) {
                createdOrder = freshOrderResponse.getBody();
                if (createdOrder.getPaymentId() == null) {
                    createdOrder.setPaymentId(paymentResponse.getPaymentId());
                    createdOrder.setPaymentStatus(paymentResponse.getStatus());
                    createdOrder.setPaymentToken(paymentResponse.getToken());
                    createdOrder.setPaymentUrl(paymentResponse.getRedirectUrl());
                }
            }
            
            result.setFinalOrder(createdOrder);
            result.setPaymentResponse(paymentResponse);
            result.setSuccess(true);
            result.addMessage("Order flow completed successfully");
            
            log.info("Order flow completed successfully for order: {}", orderId);
            return result;
            
        } catch (Exception e) {
            log.error("Order flow failed for order: {}", orderId, e);
            result.setSuccess(false);
            result.addMessage("Order flow failed: " + e.getMessage());
            return result;
        }
    } 

    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
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
