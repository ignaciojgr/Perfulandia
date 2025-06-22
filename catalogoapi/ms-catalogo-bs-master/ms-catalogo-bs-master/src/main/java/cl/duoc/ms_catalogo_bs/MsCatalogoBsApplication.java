package cl.duoc.ms_catalogo_bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCatalogoBsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCatalogoBsApplication.class, args);
	}

}
