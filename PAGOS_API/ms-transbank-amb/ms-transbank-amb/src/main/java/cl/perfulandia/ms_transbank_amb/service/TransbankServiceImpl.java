package cl.perfulandia.ms_transbank_amb.service;

import cl.perfulandia.ms_transbank_amb.client.TransbankFeignClient;
import cl.perfulandia.ms_transbank_amb.config.TransbankConfig;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentRequestDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransbankServiceImpl implements TransbankService {
    
    @Autowired
    private TransbankConfig transbankConfig;
    
    @Autowired
    private TransbankFeignClient transbankFeignClient;    
    
    @Override
    public PaymentResponseDTO createTransaction(PaymentRequestDTO request) {
        validateRequest(request);
        try {            
            PaymentResponseDTO transbankResponse = transbankFeignClient.createTransaction(
                transbankConfig.getCommerceCode(),
                transbankConfig.getApiKey(),
                request
            );

            if (transbankResponse != null) {
                return new PaymentResponseDTO(
                    transbankResponse.getToken(), 
                    transbankResponse.getUrl(),
                    request.getBuyOrder(),
                    request.getSessionId(),
                    "SUCCESS"
                );
            } else {
                throw new RuntimeException("No response from Transbank API");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transaction with Transbank: " + e.getMessage());
        }
    }
      private void validateRequest(PaymentRequestDTO request) {
        if (request.getBuyOrder() == null || request.getBuyOrder().trim().isEmpty()) {
            throw new RuntimeException("Buy order is required and cannot be empty");
        }
        if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
            throw new RuntimeException("Session ID is required and cannot be empty");
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Amount is required and must be greater than 0");
        }
        if (request.getReturnUrl() == null || request.getReturnUrl().trim().isEmpty()) {
            throw new RuntimeException("Return URL is required and cannot be empty");
        }
        
        if (request.getBuyOrder().length() > 26) {
            throw new RuntimeException("Buy order cannot exceed 26 characters");
        }
        if (request.getSessionId().length() > 61) {
            throw new RuntimeException("Session ID cannot exceed 61 characters");
        }
        if (request.getAmount() > 999999999) {
            throw new RuntimeException("Amount cannot exceed 999,999,999");
        }
        if (!request.getReturnUrl().startsWith("http://") && !request.getReturnUrl().startsWith("https://")) {
            throw new RuntimeException("Return URL must be a valid HTTP/HTTPS URL");
        }
    }

    @Override
    public String confirmTransaction(String token) {
        try {
            String response = transbankFeignClient.confirmTransaction(
                transbankConfig.getCommerceCode(),
                transbankConfig.getApiKey(),
                token
            );
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to confirm transaction: " + e.getMessage());
        }
    }
}