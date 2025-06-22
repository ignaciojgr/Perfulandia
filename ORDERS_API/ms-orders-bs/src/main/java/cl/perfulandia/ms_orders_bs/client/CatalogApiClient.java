package cl.perfulandia.ms_orders_bs.client;

import cl.perfulandia.ms_orders_bs.model.dto.CatalogItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-catalogo-bs", url = "${ms-catalogo-bs.url}")
public interface CatalogApiClient {
    
    @GetMapping("/api/catalog/items/{id}")
    ResponseEntity<CatalogItemDTO> getItemById(@PathVariable Long id);
    
    @GetMapping("/api/catalog/items")
    ResponseEntity<List<CatalogItemDTO>> getActiveItems();
      @GetMapping("/api/catalog/items/search")
    ResponseEntity<List<CatalogItemDTO>> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Boolean active
    );
}
