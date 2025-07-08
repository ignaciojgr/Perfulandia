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
            String token = transbankResponse.getTransbankToken();
            String effectiveUrl = transbankResponse.getReturnUrl();
            updatePaymentWithToken(savedPago.getId(), token);

            transbankResponse.setPaymentId(savedPago.getId().toString());
            transbankResponse.setOrderId(savedPago.getOrderId());
            transbankResponse.setTransbankToken(token);
            transbankResponse.setTransbankUrl(effectiveUrl);
            transbankResponse.setAmount(request.getAmount());

            return transbankResponse;

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

            return PaymentConfirmationResponse.builder()
                    .token(token)
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
        updateRequest.setTransbankToken(token);
        pagosDbClient.partialUpdatePago(paymentId, updateRequest);
    }   
}