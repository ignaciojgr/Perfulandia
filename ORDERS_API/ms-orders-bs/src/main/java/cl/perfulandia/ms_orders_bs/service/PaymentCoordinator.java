package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.clients.PagosApiClient;
import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_orders_bs.model.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentCoordinator {

    @Autowired
    PagosApiClient pagosApiClient;

    public PaymentInitiationResponse initiatePaymentForOrder(OrderDTO order) {
          try {
            
            PaymentRequest paymentRequest = createPaymentRequest(order);

            validatePaymentRequest(paymentRequest);

            ResponseEntity<PaymentInitiationResponse> response = pagosApiClient.initiatePayment(paymentRequest);
            PaymentInitiationResponse paymentResponse = response.getBody();

            validatePaymentResponse(paymentResponse);
            return paymentResponse;
            
        } catch (Exception e) {
            throw new RuntimeException("Payment initiation failed");
        }
    }    

    private PaymentRequest createPaymentRequest(OrderDTO order) {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(order.getOrderId());
        request.setUserId(order.getUserId());
        request.setAmount(order.getTotalAmount());
        request.setCurrency(order.getCurrency());
        request.setRedirectUrl(order.getReturnUrl());
        request.setCustomerEmail(order.getCustomerEmail());
        request.setDescription("Payment for order: " + order.getOrderId());
        return request;
    }    

    private void validatePaymentRequest(PaymentRequest request) {
        if (request.getOrderId() == null || request.getOrderId().trim().isEmpty()) {
            throw new RuntimeException("Order ID is required for payment");
        }
        
        if (request.getAmount() == null || request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valid amount is required for payment");
        }
          if (request.getRedirectUrl() == null || request.getRedirectUrl().trim().isEmpty()) {
            throw new RuntimeException("Redirect URL is required for payment");
        }
        
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new RuntimeException("Currency is required for payment");
        }
    }

    private void validatePaymentResponse(PaymentInitiationResponse response) {
        if (response == null) {
            throw new RuntimeException("No response received from payment API");
        }
        
        if (response.getPaymentId() == null || response.getPaymentId().trim().isEmpty()) {
            throw new RuntimeException("Invalid payment ID received");
        }
        
        if (!"PENDING".equals(response.getStatus())) {
            throw new RuntimeException("Payment not initiated properly, status: " + response.getStatus());
        }
    }
}
