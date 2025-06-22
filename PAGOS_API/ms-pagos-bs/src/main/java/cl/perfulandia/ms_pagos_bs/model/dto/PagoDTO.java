package cl.perfulandia.ms_pagos_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private String idOrden;
    private String idUsuario;
    private BigDecimal monto;
    private String idEstado;
    private String idTransaccion;
}
