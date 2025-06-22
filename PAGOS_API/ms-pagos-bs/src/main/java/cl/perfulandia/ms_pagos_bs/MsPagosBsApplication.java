package cl.perfulandia.ms_pagos_bs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPagosBsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPagosBsApplication.class, args);
    }
}
