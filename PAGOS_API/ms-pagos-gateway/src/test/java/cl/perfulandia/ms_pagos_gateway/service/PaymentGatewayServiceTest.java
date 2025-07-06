package cl.perfulandia.ms_pagos_gateway.service;

import cl.perfulandia.ms_pagos_gateway.client.PaymentBusinessClient;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentRequest;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentInitiationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "payment.business.service.url=http://localhost:8181"
})
class PaymentGatewayServiceTest {

    @Mock
    private PaymentBusinessClient paymentBusinessClient;

    @InjectMocks
    private PaymentGatewayService paymentGatewayService;

    private PaymentRequest paymentRequest;
    private PaymentInitiationResponse successResponse;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId("ORDER-123");
        paymentRequest.setAmount(new BigDecimal("100.00"));
        paymentRequest.setCurrency("CLP");
        paymentRequest.setCustomerId("CUSTOMER-456");

        successResponse = new PaymentInitiationResponse();
        successResponse.setPaymentId("PAY-789");
        successResponse.setOrderId("ORDER-123");
        successResponse.setAmount(new BigDecimal("100.00"));
        successResponse.setCurrency("CLP");
        successResponse.setStatus("PENDING");
        successResponse.setToken("token-123");
        successResponse.setRedirectUrl("https://payment.url");
        successResponse.setCreatedAt(LocalDateTime.now());
        successResponse.setSuccess(true);
    }

    @Test
    void initiatePayment_ShouldReturnSuccessResponse_WhenPaymentClientReturnsValidResponse() {
        // Arrange
        when(paymentBusinessClient.initiatePayment(any(PaymentRequest.class)))
                .thenReturn(ResponseEntity.ok(successResponse));

        // Act
        PaymentInitiationResponse result = paymentGatewayService.initiatePayment(paymentRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("ORDER-123", result.getOrderId());
        assertEquals("PAY-789", result.getPaymentId());
        assertEquals("PENDING", result.getStatus());
        verify(paymentBusinessClient, times(1)).initiatePayment(any(PaymentRequest.class));
    }

    @Test
    void initiatePayment_ShouldSetDefaultUrls_WhenReturnAndCancelUrlsAreEmpty() {
        // Arrange
        paymentRequest.setReturnUrl(null);
        paymentRequest.setCancelUrl("");
        
        when(paymentBusinessClient.initiatePayment(any(PaymentRequest.class)))
                .thenReturn(ResponseEntity.ok(successResponse));

        // Act
        paymentGatewayService.initiatePayment(paymentRequest);

        // Assert
        verify(paymentBusinessClient).initiatePayment(argThat(request -> 
            "http://localhost:8180/api/v1/payments/return".equals(request.getReturnUrl()) &&
            "http://localhost:8180/api/v1/payments/cancel".equals(request.getCancelUrl())
        ));
    }

    @Test
    void initiatePayment_ShouldReturnErrorResponse_WhenPaymentClientReturnsNull() {
        // Arrange
        when(paymentBusinessClient.initiatePayment(any(PaymentRequest.class)))
                .thenReturn(ResponseEntity.ok(null));

        // Act
        PaymentInitiationResponse result = paymentGatewayService.initiatePayment(paymentRequest);

        // Assert
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("FAILED", result.getStatus());
        assertEquals("No response from payment service", result.getMessage());
        assertEquals("ORDER-123", result.getOrderId());
        verify(paymentBusinessClient, times(1)).initiatePayment(any(PaymentRequest.class));
    }
}