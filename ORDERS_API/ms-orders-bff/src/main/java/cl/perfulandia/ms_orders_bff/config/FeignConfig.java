package cl.perfulandia.ms_orders_bff.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; 
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(
            15000, TimeUnit.MILLISECONDS, 
            60000, TimeUnit.MILLISECONDS, 
            true 
        );
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }
}
