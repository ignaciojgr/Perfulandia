package cl.perfulandia.ms_pagos_gateway.service;

import cl.perfulandia.ms_pagos_gateway.client.PaymentBusinessClient;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentRequest;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentInitiationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {
    
    @Autowired
    private PaymentBusinessClient paymentBusinessClient;      
    
    public PaymentInitiationResponse initiatePayment(PaymentRequest request) {
        // Ensure required URLs are set before calling business service
        if (request.getReturnUrl() == null || request.getReturnUrl().trim().isEmpty()) {
            request.setReturnUrl("http://localhost:8180/api/v1/payments/return");
        }
        if (request.getCancelUrl() == null || request.getCancelUrl().trim().isEmpty()) {
            request.setCancelUrl("http://localhost:8180/api/v1/payments/cancel");
        }
        
        ResponseEntity<PaymentInitiationResponse> response = paymentBusinessClient.initiatePayment(request);
        PaymentInitiationResponse paymentResponse = response.getBody();
        
        if (paymentResponse == null) {
            return createErrorResponse(request, "No response from payment service");
        }
        
        if (paymentResponse.getOrderId() == null || paymentResponse.getOrderId().trim().isEmpty()) {
            paymentResponse.setOrderId(request.getOrderId());
        }
        
        if (paymentResponse.getPaymentId() != null && 
            ("PENDING".equals(paymentResponse.getStatus()) || "INITIATED".equals(paymentResponse.getStatus()))) {
            paymentResponse.setSuccess(true);
        } else if ("FAILED".equals(paymentResponse.getStatus()) || "ERROR".equals(paymentResponse.getStatus())) {
            paymentResponse.setSuccess(false);
        }
        
        return paymentResponse;
    }
    
    public Object confirmPayment(String token) {
        ResponseEntity<?> response = paymentBusinessClient.confirmPayment(token);
        return response.getBody();
    }
    
    private PaymentInitiationResponse createErrorResponse(PaymentRequest request, String errorMessage) {
        PaymentInitiationResponse errorResponse = new PaymentInitiationResponse();
        errorResponse.setOrderId(request.getOrderId());
        errorResponse.setAmount(request.getAmount());
        errorResponse.setCurrency(request.getCurrency());
        errorResponse.setStatus("FAILED");
        errorResponse.setSuccess(false);
        errorResponse.setMessage(errorMessage);
        return errorResponse;
    }
}
