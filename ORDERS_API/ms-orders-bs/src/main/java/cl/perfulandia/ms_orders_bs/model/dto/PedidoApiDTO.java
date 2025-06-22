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

    public PedidoApiDTO(Long clienteId, Long productoId, Integer cantidad, String estado) {
        this.clienteId = clienteId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.estado = estado;
    }
}
