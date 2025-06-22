package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.clients.PedidosApiClient;
import cl.perfulandia.ms_orders_bs.model.dto.OrderDTO;
import cl.perfulandia.ms_orders_bs.model.dto.OrderItemDTO;
import cl.perfulandia.ms_orders_bs.model.dto.PedidoApiDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PedidosIntegrationService {

    @Autowired
    PedidosApiClient pedidosApiClient;

    public List<PedidoApiDTO> createPedidosForOrder(OrderDTO order) {
        log.info("Creating pedidos for order: {}", order.getOrderId());
        
        List<PedidoApiDTO> createdPedidos = new ArrayList<>();
        
        try {
            for (OrderItemDTO item : order.getItems()) {
                PedidoApiDTO pedido = createPedidoFromOrderItem(order, item);
                PedidoApiDTO createdPedido = pedidosApiClient.crearPedido(pedido).getBody();
                
                if (createdPedido != null) {
                    createdPedidos.add(createdPedido);
                    log.info("Created pedido ID: {} for product: {}", 
                            createdPedido.getId(), item.getProductId());
                } else {
                    log.error("Failed to create pedido for product: {}", item.getProductId());
                }
            }
            
            log.info("Successfully created {} pedidos for order: {}", 
                    createdPedidos.size(), order.getOrderId());
            
        } catch (Exception e) {
            log.error("Error creating pedidos for order: {}", order.getOrderId(), e);
            throw new PedidosIntegrationException("Failed to create pedidos: " + e.getMessage(), e);
        }
        
        return createdPedidos;
    }

    private PedidoApiDTO createPedidoFromOrderItem(OrderDTO order, OrderItemDTO item) {
        PedidoApiDTO pedido = new PedidoApiDTO();

        pedido.setClienteId(Long.parseLong(order.getUserId()));
        pedido.setProductoId(Long.parseLong(item.getProductId()));
        pedido.setCantidad(item.getQuantity());
        pedido.setEstado("CREADO");
        
        return pedido;
    }
    public static class PedidosIntegrationException extends RuntimeException {
        public PedidosIntegrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
