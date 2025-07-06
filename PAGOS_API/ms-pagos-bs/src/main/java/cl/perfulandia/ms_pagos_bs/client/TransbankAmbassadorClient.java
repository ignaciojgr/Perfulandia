package cl.perfulandia.ms_pagos_bs.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cl.perfulandia.ms_pagos_bs.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_pagos_bs.model.dto.PaymentRequest;

@FeignClient(name = "ms-transbank-ambassador", url = "${ms-transbank-ambassador.url}")
public interface TransbankAmbassadorClient {    
    @PostMapping("/api/v1/transbank/transactions")
    ResponseEntity<PaymentInitiationResponse> initiateTransaction(@RequestBody PaymentRequest request);

    @PostMapping("/api/v1/transbank/confirm")
    ResponseEntity<PaymentInitiationResponse> confirmTransaction(@RequestBody String token);
}
