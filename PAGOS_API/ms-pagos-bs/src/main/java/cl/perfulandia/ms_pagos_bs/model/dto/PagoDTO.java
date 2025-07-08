package cl.perfulandia.ms_pagos_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTO {
    private Long id;
    private String orderId;
    private String userId;
    private Long monto;
    private String idStatus;
    private String transbankToken;
}
