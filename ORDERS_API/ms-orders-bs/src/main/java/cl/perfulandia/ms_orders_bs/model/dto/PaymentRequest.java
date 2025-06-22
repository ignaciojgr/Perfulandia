package cl.perfulandia.ms_orders_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    private String userId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    private String currency;
    
    @NotBlank(message = "Redirect URL is required")
    private String redirectUrl;
    
    private String customerEmail;
    private String description;    

    public PaymentRequest(String orderId, BigDecimal amount, String redirectUrl) {
        this.orderId = orderId;
        this.amount = amount;
        this.redirectUrl = redirectUrl;
        this.currency = "CLP"; 
    }    
    public boolean isValid() {
        return orderId != null && !orderId.trim().isEmpty() &&
               amount != null && amount.compareTo(BigDecimal.ZERO) > 0 &&
               redirectUrl != null && !redirectUrl.trim().isEmpty();
    }

    public String getDescriptionOrDefault() {
        if (description != null && !description.trim().isEmpty()) {
            return description;
        }
        return "Payment for order: " + orderId;
    }
}