package cl.perfulandia.ms_pagos_bs.client;

import cl.perfulandia.ms_pagos_bs.model.dto.PagoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ms-pagos-db", url = "${ms-pagos-db.url}")
public interface PagosDbClient {

    @PostMapping("/api/pagos")
    ResponseEntity<PagoDTO> createPago(@RequestBody PagoDTO pagoDTO);

    @GetMapping("/api/pagos/{id}")
    ResponseEntity<PagoDTO> findPagoById(@PathVariable("id") Long id);

    @GetMapping("/api/pagos/usuario/{idUsuario}")
    ResponseEntity<List<PagoDTO>> findPagosByIdUsuario(@PathVariable("idUsuario") String idUsuario);

    @PutMapping("/api/pagos/{id}")
    ResponseEntity<PagoDTO> partialUpdatePago(@PathVariable("id") Long id, @RequestBody PagoDTO pagoDTO);
}
