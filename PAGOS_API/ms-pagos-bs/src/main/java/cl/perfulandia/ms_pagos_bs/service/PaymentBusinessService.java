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
            PagoDTO pagoDTO = new PagoDTO();
            PagoDTO savedPago = savePagoToDatabase(pagoDTO);

            PaymentRequest transbankRequest = buildTransbankRequest(request, savedPago);
            PaymentInitiationResponse transbankResponse = initiateTransbankTransaction(transbankRequest);            
            String effectiveToken = transbankResponse.getTransbankToken();
            String effectiveUrl = transbankResponse.getReturnUrl();
            updatePaymentWithToken(savedPago.getId(), effectiveToken);

            transbankResponse.setPaymentId(savedPago.getId().toString());
            transbankResponse.setOrderId(savedPago.getOrderId());
            transbankResponse.setTransbankToken(effectiveToken);
            transbankResponse.setTransbankUrl(effectiveUrl);
            transbankResponse.setAmount();

            return PaymentInitiationResponse.builder()
                    .paymentId(savedPago.getId().toString())
                    .orderId(request.getOrderId())
                    .transbankToken(effectiveToken)
                    .transbankUrl(effectiveUrl)
                    .token(transbankToken)  
                    .redirectUrl(effectiveUrl) 
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .status("PENDING")
                    .message("Payment initiated successfully")
                    .success(true)
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to initiate payment: " + e.getMessage());
        }
    }    
    public PaymentConfirmationResponse confirmPayment(String token) {
        
        try {
            
            PaymentInitiationResponse confirmation = transbankClient.confirmTransaction(token).getBody();
            
            if (confirmation == null) {
                throw new RuntimeException("No response from Transbank");
            }
            
            String newStatus = mapTransbankStatusToPaymentStatus(confirmation.getStatus());
            
            PaymentConfirmationResponse paymentConfirmationResponse = new PaymentConfirmationResponse(token, newStatus);

            return PaymentConfirmationResponse.builder()
                    .token(token)
                    .status(newStatus)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to confirm payment: " + e.getMessage());
        }
    }

    private PagoDTO savePagoToDatabase(PagoDTO pagoDTO) {
        PagoDTO saved = pagosDbClient.createPago(pagoDTO).getBody();
        if (saved == null) {
            throw new RuntimeException("Failed to save payment record");
        }
        return saved;
    }    
    private PaymentRequest buildTransbankRequest(PaymentRequest request, PagoDTO pago) {
        String orderId = "ORD-" + request.getOrderId();
        String sessionId = "SESS-" + pago.getId();
        
        PaymentRequest transbankRequest = new PaymentRequest(
                orderId,
                sessionId,
                request.getAmount(),
                request.getReturnUrl()
        );
        
        return transbankRequest;
    }    
    private PaymentInitiationResponse initiateTransbankTransaction(PaymentRequest request) {
        
        PaymentInitiationResponse response = transbankClient.initiateTransaction(request).getBody();
        if (response == null) {
            throw new RuntimeException("No response from Transbank");
        }
        
        
        String token = response.getTransbankToken();
        String returnUrl = response.getReturnUrl();
        
        
        if (token == null || token.trim().isEmpty()) {
            throw new RuntimeException("Transbank returned invalid token");
        }
        
        if (returnUrl == null || returnUrl.trim().isEmpty()) {
            throw new RuntimeException("Transbank returned invalid URL");
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
}