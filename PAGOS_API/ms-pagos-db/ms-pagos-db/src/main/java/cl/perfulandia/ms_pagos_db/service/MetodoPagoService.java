package cl.perfulandia.ms_pagos_db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.perfulandia.ms_pagos_db.model.dto.MetodoPagoDTO;
import cl.perfulandia.ms_pagos_db.model.entities.MetodoPago;
import cl.perfulandia.ms_pagos_db.model.repository.MetodoPagoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetodoPagoService {

    @Autowired
    MetodoPagoRepository metodoPagoRepository;

    public MetodoPagoDTO findMetodoPagoById(Long id) {
        log.info("Finding metodo pago by id: {}", id);
        Optional<MetodoPago> metodoPago = metodoPagoRepository.findById(id);

        MetodoPagoDTO metodoPagoDTO = null;

        if(metodoPago.isPresent()){
            metodoPagoDTO = translateEntityToDto(metodoPago.get());
            log.info("Metodo pago found: {}", metodoPagoDTO);
        } else {
            log.warn("Metodo pago not found with id: {}", id);
        }

        return metodoPagoDTO;
    }

    public List<MetodoPagoDTO> findAllMetodosPago(){
        log.info("Finding all metodos pago");
        List<MetodoPago> metodosPago = metodoPagoRepository.findAll();

        List<MetodoPagoDTO> metodosPagoDTO = new ArrayList<>();

        for(MetodoPago metodoPago : metodosPago){
            metodosPagoDTO.add(translateEntityToDto(metodoPago));
        }

        log.info("Found {} metodos pago", metodosPagoDTO.size());
        return metodosPagoDTO;
    }

    public List<MetodoPagoDTO> findActiveMetodosPago(){
        log.info("Finding active metodos pago");
        List<MetodoPago> metodosPago = metodoPagoRepository.findByActivoTrue();

        List<MetodoPagoDTO> metodosPagoDTO = new ArrayList<>();

        for(MetodoPago metodoPago : metodosPago){
            metodosPagoDTO.add(translateEntityToDto(metodoPago));
        }

        log.info("Found {} active metodos pago", metodosPagoDTO.size());
        return metodosPagoDTO;
    }
    
    public MetodoPagoDTO createMetodoPago(MetodoPagoDTO metodoPagoDTO) {
        log.info("Creating new metodo pago: {}", metodoPagoDTO);
        MetodoPago metodoPago = translateDtoToEntity(metodoPagoDTO);
        MetodoPago savedMetodoPago = metodoPagoRepository.save(metodoPago);
        MetodoPagoDTO result = translateEntityToDto(savedMetodoPago);
        log.info("Metodo pago created successfully with id: {}", result.getId());
        return result;
    }

    public MetodoPagoDTO updateMetodoPago(Long id, MetodoPagoDTO metodoPagoDTO) {
        log.info("Updating metodo pago with id: {} with data: {}", id, metodoPagoDTO);
        Optional<MetodoPago> existingMetodoPago = metodoPagoRepository.findById(id);

        MetodoPagoDTO updatedMetodoPagoDTO = null;

        if(existingMetodoPago.isPresent()) {
            MetodoPago metodoPago = translateDtoToEntity(metodoPagoDTO);
            metodoPago.setId(id);
            metodoPago.setFechaCreacion(existingMetodoPago.get().getFechaCreacion());
            MetodoPago updatedMetodoPago = metodoPagoRepository.save(metodoPago);
            updatedMetodoPagoDTO = translateEntityToDto(updatedMetodoPago);
            log.info("Metodo pago updated successfully: {}", updatedMetodoPagoDTO);
        } else {
            log.warn("Cannot update metodo pago - not found with id: {}", id);
        }

        return updatedMetodoPagoDTO;
    }

    public MetodoPagoDTO deleteMetodoPago(Long id) {
        log.info("Deleting metodo pago with id: {}", id);
        Optional<MetodoPago> metodoPago = metodoPagoRepository.findById(id);

        MetodoPagoDTO metodoPagoDTO = null;

        if(metodoPago.isPresent()){
            metodoPagoDTO = translateEntityToDto(metodoPago.get());
            metodoPagoRepository.deleteById(id);
            log.info("Metodo pago deleted successfully: {}", metodoPagoDTO);
            return metodoPagoDTO;
        }

        log.warn("Cannot delete metodo pago - not found with id: {}", id);
        return metodoPagoDTO;
    }

    private MetodoPago translateDtoToEntity(MetodoPagoDTO metodoPagoDTO) {
        MetodoPago metodoPago = new MetodoPago();
        metodoPago.setNombre(metodoPagoDTO.getNombre());
        metodoPago.setDescripcion(metodoPagoDTO.getDescripcion());
        metodoPago.setActivo(metodoPagoDTO.getActivo() != null ? metodoPagoDTO.getActivo() : true);
        return metodoPago;
    }

    public MetodoPagoDTO translateEntityToDto(MetodoPago metodoPago){
        MetodoPagoDTO metodoPagoDTO = new MetodoPagoDTO();
        metodoPagoDTO.setId(metodoPago.getId());
        metodoPagoDTO.setNombre(metodoPago.getNombre());
        metodoPagoDTO.setDescripcion(metodoPago.getDescripcion());
        metodoPagoDTO.setActivo(metodoPago.getActivo());
        metodoPagoDTO.setFechaCreacion(metodoPago.getFechaCreacion());
        metodoPagoDTO.setFechaActualizacion(metodoPago.getFechaActualizacion());
        return metodoPagoDTO;
    }
}
