package cl.perfulandia.ms_pagos_db.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.perfulandia.ms_pagos_db.model.dto.PagoDTO;
import cl.perfulandia.ms_pagos_db.model.entities.Pago;
import cl.perfulandia.ms_pagos_db.model.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PagoService {

    @Autowired
    PagoRepository pagoRepository;

    public PagoDTO findPagoById(Long id) {
        log.info("Finding pago by id: {}", id);
        Optional<Pago> pago = pagoRepository.findById(id);

        PagoDTO pagoDTO = null;

        if(pago.isPresent()){
            pagoDTO = translateEntityToDto(pago.get());
            log.info("Pago found: {}", pagoDTO);
        } else {
            log.warn("Pago not found with id: {}", id);
        }

        return pagoDTO;
    }

    public List<PagoDTO> findPagosByIdUsuario(String idUsuario){
        log.info("Finding pagos by user id: {}", idUsuario);
        List<Pago> pagos = pagoRepository.findByIdUsuario(idUsuario);

        List<PagoDTO> pagosDTO = new ArrayList<>();

        for(Pago pago : pagos){
            pagosDTO.add(translateEntityToDto(pago));
        }

        log.info("Found {} pagos for user: {}", pagosDTO.size(), idUsuario);
        return pagosDTO;
    }    
    public PagoDTO createPago(PagoDTO pagoDTO) {
        log.info("Creating new pago: {}", pagoDTO);
        Pago pago = translateDtoToEntity(pagoDTO);
        Pago savedPago = pagoRepository.save(pago);
        PagoDTO result = translateEntityToDto(savedPago);
        log.info("Pago created successfully with id: {}", result.getId());
        return result;
    }

    public PagoDTO deletePago (Long id) {
        log.info("Deleting pago with id: {}", id);
        Optional<Pago> pago = pagoRepository.findById(id);

        PagoDTO pagoDTO = null;

        if(pago.isPresent()){
            pagoDTO = translateEntityToDto(pago.get());
            pagoRepository.deleteById(id);
            log.info("Pago deleted successfully: {}", pagoDTO);
            return pagoDTO;
        }

        log.warn("Cannot delete pago - not found with id: {}", id);
        return pagoDTO;
    }

    private Pago translateDtoToEntity(PagoDTO pagoDTO) {
        Pago pago = new Pago();
        pago.setIdOrden(pagoDTO.getIdOrden());
        pago.setIdUsuario(pagoDTO.getIdUsuario());
        pago.setMonto(pagoDTO.getMonto());
        pago.setIdEstado(pagoDTO.getIdEstado());
        pago.setIdTransaccion(pagoDTO.getIdTransaccion());
        return pago;
    }

    public PagoDTO translateEntityToDto(Pago pago){
        PagoDTO pagoDTO = new PagoDTO();
        pagoDTO.setId(pago.getId());
        pagoDTO.setIdOrden(pago.getIdOrden());
        pagoDTO.setIdUsuario(pago.getIdUsuario());
        pagoDTO.setMonto(pago.getMonto());
        pagoDTO.setIdEstado(pago.getIdEstado());
        pagoDTO.setIdTransaccion(pago.getIdTransaccion());
        pagoDTO.setFechaCreacion(pago.getFechaCreacion());
        pagoDTO.setFechaActualizacion(pago.getFechaActualizacion());
        return pagoDTO;
    }    public PagoDTO updatePago(Long id, PagoDTO pagoDTO) {
        log.info("Updating pago with id: {} with data: {}", id, pagoDTO);
        Optional<Pago> existingPago = pagoRepository.findById(id);

        PagoDTO updatedPagoDTO = null;

        if(existingPago.isPresent()) {
            Pago pago = translateDtoToEntity(pagoDTO);
            pago.setId(id);
            pago.setFechaCreacion(existingPago.get().getFechaCreacion());
            Pago updatedPago = pagoRepository.save(pago);
            updatedPagoDTO = translateEntityToDto(updatedPago);
            log.info("Pago updated successfully: {}", updatedPagoDTO);
        } else {
            log.warn("Cannot update pago - not found with id: {}", id);
        }

        return updatedPagoDTO;
    }

    public PagoDTO partialUpdatePago(Long id, PagoDTO pagoDTO) {
        log.info("Partially updating pago with id: {} with data: {}", id, pagoDTO);
        Optional<Pago> existingPago = pagoRepository.findById(id);

        PagoDTO updatedPagoDTO = null;

        if(existingPago.isPresent()){
            Pago pago = existingPago.get();
            
            if(pagoDTO.getIdOrden() != null){
                pago.setIdOrden(pagoDTO.getIdOrden());
            }

            if(pagoDTO.getIdUsuario() != null) {
                pago.setIdUsuario(pagoDTO.getIdUsuario());
            }

            if(pagoDTO.getMonto() != null) {
                pago.setMonto(pagoDTO.getMonto());
            }

            if(pagoDTO.getIdEstado() != null) {
                pago.setIdEstado(pagoDTO.getIdEstado());
            }

            if(pagoDTO.getIdTransaccion() != null) {
                pago.setIdTransaccion(pagoDTO.getIdTransaccion());
            }

            Pago updatedPago = pagoRepository.save(pago);
            updatedPagoDTO = translateEntityToDto(updatedPago);
            log.info("Pago partially updated successfully: {}", updatedPagoDTO);
        } else {
            log.warn("Cannot partially update pago - not found with id: {}", id);
        }

        return updatedPagoDTO;
    }

    public List<PagoDTO> findAllPagos(){
        log.info("Finding all pagos");
        List<Pago> pagos = pagoRepository.findAll();

        List<PagoDTO> pagosDTO = new ArrayList<>();

        for(Pago pago : pagos){
            pagosDTO.add(translateEntityToDto(pago));
        }

        log.info("Found {} pagos", pagosDTO.size());
        return pagosDTO;
    }

    public List<PagoDTO> findPagosByIdOrden(String idOrden){
        log.info("Finding pagos by order id: {}", idOrden);
        List<Pago> pagos = pagoRepository.findByIdOrden(idOrden);

        List<PagoDTO> pagosDTO = new ArrayList<>();

        for(Pago pago : pagos){
            pagosDTO.add(translateEntityToDto(pago));
        }

        log.info("Found {} pagos for order: {}", pagosDTO.size(), idOrden);
        return pagosDTO;
    }

    public List<PagoDTO> findPagosByEstado(String idEstado){
        log.info("Finding pagos by estado: {}", idEstado);
        List<Pago> pagos = pagoRepository.findByIdEstado(idEstado);

        List<PagoDTO> pagosDTO = new ArrayList<>();

        for(Pago pago : pagos){
            pagosDTO.add(translateEntityToDto(pago));
        }

        log.info("Found {} pagos with estado: {}", pagosDTO.size(), idEstado);
        return pagosDTO;
    }
}
