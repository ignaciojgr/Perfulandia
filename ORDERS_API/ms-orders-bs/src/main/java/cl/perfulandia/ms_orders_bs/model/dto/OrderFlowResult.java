package cl.perfulandia.ms_orders_bs.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderFlowResult {
    private String orderId;
    private String userId;
    private boolean success;
    private List<String> messages;
    private OrderDTO finalOrder;
    private PaymentInitiationResponse paymentResponse;
    
    public OrderFlowResult() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }
    
    public String getSummary() {
        return String.join(", ", messages);
    }
    
    public String getErrorMessage() {
        if (success) {
            return null;
        }
        return messages.isEmpty() ? "Unknown error" : messages.get(messages.size() - 1);
    }
}