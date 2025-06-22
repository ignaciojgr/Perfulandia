package cl.perfulandia.ms_pagos_db.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.perfulandia.ms_pagos_db.model.entities.MetodoPago;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Long> {
    List<MetodoPago> findByActivoTrue();
    List<MetodoPago> findByNombreContainingIgnoreCase(String nombre);
}
