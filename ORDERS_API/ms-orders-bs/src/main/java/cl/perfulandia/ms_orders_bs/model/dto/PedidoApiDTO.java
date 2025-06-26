package cl.perfulandia.ms_orders_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoApiDTO {
    private Long id;
    private Long clienteId;
    private Long productoId;     
    private Integer cantidad;
    private String estado;
}
