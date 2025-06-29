package cl.perfulandia.ms_transbank_amb.client;

import cl.perfulandia.ms_transbank_amb.model.dto.PaymentRequestDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "transbank-api", url = "${transbank.api.url}")
public interface TransbankFeignClient {    
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    PaymentResponseDTO createTransaction(
        @RequestHeader("Tbk-Api-Key-Id") String commerceCode,
        @RequestHeader("Tbk-Api-Key-Secret") String apiKey,
        @RequestBody PaymentRequestDTO request
    );

    @PutMapping(value = "/transactions/{token}")
    String confirmTransaction(
        @RequestHeader("Tbk-Api-Key-Id") String tbkApiKeyId,
        @RequestHeader("Tbk-Api-Key-Secret") String tbkApiKeySecret,
        @PathVariable("token") String token
    );
}
