package cl.perfulandia.ms_transbank_amb.controller;

import cl.perfulandia.ms_transbank_amb.model.dto.PaymentRequestDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentResponseDTO;
import cl.perfulandia.ms_transbank_amb.service.TransbankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/transbank")
public class TransbankController {
    
    @Autowired
    private TransbankService transbankService;
    
    @PostMapping("/transactions")
    public ResponseEntity<PaymentResponseDTO> createTransaction(@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = transbankService.createTransaction(request);
        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Transbank service is running on port 8183");
    }
    
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmTransaction(@RequestBody String token) {
        String response = transbankService.confirmTransaction(token);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/transactions/{token}")
    public ResponseEntity<PaymentResponseDTO> queryTransaction(@PathVariable String token) {
        var response = transbankService.queryTransaction(token);
        PaymentResponseDTO paymentResponse = new PaymentResponseDTO(
            response.getToken(),
            response.getUrl(),
            null, 
            null, 
            "SUCCESS",
            "Transaction queried successfully"
        );
        return ResponseEntity.ok(paymentResponse);
    }
}
