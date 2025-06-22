package cl.perfulandia.ms_orders_bs.clients;

import cl.perfulandia.ms_orders_bs.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_orders_bs.model.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-pagos-gateway", url = "${ms-pagos-gateway.url}")
public interface PagosApiClient {

    @PostMapping("/api/v1/payments/initiate")
    ResponseEntity<PaymentInitiationResponse> initiatePayment(@RequestBody PaymentRequest paymentRequest);

    @PostMapping("/api/v1/payments/confirm")
    ResponseEntity<?> confirmPayment(@RequestParam String token);
}
