package cl.perfulandia.ms_pagos_bs.controller;

import cl.perfulandia.ms_pagos_bs.model.dto.PaymentConfirmationResponse;
import cl.perfulandia.ms_pagos_bs.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_pagos_bs.model.dto.PaymentRequest;
import cl.perfulandia.ms_pagos_bs.service.PaymentBusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class SimplePaymentController {

    private final PaymentBusinessService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentInitiationResponse> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        log.info("Initiating payment for order: {}", request.getOrderId());
        PaymentInitiationResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentConfirmationResponse> confirmPayment(@RequestParam String token) {
        log.info("Confirming payment with token: {}", token);
        PaymentConfirmationResponse response = paymentService.confirmPayment(token);
        return ResponseEntity.ok(response);
    }
}
