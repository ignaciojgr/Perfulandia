package cl.perfulandia.ms_transbank_amb.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
            .servers(List.of(new Server().url("http://localhost:" + serverPort)))
            .info(new Info()
                .title("Transbank Ambassador")
                .description("Transbank Ambassador for payments requests to Transbank API")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Perfulandia Development Team")
                    .email("dev@perfulandia.cl"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("http://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
