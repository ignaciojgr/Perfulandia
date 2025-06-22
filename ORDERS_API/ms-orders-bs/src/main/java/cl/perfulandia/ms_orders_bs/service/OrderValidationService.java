package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Service
public class OrderValidationService {

    @Autowired
    CatalogEnrichmentService catalogEnrichmentService;    

    public void validateOrderData(OrderDTO order) {
        log.debug("Validating order data for: {}", order.getOrderId());
        
        validateBasicOrderInfo(order);
        validateCustomerInfo(order);
        validatePaymentInfo(order);
        validateOrderItems(order);

        validateCatalogItems(order);

        calculateAndSetTotalAmount(order);

        validateCalculatedAmount(order);
        
        log.debug("Order validation completed for: {}", order.getOrderId());
    }
    
    private void validateCatalogItems(OrderDTO order) {
        log.info("Validating order items against catalog for order: {}", order.getOrderId());
        
        boolean isValid = catalogEnrichmentService.validateOrderItems(order.getItems());
        if (!isValid) {
            throw new OrderValidationException("One or more items are not available or invalid");
        }

        catalogEnrichmentService.enrichOrderItems(order.getItems());
        
        log.info("Catalog validation and enrichment completed for order: {}", order.getOrderId());
    }

    private void validateBasicOrderInfo(OrderDTO order) {
        if (order.getUserId() == null || order.getUserId().trim().isEmpty()) {
            throw new OrderValidationException("User ID is required");
        }
        
        if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            throw new OrderValidationException("Order ID is required");
        }
    } 

    private void calculateAndSetTotalAmount(OrderDTO order) {
        log.info("Calculating total amount from catalog-enriched items for order: {}", order.getOrderId());
        
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        
        if (order.getItems() != null) {
            for (var item : order.getItems()) {
                if (item.getUnitPrice() != null && item.getQuantity() != null) {
                    BigDecimal itemTotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                    calculatedTotal = calculatedTotal.add(itemTotal);
                    
                    item.setTotalPrice(itemTotal);
                    
                    log.debug("Item {} - Unit Price: {}, Quantity: {}, Total: {}", 
                             item.getProductId(), item.getUnitPrice(), item.getQuantity(), itemTotal);
                }
            }
        }

        order.setTotalAmount(calculatedTotal);

        if (order.getCurrency() == null || order.getCurrency().trim().isEmpty()) {
            order.setCurrency("CLP");
        }
        
        log.info("Calculated total amount for order {}: {} {}", 
                order.getOrderId(), calculatedTotal, order.getCurrency());
    }

    private void validateCalculatedAmount(OrderDTO order) {
        if (order.getTotalAmount() == null) {
            throw new OrderValidationException("Failed to calculate total amount from items");
        }
        
        if (order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderValidationException("Calculated total amount must be greater than zero");
        }
        
        if (order.getTotalAmount().compareTo(new BigDecimal("1000000")) > 0) {
            throw new OrderValidationException("Calculated total amount exceeds maximum allowed limit");
        }
    }

    private void validateCustomerInfo(OrderDTO order) {
        if (order.getCustomerEmail() == null || order.getCustomerEmail().trim().isEmpty()) {
            throw new OrderValidationException("Customer email is required");
        }
        
        if (!isValidEmail(order.getCustomerEmail())) {
            throw new OrderValidationException("Invalid email format");
        }
        
        if (order.getShippingAddress() == null || order.getShippingAddress().trim().isEmpty()) {
            throw new OrderValidationException("Shipping address is required");
        }
    }

    private void validatePaymentInfo(OrderDTO order) {
        if (order.getReturnUrl() == null || order.getReturnUrl().trim().isEmpty()) {
            throw new OrderValidationException("Return URL is required for payment processing");
        }
        
        if (!isValidUrl(order.getReturnUrl())) {
            throw new OrderValidationException("Invalid return URL format");
        }
        
        if (order.getCurrency() == null || order.getCurrency().trim().isEmpty()) {
            order.setCurrency("CLP"); 
        }
    }    
    private void validateOrderItems(OrderDTO order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderValidationException("Order must contain at least one item");
        }
        
        for (var item : order.getItems()) {
            if (item.getProductId() == null || item.getProductId().trim().isEmpty()) {
                throw new OrderValidationException("Product ID is required for all items");
            }
            
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new OrderValidationException("Valid quantity is required for all items");
            }
        }
        
        log.debug("Basic order items validation completed for order: {}", order.getOrderId());
    }    

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    public static class OrderValidationException extends RuntimeException {
        public OrderValidationException(String message) {
            super(message);
        }
    }
}
