package cl.perfulandia.ms_pagos_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPagosGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPagosGatewayApplication.class, args);
	}

}
