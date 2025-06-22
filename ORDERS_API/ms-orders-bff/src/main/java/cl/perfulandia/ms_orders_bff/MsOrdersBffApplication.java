package cl.perfulandia.ms_orders_bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsOrdersBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrdersBffApplication.class, args);
	}

}
