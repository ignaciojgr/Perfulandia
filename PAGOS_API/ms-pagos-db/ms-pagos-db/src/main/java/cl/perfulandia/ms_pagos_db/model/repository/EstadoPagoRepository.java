package cl.perfulandia.ms_pagos_db.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.perfulandia.ms_pagos_db.model.entities.EstadoPago;

public interface EstadoPagoRepository extends JpaRepository<EstadoPago, Long> {
    List<EstadoPago> findByActivoTrue();
    Optional<EstadoPago> findByCodigo(String codigo);
    List<EstadoPago> findByNombreContainingIgnoreCase(String nombre);
}
