package cl.perfulandia.ms_pagos_gateway.service;

import cl.perfulandia.ms_pagos_gateway.client.PaymentBusinessClient;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentInitiationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentGatewayService {
    
    @Autowired
    private PaymentBusinessClient paymentBusinessClient;      
    
    public PaymentInitiationResponse initiatePayment(PaymentRequest request) {
        
    log.info("Initiating payment for order: {}", request.getOrderId());

        if(!isValidPaymentRequest(request)){
            log.error("Invalid payment request for order {}", request.getOrderId());
            return createErrorResponse(request);
        }
        
        if (request.getReturnUrl() == null || request.getReturnUrl().trim().isEmpty()) {
            log.error("The return url is missing");
            return createErrorResponse(request);
        }
        
        ResponseEntity<PaymentInitiationResponse> response = paymentBusinessClient.initiatePayment(request);
        PaymentInitiationResponse paymentResponse = response.getBody();
        
        if (paymentResponse == null) {
            return createErrorResponse(request);
        }
        
        return paymentResponse;
    }
    
    public Object confirmPayment(String token) {
        ResponseEntity<?> response = paymentBusinessClient.confirmPayment(token);
        return response.getBody();
    }

    private boolean isValidPaymentRequest(PaymentRequest request) {
        if (request == null) {
            log.error("Payment request is null");
            return false;
        }
        
        if (request.getReturnUrl() == null || request.getReturnUrl().trim().isEmpty()) {
            log.error("The return url is missing");
            return false;
        }

        if (request.getOrderId() == null || request.getOrderId().trim().isEmpty()) {
            log.error("Order ID is missing in payment request");
            return false;
        }
        
        if (request.getAmount() == null || request.getAmount() == 0 || request.getAmount() < 0) {
            log.error("Invalid amount in payment request for order: {}", request.getOrderId());
            return false;
        }

        if (request.getCustomerId() == null) {
            log.error("Customer ID is missing in payment request");
            return false;
        }
        
        return true;
    }
    
    private PaymentInitiationResponse createErrorResponse(PaymentRequest request) {
        PaymentInitiationResponse errorResponse = new PaymentInitiationResponse();
        errorResponse.setOrderId(request.getOrderId());
        errorResponse.setAmount(request.getAmount());
        errorResponse.setStatus("FAILED");
        return errorResponse;
    }
}
