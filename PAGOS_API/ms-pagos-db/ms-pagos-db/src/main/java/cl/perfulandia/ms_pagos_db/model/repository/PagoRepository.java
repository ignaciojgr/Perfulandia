package cl.perfulandia.ms_pagos_db.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.perfulandia.ms_pagos_db.model.entities.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long>{

    List<Pago> findByIdUsuario(String idUsuario);
    List<Pago> findByIdOrden(String idOrden);
    List<Pago> findByIdEstado(String idEstado);
    List<Pago> findByIdTransaccion(String idTransaccion);
}
