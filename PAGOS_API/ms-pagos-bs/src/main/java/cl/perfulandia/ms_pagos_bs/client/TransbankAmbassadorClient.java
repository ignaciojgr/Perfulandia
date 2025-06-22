package cl.perfulandia.ms_pagos_bs.client;

import cl.perfulandia.ms_pagos_bs.model.dto.TransbankInitiationRequest;
import cl.perfulandia.ms_pagos_bs.model.dto.TransbankInitiationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-transbank-ambassador", url = "${ms-transbank-ambassador.url}")
public interface TransbankAmbassadorClient {    
    @PostMapping("/api/v1/transbank/transactions")
    ResponseEntity<TransbankInitiationResponse> initiateTransaction(@RequestBody TransbankInitiationRequest request);

    @PostMapping("/api/v1/transbank/confirm")
    ResponseEntity<TransbankInitiationResponse> confirmTransaction(@RequestBody String token);
}
