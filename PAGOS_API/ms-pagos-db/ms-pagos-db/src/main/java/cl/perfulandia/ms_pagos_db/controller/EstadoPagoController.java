package cl.perfulandia.ms_pagos_db.controller;

import org.springframework.web.bind.annotation.RestController;

import cl.perfulandia.ms_pagos_db.model.dto.EstadoPagoDTO;
import cl.perfulandia.ms_pagos_db.service.EstadoPagoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/estados-pago")
@Slf4j
public class EstadoPagoController {

    @Autowired
    EstadoPagoService estadoPagoService;    
    
    @GetMapping("/{id}")
    public ResponseEntity<EstadoPagoDTO> findEstadoPagoById(@PathVariable("id") Long id) {
        log.info("Finding estado pago by id: {}", id);
        EstadoPagoDTO estadoPagoDTO = estadoPagoService.findEstadoPagoById(id);
        return (estadoPagoDTO != null) ? new ResponseEntity<>(estadoPagoDTO, HttpStatus.OK) : 
                                         new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<EstadoPagoDTO> findEstadoPagoByCodigo(@PathVariable("codigo") String codigo) {
        log.info("Finding estado pago by codigo: {}", codigo);
        EstadoPagoDTO estadoPagoDTO = estadoPagoService.findEstadoPagoByCodigo(codigo);
        return (estadoPagoDTO != null) ? new ResponseEntity<>(estadoPagoDTO, HttpStatus.OK) : 
                                         new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("")
    public ResponseEntity<List<EstadoPagoDTO>> findAllEstadosPago() {
        log.info("Finding all estados pago");
        List<EstadoPagoDTO> estadosDTO = estadoPagoService.findAllEstadosPago();
        return (estadosDTO != null && !estadosDTO.isEmpty()) ?
                new ResponseEntity<>(estadosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @GetMapping("/activos")
    public ResponseEntity<List<EstadoPagoDTO>> findActiveEstadosPago() {
        log.info("Finding active estados pago");
        List<EstadoPagoDTO> estadosDTO = estadoPagoService.findActiveEstadosPago();
        return (estadosDTO != null && !estadosDTO.isEmpty()) ?
                new ResponseEntity<>(estadosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @PostMapping("")
    public ResponseEntity<EstadoPagoDTO> createEstadoPago(@RequestBody EstadoPagoDTO estadoPagoDTO) {
        log.info("Creating new estado pago: {}", estadoPagoDTO);
        EstadoPagoDTO createdEstado = estadoPagoService.createEstadoPago(estadoPagoDTO);
        return (createdEstado != null) ? new ResponseEntity<>(createdEstado, HttpStatus.CREATED) :
                                         new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }    
    
    @PutMapping("/{id}")
    public ResponseEntity<EstadoPagoDTO> updateEstadoPago(@PathVariable("id") Long id, @RequestBody EstadoPagoDTO estadoPagoDTO) {
        log.info("Updating estado pago with id: {} with data: {}", id, estadoPagoDTO);
        EstadoPagoDTO updatedEstado = estadoPagoService.updateEstadoPago(id, estadoPagoDTO);
        return (updatedEstado != null) ?
            new ResponseEntity<>(updatedEstado, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EstadoPagoDTO> deleteEstadoPago(@PathVariable("id") Long id) {
        log.info("Deleting estado pago with id: {}", id);
        EstadoPagoDTO deletedEstado = estadoPagoService.deleteEstadoPago(id);
        return (deletedEstado != null) ?
                new ResponseEntity<>(deletedEstado, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}