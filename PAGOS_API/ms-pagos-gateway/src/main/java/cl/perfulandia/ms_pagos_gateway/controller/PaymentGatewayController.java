package cl.perfulandia.ms_pagos_gateway.controller;

import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentRequest;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_pagos_gateway.service.PaymentGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentGatewayController {
    
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    
    @PostMapping("/initiate")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        PaymentInitiationResponse response = paymentGatewayService.initiatePayment(request);
        return (response.isSuccess()) ?
            ResponseEntity.ok(response) :
            ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestParam String token) {
        Object response = paymentGatewayService.confirmPayment(token);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/return")
    public ResponseEntity<String> handlePaymentReturn(@RequestParam(required = false) String token,
                                                     @RequestParam(required = false) String orderId) {
        return ResponseEntity.ok("Payment return processed. Token: " + token + ", Order: " + orderId);
    }
    
    @GetMapping("/cancel")
    public ResponseEntity<String> handlePaymentCancel(@RequestParam(required = false) String token,
                                                     @RequestParam(required = false) String orderId) {
        return ResponseEntity.ok("Payment cancelled. Token: " + token + ", Order: " + orderId);
    }
}