package cl.perfulandia.ms_transbank_amb.config;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }   

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new TransbankErrorDecoder();
    }    public static class TransbankErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, feign.Response response) {
            String responseBody = "";
            try {
                if (response.body() != null) {
                    responseBody = " - Response: " + new String(response.body().asInputStream().readAllBytes());
                }
            } catch (Exception e) {
                responseBody = " - Could not read response body";
            }
            
            String message = String.format("Transbank API error: %d %s%s", 
                                          response.status(), response.reason(), responseBody);
            
            switch (response.status()) {
                case 400:
                    return new RuntimeException("Bad Request: " + message);
                case 401:
                    return new RuntimeException("Unauthorized: Check API credentials - " + message);
                case 403:
                    return new RuntimeException("Forbidden: " + message);
                case 404:
                    return new RuntimeException("Not Found: " + message);
                case 422:
                    return new RuntimeException("Unprocessable Entity: Invalid request data - " + message);
                case 500:
                    return new RuntimeException("Transbank Internal Server Error: " + message);
                default:
                    return new RuntimeException("Transbank API Error: " + message);
            }
        }
    }
}
