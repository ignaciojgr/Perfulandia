package cl.perfulandia.ms_pagos_db.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoDTO {
    private Long id;
    private String nombre;
}
