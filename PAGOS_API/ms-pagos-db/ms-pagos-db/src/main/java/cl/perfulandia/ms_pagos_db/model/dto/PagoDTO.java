package cl.perfulandia.ms_pagos_db.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PagoDTO {
    private Long id;
    
    @NotBlank(message = "El ID de orden es requerido")
    private String idOrden;
    
    @NotBlank(message = "El ID de usuario es requerido")
    private String idUsuario;
    
    @NotNull(message = "El monto es requerido")
    @Positive(message = "El monto debe ser positivo")
    private Long monto;
    
    @NotBlank(message = "El estado es requerido")
    private String idEstado;
    
    private String idTransaccion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
