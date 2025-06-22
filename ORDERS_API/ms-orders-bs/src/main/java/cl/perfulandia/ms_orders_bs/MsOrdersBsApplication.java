package cl.perfulandia.ms_orders_bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsOrdersBsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrdersBsApplication.class, args);
	}

}
