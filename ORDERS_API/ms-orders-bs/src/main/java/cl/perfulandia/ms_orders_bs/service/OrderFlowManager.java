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
        
        OrderFlowResult result = new OrderFlowResult();
        String orderId = generateOrderId();
        orderRequest.setOrderId(orderId);
        result.setOrderId(orderId);
        result.setUserId(orderRequest.getUserId());
        
        try {
            validationService.validateOrderData(orderRequest);
            ResponseEntity<OrderDTO> response = ordersDbClient.createOrder(orderRequest);
            OrderDTO createdOrder = response.getBody();
            
            if (createdOrder == null) {
                throw new RuntimeException("Failed to create order in database");
            }
            
            pedidosIntegrationService.createPedidosForOrder(createdOrder);
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
            
            return result;
            
        } catch (Exception e) {
            result.setSuccess(false);
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
                return 1L; 
        }
    }
}
