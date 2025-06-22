package cl.perfulandia.ms_pagos_db.controller;

import org.springframework.web.bind.annotation.RestController;

import cl.perfulandia.ms_pagos_db.model.dto.PagoDTO;
import cl.perfulandia.ms_pagos_db.service.PagoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/pagos")
@Slf4j
@Validated
public class PagoController {

    @Autowired
    PagoService pagoService;    
    
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> findPagoById(@PathVariable("id") Long id) {
        log.info("Finding pago by id: {}", id);
        PagoDTO pagoDTO = pagoService.findPagoById(id);
        return (pagoDTO != null) ? new ResponseEntity<>(pagoDTO, HttpStatus.OK) : 
                                   new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PagoDTO>> findPagosByIdUsuario(@PathVariable("idUsuario") String idUsuario) {
        log.info("Finding pagos by user id: {}", idUsuario);
        List<PagoDTO> pagosDTO = pagoService.findPagosByIdUsuario(idUsuario);
        return (pagosDTO != null && !pagosDTO.isEmpty()) ?
                new ResponseEntity<>(pagosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<List<PagoDTO>> findPagosByIdOrden(@PathVariable("idOrden") String idOrden) {
        log.info("Finding pagos by order id: {}", idOrden);
        List<PagoDTO> pagosDTO = pagoService.findPagosByIdOrden(idOrden);
        return (pagosDTO != null && !pagosDTO.isEmpty()) ?
                new ResponseEntity<>(pagosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @GetMapping("/estado/{idEstado}")
    public ResponseEntity<List<PagoDTO>> findPagosByEstado(@PathVariable("idEstado") String idEstado) {
        log.info("Finding pagos by estado: {}", idEstado);
        List<PagoDTO> pagosDTO = pagoService.findPagosByEstado(idEstado);
        return (pagosDTO != null && !pagosDTO.isEmpty()) ?
                new ResponseEntity<>(pagosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }

    @GetMapping("")
    public ResponseEntity<List<PagoDTO>> findAllPagos() {
        log.info("Finding all pagos");
        List<PagoDTO> pagosDTO = pagoService.findAllPagos();
        return (pagosDTO != null && !pagosDTO.isEmpty()) ?
                new ResponseEntity<>(pagosDTO, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND); 
    }    
    
    @PostMapping("")
    public ResponseEntity<PagoDTO> createPago(@Valid @RequestBody PagoDTO pagoDTO) {
        log.info("Creating new pago: {}", pagoDTO);
        PagoDTO createdPago = pagoService.createPago(pagoDTO);
        return (createdPago != null) ? new ResponseEntity<>(createdPago, HttpStatus.CREATED) :
                                       new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<PagoDTO> deletePago(@PathVariable("id") Long id) {
        log.info("Deleting pago with id: {}", id);
        PagoDTO deletedPago = pagoService.deletePago(id);
        return (deletedPago != null) ?
                new ResponseEntity<>(deletedPago, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }    
    
    @PutMapping("/{id}")
    public ResponseEntity<PagoDTO> updatePago(@PathVariable("id") Long id, @RequestBody PagoDTO pagoDTO) {
        log.info("Updating pago with id: {} with data: {}", id, pagoDTO);
        boolean isPartialUpdate = pagoDTO.getIdOrden() == null || 
                                 pagoDTO.getIdUsuario() == null || 
                                 pagoDTO.getMonto() == null || 
                                 pagoDTO.getIdEstado() == null;
        
        PagoDTO updatedPago;
        if (isPartialUpdate) {
            log.info("Detected partial update, using partialUpdatePago method");
            updatedPago = pagoService.partialUpdatePago(id, pagoDTO);
        } else {
            log.info("Detected full update, using updatePago method");
            updatedPago = pagoService.updatePago(id, pagoDTO);
        }
        
        return (updatedPago != null) ?
            new ResponseEntity<>(updatedPago, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PagoDTO> partialUpdatePago(@PathVariable("id") Long id, @RequestBody PagoDTO pagoDTO) {
        log.info("Partially updating pago with id: {} with data: {}", id, pagoDTO);
        PagoDTO updatedPago = pagoService.partialUpdatePago(id, pagoDTO);
        return (updatedPago != null) ?
            new ResponseEntity<>(updatedPago, HttpStatus.OK) :
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
