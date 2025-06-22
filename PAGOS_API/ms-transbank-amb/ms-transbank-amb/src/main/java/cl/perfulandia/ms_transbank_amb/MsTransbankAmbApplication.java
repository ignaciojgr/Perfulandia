package cl.perfulandia.ms_transbank_amb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsTransbankAmbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTransbankAmbApplication.class, args);
	}

}
