package cl.perfulandia.ms_pagos_db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.perfulandia.ms_pagos_db.model.dto.EstadoPagoDTO;
import cl.perfulandia.ms_pagos_db.model.entities.EstadoPago;
import cl.perfulandia.ms_pagos_db.model.repository.EstadoPagoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EstadoPagoService {

    @Autowired
    EstadoPagoRepository estadoPagoRepository;

    public EstadoPagoDTO findEstadoPagoById(Long id) {
        Optional<EstadoPago> estadoPago = estadoPagoRepository.findById(id);

        EstadoPagoDTO estadoPagoDTO = null;

        if(estadoPago.isPresent()){
            estadoPagoDTO = translateEntityToDto(estadoPago.get());
        }

        return estadoPagoDTO;
    }

    public EstadoPagoDTO findEstadoPagoByCodigo(String codigo) {
        Optional<EstadoPago> estadoPago = estadoPagoRepository.findByCodigo(codigo);

        EstadoPagoDTO estadoPagoDTO = null;

        if(estadoPago.isPresent()){
            estadoPagoDTO = translateEntityToDto(estadoPago.get());
        }

        return estadoPagoDTO;
    }

    public List<EstadoPagoDTO> findAllEstadosPago(){
        List<EstadoPago> estadosPago = estadoPagoRepository.findAll();

        List<EstadoPagoDTO> estadosPagoDTO = new ArrayList<>();

        for(EstadoPago estadoPago : estadosPago){
            estadosPagoDTO.add(translateEntityToDto(estadoPago));
        }
        return estadosPagoDTO;
    }

    public List<EstadoPagoDTO> findActiveEstadosPago(){
        List<EstadoPago> estadosPago = estadoPagoRepository.findByActivoTrue();

        List<EstadoPagoDTO> estadosPagoDTO = new ArrayList<>();

        for(EstadoPago estadoPago : estadosPago){
            estadosPagoDTO.add(translateEntityToDto(estadoPago));
        }
        return estadosPagoDTO;
    }
    
    public EstadoPagoDTO createEstadoPago(EstadoPagoDTO estadoPagoDTO) {
        EstadoPago estadoPago = translateDtoToEntity(estadoPagoDTO);
        EstadoPago savedEstadoPago = estadoPagoRepository.save(estadoPago);
        EstadoPagoDTO result = translateEntityToDto(savedEstadoPago);
        return result;
    }

    public EstadoPagoDTO updateEstadoPago(Long id, EstadoPagoDTO estadoPagoDTO) {
        Optional<EstadoPago> existingEstadoPago = estadoPagoRepository.findById(id);

        EstadoPagoDTO updatedEstadoPagoDTO = null;

        if(existingEstadoPago.isPresent()) {
            EstadoPago estadoPago = translateDtoToEntity(estadoPagoDTO);
            estadoPago.setId(id);
            estadoPago.setFechaCreacion(existingEstadoPago.get().getFechaCreacion());
            EstadoPago updatedEstadoPago = estadoPagoRepository.save(estadoPago);
            updatedEstadoPagoDTO = translateEntityToDto(updatedEstadoPago);
        }

        return updatedEstadoPagoDTO;
    }

    public EstadoPagoDTO deleteEstadoPago(Long id) {
        Optional<EstadoPago> estadoPago = estadoPagoRepository.findById(id);

        EstadoPagoDTO estadoPagoDTO = null;

        if(estadoPago.isPresent()){
            estadoPagoDTO = translateEntityToDto(estadoPago.get());
            estadoPagoRepository.deleteById(id);
            return estadoPagoDTO;
        }
        return estadoPagoDTO;
    }

    private EstadoPago translateDtoToEntity(EstadoPagoDTO estadoPagoDTO) {
        EstadoPago estadoPago = new EstadoPago();
        estadoPago.setCodigo(estadoPagoDTO.getCodigo());
        estadoPago.setNombre(estadoPagoDTO.getNombre());
        estadoPago.setDescripcion(estadoPagoDTO.getDescripcion());
        estadoPago.setActivo(estadoPagoDTO.getActivo() != null ? estadoPagoDTO.getActivo() : true);
        return estadoPago;
    }

    public EstadoPagoDTO translateEntityToDto(EstadoPago estadoPago){
        EstadoPagoDTO estadoPagoDTO = new EstadoPagoDTO();
        estadoPagoDTO.setId(estadoPago.getId());
        estadoPagoDTO.setCodigo(estadoPago.getCodigo());
        estadoPagoDTO.setNombre(estadoPago.getNombre());
        estadoPagoDTO.setDescripcion(estadoPago.getDescripcion());
        estadoPagoDTO.setActivo(estadoPago.getActivo());
        estadoPagoDTO.setFechaCreacion(estadoPago.getFechaCreacion());
        estadoPagoDTO.setFechaActualizacion(estadoPago.getFechaActualizacion());
        return estadoPagoDTO;
    }
}
