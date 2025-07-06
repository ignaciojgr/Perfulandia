package cl.perfulandia.ms_pagos_gateway.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void paymentRequest_ShouldPassValidation_WhenAllRequiredFieldsAreValid() {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORDER-123");
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("CLP");
        request.setCustomerId("CUSTOMER-456");

        // Act
        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void paymentRequest_ShouldFailValidation_WhenOrderIdIsBlank() {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(""); // blank order ID
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("CLP");

        // Act
        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<PaymentRequest> violation = violations.iterator().next();
        assertEquals("Order ID is required", violation.getMessage());
    }

    @Test
    void paymentRequest_ShouldFailValidation_WhenAmountIsNegative() {
        // Arrange
        PaymentRequest request = new PaymentRequest();
        request.setOrderId("ORDER-123");
        request.setAmount(new BigDecimal("-50.00")); // negative amount
        request.setCurrency("CLP");

        // Act
        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<PaymentRequest> violation = violations.iterator().next();
        assertEquals("Amount must be positive", violation.getMessage());
    }
}
