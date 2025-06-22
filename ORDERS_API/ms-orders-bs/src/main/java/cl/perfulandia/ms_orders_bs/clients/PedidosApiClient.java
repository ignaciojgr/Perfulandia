package cl.perfulandia.ms_orders_bs.clients;

import cl.perfulandia.ms_orders_bs.model.dto.PedidoApiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-pedido-bs", url = "${ms-pedido-bs.url}")
public interface PedidosApiClient {

    @PostMapping("/pedidos")
    ResponseEntity<PedidoApiDTO> crearPedido(@RequestBody PedidoApiDTO pedidoDTO);
}
