package cl.perfulandia.ms_transbank_amb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransbankConfig {
    
    @Value("${transbank.api.url}")
    private String apiUrl;
    
    @Value("${transbank.api.key}")
    private String apiKey;
    
    @Value("${transbank.commerce.code}")
    private String commerceCode;
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getCommerceCode() {
        return commerceCode;
    }
}
