package cl.perfulandia.ms_pagos_gateway.controller;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentRequest;
import cl.perfulandia.ms_pagos_gateway.model.dto.PaymentInitiationResponse;
import cl.perfulandia.ms_pagos_gateway.service.PaymentGatewayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentGatewayController.class)
class PaymentGatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentGatewayService paymentGatewayService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void initiatePayment_ShouldReturnOk_WhenValidRequestAndSuccessfulPayment() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORDER-123");
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("CLP");
        request.setCustomerId("CUSTOMER-456");

        PaymentInitiationResponse response = new PaymentInitiationResponse();
        response.setSuccess(true);
        response.setPaymentId("PAY-789");
        response.setOrderId("ORDER-123");
        response.setStatus("PENDING");

        when(paymentGatewayService.initiatePayment(any(PaymentRequest.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.paymentId").value("PAY-789"))
                .andExpect(jsonPath("$.orderId").value("ORDER-123"));
    }

    @Test
    void initiatePayment_ShouldReturnBadRequest_WhenValidationFails() throws Exception {
        // Arrange
        PaymentRequest invalidRequest = new PaymentRequest();
        // Missing required fields like orderId, amount, currency

        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confirmPayment_ShouldReturnOk_WhenValidToken() throws Exception {
        // Arrange
        String token = "valid-token-123";
        String confirmationResponse = "Payment confirmed successfully";
        
        when(paymentGatewayService.confirmPayment(token))
                .thenReturn(confirmationResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/confirm")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment confirmed successfully"));
    }
}
