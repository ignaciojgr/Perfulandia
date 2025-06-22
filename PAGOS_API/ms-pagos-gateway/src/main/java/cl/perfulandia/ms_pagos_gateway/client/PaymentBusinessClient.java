package cl.perfulandia.ms_pagos_gateway.client;

import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentRequest;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentInitiationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-business", url = "${payment.business.service.url}")
public interface PaymentBusinessClient {
    
    @PostMapping("/api/v1/payments/initiate")
    ResponseEntity<PaymentInitiationResponse> initiatePayment(@RequestBody PaymentRequest request);
    
    @PostMapping("/api/v1/payments/confirm")
    ResponseEntity<?> confirmPayment(@RequestParam String token);
}
