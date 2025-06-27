package cl.perfulandia.ms_transbank_amb.service;

import cl.perfulandia.ms_transbank_amb.client.TransbankFeignClient;
import cl.perfulandia.ms_transbank_amb.config.TransbankConfig;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentRequestDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.PaymentResponseDTO;
import cl.perfulandia.ms_transbank_amb.model.dto.TransbankCreateRequest;
import cl.perfulandia.ms_transbank_amb.model.dto.TransbankCreateResponse;
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
        log.info("Creating transaction - BuyOrder: {}, SessionId: {}, Amount: {}, ReturnUrl: {}", 
                 request.getBuyOrder(), request.getSessionId(), request.getAmount(), request.getReturnUrl());
        
        validateRequest(request);
        
        TransbankCreateRequest transbankRequest = new TransbankCreateRequest(
            request.getBuyOrder(),
            request.getSessionId(),
            request.getAmount().intValue(),
            request.getReturnUrl()
        );
        
        log.info("Sending to Transbank: {}", transbankRequest);
        log.info("Using credentials - Commerce Code: {}, API URL: {}", 
                transbankConfig.getCommerceCode(), transbankConfig.getApiUrl());
        
        try {
            log.info("Making request to Transbank API via Feign...");            
            TransbankCreateResponse transbankResponse = transbankFeignClient.createTransaction(
                transbankConfig.getCommerceCode(),
                transbankConfig.getApiKey(),
                transbankRequest
            );
            
            log.info("Transbank API response: {}", transbankResponse);
            
            if (transbankResponse != null) {
                log.info("Transaction created successfully. Token: {}, URL: {}", 
                        transbankResponse.getToken(), transbankResponse.getUrl());
                
                return new PaymentResponseDTO(
                    transbankResponse.getToken(), 
                    transbankResponse.getUrl(),
                    request.getBuyOrder(),
                    request.getSessionId(),
                    "SUCCESS",
                    "Transaction initiated successfully"
                );
            } else {
                log.error("Received null response from Transbank API");
                throw new RuntimeException("No response from Transbank API");
            }
            
        } catch (Exception e) {
            log.error("Error calling Transbank API: {}", e.getMessage(), e);
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
        log.info("Confirming transaction with token: {}", token);
        
        try {
            String response = transbankFeignClient.confirmTransaction(
                transbankConfig.getCommerceCode(),
                transbankConfig.getApiKey(),
                token
            );
            
            log.info("Transaction confirmed: {}", response);
            return response;
            
        } catch (Exception e) {
            log.error("Error confirming transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to confirm transaction: " + e.getMessage());
        }
    }
    
    @Override
    public TransbankCreateResponse queryTransaction(String token) {
        log.info("Querying transaction with token: {}", token);
        
        try {
            TransbankCreateResponse response = transbankFeignClient.queryTransaction(
                transbankConfig.getCommerceCode(),
                transbankConfig.getApiKey(),
                token
            );
            
            log.info("Transaction query result: {}", response);
            return response;
            
        } catch (Exception e) {
            log.error("Error querying transaction: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to query transaction: " + e.getMessage());
        }
    }
}