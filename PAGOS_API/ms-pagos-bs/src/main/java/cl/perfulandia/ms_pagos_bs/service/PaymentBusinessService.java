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
        
        try {
            PagoDTO pagoDTO = createInitialPaymentRecord(request);
            PagoDTO savedPago = savePagoToDatabase(pagoDTO);

            TransbankInitiationRequest transbankRequest = buildTransbankRequest(request, savedPago);
            TransbankInitiationResponse transbankResponse = initiateTransbankTransaction(transbankRequest);            
            String effectiveToken = transbankResponse.getEffectiveToken();
            String effectiveUrl = transbankResponse.getEffectiveUrl();
            updatePaymentWithToken(savedPago.getId(), effectiveToken);

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
            throw new PaymentProcessingException("Failed to initiate payment: " + e.getMessage(), e);
        }
    }    
    public PaymentConfirmationResponse confirmPayment(String token) {
        
        try {
            
            TransbankInitiationResponse confirmation = transbankClient.confirmTransaction(token).getBody();
            
            if (confirmation == null) {
                throw new PaymentProcessingException("No response from Transbank");
            }
            
            String newStatus = mapTransbankStatusToPaymentStatus(confirmation.getStatus());
            
            return PaymentConfirmationResponse.builder()
                    .token(token)
                    .status(newStatus)
                    .message("Payment " + newStatus.toLowerCase())
                    .build();

        } catch (Exception e) {
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
        
        return transbankRequest;
    }    
    private TransbankInitiationResponse initiateTransbankTransaction(TransbankInitiationRequest request) {
        
        TransbankInitiationResponse response = transbankClient.initiateTransaction(request).getBody();
        if (response == null) {
            throw new PaymentProcessingException("No response from Transbank");
        }
        
        
        String effectiveToken = response.getEffectiveToken();
        String effectiveUrl = response.getEffectiveUrl();
        
        
        if (effectiveToken == null || effectiveToken.trim().isEmpty()) {
            log.error("Transbank response has null or empty token. Full response: {}", response);
            throw new PaymentProcessingException("Transbank returned invalid token");
        }
        
        if (effectiveUrl == null || effectiveUrl.trim().isEmpty()) {
            throw new PaymentProcessingException("Transbank returned invalid URL");
        }
        
        return response;
    }

    private void updatePaymentWithToken(Long paymentId, String token) {
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