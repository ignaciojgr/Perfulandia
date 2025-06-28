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
        Optional<MetodoPago> metodoPago = metodoPagoRepository.findById(id);

        MetodoPagoDTO metodoPagoDTO = null;

        if(metodoPago.isPresent()){
            metodoPagoDTO = translateEntityToDto(metodoPago.get());
        }

        return metodoPagoDTO;
    }

    public List<MetodoPagoDTO> findAllMetodosPago(){
        List<MetodoPago> metodosPago = metodoPagoRepository.findAll();

        List<MetodoPagoDTO> metodosPagoDTO = new ArrayList<>();

        for(MetodoPago metodoPago : metodosPago){
            metodosPagoDTO.add(translateEntityToDto(metodoPago));
        }
        return metodosPagoDTO;
    }

    public List<MetodoPagoDTO> findActiveMetodosPago(){
        List<MetodoPago> metodosPago = metodoPagoRepository.findByActivoTrue();

        List<MetodoPagoDTO> metodosPagoDTO = new ArrayList<>();

        for(MetodoPago metodoPago : metodosPago){
            metodosPagoDTO.add(translateEntityToDto(metodoPago));
        }
        return metodosPagoDTO;
    }
    
    public MetodoPagoDTO createMetodoPago(MetodoPagoDTO metodoPagoDTO) {
        MetodoPago metodoPago = translateDtoToEntity(metodoPagoDTO);
        MetodoPago savedMetodoPago = metodoPagoRepository.save(metodoPago);
        MetodoPagoDTO result = translateEntityToDto(savedMetodoPago);
        return result;
    }

    public MetodoPagoDTO updateMetodoPago(Long id, MetodoPagoDTO metodoPagoDTO) {
        Optional<MetodoPago> existingMetodoPago = metodoPagoRepository.findById(id);

        MetodoPagoDTO updatedMetodoPagoDTO = null;

        if(existingMetodoPago.isPresent()) {
            MetodoPago metodoPago = translateDtoToEntity(metodoPagoDTO);
            metodoPago.setId(id);
            metodoPago.setFechaCreacion(existingMetodoPago.get().getFechaCreacion());
            MetodoPago updatedMetodoPago = metodoPagoRepository.save(metodoPago);
            updatedMetodoPagoDTO = translateEntityToDto(updatedMetodoPago);
        }

        return updatedMetodoPagoDTO;
    }

    public MetodoPagoDTO deleteMetodoPago(Long id) {
        Optional<MetodoPago> metodoPago = metodoPagoRepository.findById(id);

        MetodoPagoDTO metodoPagoDTO = null;

        if(metodoPago.isPresent()){
            metodoPagoDTO = translateEntityToDto(metodoPago.get());
            metodoPagoRepository.deleteById(id);
            return metodoPagoDTO;
        }
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
