package cl.perfulandia.ms_pagos_bs.service;

import cl.perfulandia.ms_pagos_bs.client.PagosDbClient;
import cl.perfulandia.ms_pagos_bs.client.TransbankAmbassadorClient;
import cl.perfulandia.ms_pagos_bs.model.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentBusinessService {

    private final PagosDbClient pagosDbClient;
    private final TransbankAmbassadorClient transbankClient;

    public PaymentInitiationResponse initiatePayment(PaymentRequest request) {
        log.info("Initiating payment for order: {}", request.getOrderId());
        
        try {
            PagoDTO pagoDTO = createInitialPaymentRecord(request);
            PagoDTO savedPago = savePagoToDatabase(pagoDTO);

            TransbankInitiationRequest transbankRequest = buildTransbankRequest(request, savedPago);
            TransbankInitiationResponse transbankResponse = initiateTransbankTransaction(transbankRequest);            
            String effectiveToken = transbankResponse.getEffectiveToken();
            String effectiveUrl = transbankResponse.getEffectiveUrl();
            updatePaymentWithToken(savedPago.getId(), effectiveToken);

            log.info("Payment initiated successfully for order: {}, internal payment ID: {}", 
                    request.getOrderId(), savedPago.getId());

            return PaymentInitiationResponse.builder()
                    .paymentId(savedPago.getId().toString())
                    .orderId(request.getOrderId())
                    .transbankToken(effectiveToken)
                    .transbankUrl(effectiveUrl)
                    .token(effectiveToken)  
                    .redirectUrl(effectiveUrl) 
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status("PENDING")
                    .message("Payment initiated successfully")
                    .success(true)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Error initiating payment for order: {}", request.getOrderId(), e);
            throw new PaymentProcessingException("Failed to initiate payment: " + e.getMessage(), e);
        }
    }    
    public PaymentConfirmationResponse confirmPayment(String token) {
        log.info("Confirming payment with token: {}", token);
        
        try {
            
            TransbankInitiationResponse confirmation = transbankClient.confirmTransaction(token).getBody();
            
            if (confirmation == null) {
                throw new PaymentProcessingException("No response from Transbank");
            }

            
            String newStatus = mapTransbankStatusToPaymentStatus(confirmation.getStatus());
            
            log.info("Payment confirmed with status: {}", newStatus);
            
            return PaymentConfirmationResponse.builder()
                    .token(token)
                    .status(newStatus)
                    .message("Payment " + newStatus.toLowerCase())
                    .build();

        } catch (Exception e) {
            log.error("Error confirming payment with token: {}", token, e);
            throw new PaymentProcessingException("Failed to confirm payment: " + e.getMessage(), e);
        }
    }

    private PagoDTO createInitialPaymentRecord(PaymentRequest request) {
        return new PagoDTO(
                null, 
                request.getOrderId(),
                request.getUserId(),
                request.getAmount(),
                "PENDING",
                null 
        );
    }

    private PagoDTO savePagoToDatabase(PagoDTO pagoDTO) {
        log.debug("Saving payment record to database: {}", pagoDTO);
        PagoDTO saved = pagosDbClient.createPago(pagoDTO).getBody();
        if (saved == null) {
            throw new PaymentProcessingException("Failed to save payment record");
        }
        return saved;
    }    
    private TransbankInitiationRequest buildTransbankRequest(PaymentRequest request, PagoDTO pago) {
        String buyOrder = "ORD-" + request.getOrderId();
        String sessionId = "SESS-" + pago.getId();
        
        
        Long roundedAmount = request.getAmount().setScale(0, java.math.RoundingMode.HALF_UP).longValue();
        
        TransbankInitiationRequest transbankRequest = new TransbankInitiationRequest(
                buyOrder,
                sessionId,
                roundedAmount,
                request.getReturnUrl()
        );
        
        log.debug("Built Transbank request - BuyOrder: {}, SessionId: {}, Amount: {} (rounded from {}), ReturnUrl: {}", 
                buyOrder, sessionId, roundedAmount, request.getAmount(), request.getReturnUrl());
        
        return transbankRequest;
    }    
    private TransbankInitiationResponse initiateTransbankTransaction(TransbankInitiationRequest request) {
        log.debug("Initiating Transbank transaction with request: {}", request);
        log.info("Sending to Transbank - BuyOrder: {}, SessionId: {}, Amount: {}, ReturnUrl: {}", 
                request.getBuyOrder(), request.getSessionId(), request.getAmount(), request.getReturnUrl());
        
        TransbankInitiationResponse response = transbankClient.initiateTransaction(request).getBody();
        if (response == null) {
            throw new PaymentProcessingException("No response from Transbank");
        }
          log.info("Received Transbank response - Token: {}, URL: {}, Status: {}, Message: {}", 
                response.getToken(), response.getUrl(), response.getStatus(), response.getMessage());
        
        
        String effectiveToken = response.getEffectiveToken();
        String effectiveUrl = response.getEffectiveUrl();
        
        
        if (effectiveToken == null || effectiveToken.trim().isEmpty()) {
            log.error("Transbank response has null or empty token. Full response: {}", response);
            throw new PaymentProcessingException("Transbank returned invalid token");
        }
        
        if (effectiveUrl == null || effectiveUrl.trim().isEmpty()) {
            log.error("Transbank response has null or empty URL. Full response: {}", response);
            throw new PaymentProcessingException("Transbank returned invalid URL");
        }
        
        log.info("Using effective values - Token: {}, URL: {}", effectiveToken, effectiveUrl);
        
        return response;
    }

    private void updatePaymentWithToken(Long paymentId, String token) {
        log.debug("Updating payment {} with token: {}", paymentId, token);
        PagoDTO updateRequest = new PagoDTO();
        updateRequest.setIdTransaccion(token);
        pagosDbClient.partialUpdatePago(paymentId, updateRequest);
    }

    private String mapTransbankStatusToPaymentStatus(String transbankStatus) {
        
        return switch (transbankStatus.toUpperCase()) {
            case "APPROVED", "AUTHORIZED" -> "COMPLETED";
            case "REJECTED", "FAILED" -> "FAILED";
            default -> "PENDING";
        };
    }    
    public static class PaymentProcessingException extends RuntimeException {
        public PaymentProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
        public PaymentProcessingException(String message) {
            super(message);
        }
    }
}