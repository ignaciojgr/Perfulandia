package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.clients.PagosApiClient;
import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_orders_bs.model.dto.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentCoordinator {

    @Autowired
    PagosApiClient pagosApiClient;
    
    @Value("${payment.default.currency:CLP}")
    private String defaultCurrency;
    
    @Value("${payment.default.return-url:http://localhost:3000/payment/return}")
    private String defaultReturnUrl;

    public PaymentInitiationResponse initiatePaymentForOrder(OrderDTO order) {
        log.info("Initiating payment for order: {}", order.getOrderId());
          try {
            
            PaymentRequest paymentRequest = createPaymentRequest(order);
            log.info("Payment request created - OrderId: {}, Amount: {}, Currency: {}, RedirectUrl: {}", 
                    paymentRequest.getOrderId(), 
                    paymentRequest.getAmount(), 
                    paymentRequest.getCurrency(), 
                    paymentRequest.getRedirectUrl());

            validatePaymentRequest(paymentRequest);

            ResponseEntity<PaymentInitiationResponse> response = pagosApiClient.initiatePayment(paymentRequest);
            PaymentInitiationResponse paymentResponse = response.getBody();

            validatePaymentResponse(paymentResponse);
            
            log.info("Payment initiated successfully for order: {}, payment ID: {}", 
                    order.getOrderId(), paymentResponse.getPaymentId());
            
            return paymentResponse;
            
        } catch (Exception e) {
            log.error("Failed to initiate payment for order: {}", order.getOrderId(), e);
            throw new PaymentCoordinationException("Payment initiation failed: " + e.getMessage(), e);
        }
    }    

    private PaymentRequest createPaymentRequest(OrderDTO order) {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(order.getOrderId());
        request.setUserId(order.getUserId());
        request.setAmount(order.getTotalAmount());
          
        request.setCurrency(order.getCurrency() != null && !order.getCurrency().trim().isEmpty() 
            ? order.getCurrency() : defaultCurrency);
          
        request.setRedirectUrl(order.getReturnUrl() != null && !order.getReturnUrl().trim().isEmpty()
            ? order.getReturnUrl() : defaultReturnUrl);
        
        request.setCustomerEmail(order.getCustomerEmail());
        
        
        request.setDescription("Payment for order: " + order.getOrderId());
        
        return request;
    }    

    private void validatePaymentRequest(PaymentRequest request) {
        if (request.getOrderId() == null || request.getOrderId().trim().isEmpty()) {
            throw new PaymentValidationException("Order ID is required for payment");
        }
        
        if (request.getAmount() == null || request.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new PaymentValidationException("Valid amount is required for payment");
        }
          if (request.getRedirectUrl() == null || request.getRedirectUrl().trim().isEmpty()) {
            throw new PaymentValidationException("Redirect URL is required for payment");
        }
        
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new PaymentValidationException("Currency is required for payment");
        }
    }

    private void validatePaymentResponse(PaymentInitiationResponse response) {
        if (response == null) {
            throw new PaymentResponseException("No response received from payment API");
        }
        
        if (response.getPaymentId() == null || response.getPaymentId().trim().isEmpty()) {
            throw new PaymentResponseException("Invalid payment ID received");
        }
        
        if (!"PENDING".equals(response.getStatus())) {
            throw new PaymentResponseException("Payment not initiated properly, status: " + response.getStatus());
        }
    }

    public static class PaymentCoordinationException extends RuntimeException {
        public PaymentCoordinationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class PaymentValidationException extends RuntimeException {
        public PaymentValidationException(String message) {
            super(message);
        }
    }

    public static class PaymentResponseException extends RuntimeException {
        public PaymentResponseException(String message) {
            super(message);
        }
    }
}
