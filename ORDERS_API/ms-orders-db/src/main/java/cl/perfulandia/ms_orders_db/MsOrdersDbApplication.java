package cl.perfulandia.ms_orders_db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsOrdersDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrdersDbApplication.class, args);
	}

}
