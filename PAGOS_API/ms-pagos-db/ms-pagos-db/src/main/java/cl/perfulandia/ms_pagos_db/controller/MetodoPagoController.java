package cl.perfulandia.ms_pagos_db.controller;

import org.springframework.web.bind.annotation.RestController;

import cl.perfulandia.ms_pagos_db.model.dto.MetodoPagoDTO;
import cl.perfulandia.ms_pagos_db.service.MetodoPagoService;

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
@RequestMapping("/api/metodos-pago")
@Slf4j
public class MetodoPagoController {

    @Autowired
    MetodoPagoService metodoPagoService;    
    
    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> findMetodoPagoById(@PathVariable("id") Long id) {
        log.info("Finding metodo pago by id: {}", id);
        MetodoPagoDTO metodoPagoDTO = metodoPagoService.findMetodoPagoById(id);
        return (metodoPagoDTO != null) ? new ResponseEntity<>(metodoPagoDTO, HttpStatus.OK) : 
                                         new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("")
    public ResponseEntity<List<MetodoPagoDTO>> findAllMetodosPago() {
        log.info("Finding all metodos pago");
        List<MetodoPagoDTO> metodosDTO = metodoPagoService.findAllMetodosPago();
        return (metodosDTO != null && !metodosDTO.isEmpty()) ?
                new ResponseEntity<>(metodosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @GetMapping("/activos")
    public ResponseEntity<List<MetodoPagoDTO>> findActiveMetodosPago() {
        log.info("Finding active metodos pago");
        List<MetodoPagoDTO> metodosDTO = metodoPagoService.findActiveMetodosPago();
        return (metodosDTO != null && !metodosDTO.isEmpty()) ?
                new ResponseEntity<>(metodosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @PostMapping("")
    public ResponseEntity<MetodoPagoDTO> createMetodoPago(@RequestBody MetodoPagoDTO metodoPagoDTO) {
        log.info("Creating new metodo pago: {}", metodoPagoDTO);
        MetodoPagoDTO createdMetodo = metodoPagoService.createMetodoPago(metodoPagoDTO);
        return (createdMetodo != null) ? new ResponseEntity<>(createdMetodo, HttpStatus.CREATED) :
                                         new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }    
    
    @PutMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> updateMetodoPago(@PathVariable("id") Long id, @RequestBody MetodoPagoDTO metodoPagoDTO) {
        log.info("Updating metodo pago with id: {} with data: {}", id, metodoPagoDTO);
        MetodoPagoDTO updatedMetodo = metodoPagoService.updateMetodoPago(id, metodoPagoDTO);
        return (updatedMetodo != null) ?
            new ResponseEntity<>(updatedMetodo, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MetodoPagoDTO> deleteMetodoPago(@PathVariable("id") Long id) {
        log.info("Deleting metodo pago with id: {}", id);
        MetodoPagoDTO deletedMetodo = metodoPagoService.deleteMetodoPago(id);
        return (deletedMetodo != null) ?
                new ResponseEntity<>(deletedMetodo, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
