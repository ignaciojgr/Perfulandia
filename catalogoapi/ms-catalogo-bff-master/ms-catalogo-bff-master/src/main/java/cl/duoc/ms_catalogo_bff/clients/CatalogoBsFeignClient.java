package cl.duoc.ms_catalogo_bff.clients;

import cl.duoc.ms_catalogo_bff.model.dto.CatalogItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@FeignClient(name = "ms-catalogo-bs", url = "http://localhost:8791")
public interface CatalogoBsFeignClient {

    @GetMapping("/api/catalog/items")
    ResponseEntity<List<CatalogItemDTO>> getAllItems();

    @GetMapping("/api/catalog/items/{id}")
    ResponseEntity<CatalogItemDTO> getItemById(@PathVariable("id") Long id);

    @PostMapping("/api/catalog/items")
    ResponseEntity<CatalogItemDTO> createItem(@RequestBody CatalogItemDTO catalogItemDTO);

    @PutMapping("/api/catalog/items/{id}")
    ResponseEntity<CatalogItemDTO> updateItem(@PathVariable("id") Long id, @RequestBody CatalogItemDTO catalogItemDTO);

    @DeleteMapping("/api/catalog/items/{id}")
    ResponseEntity<Void> deleteItem(@PathVariable("id") Long id);

    @GetMapping("/api/catalog/items/search")
    ResponseEntity<List<CatalogItemDTO>> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice);

    @GetMapping("/api/catalog/items/category/{category}")
    ResponseEntity<List<CatalogItemDTO>> getItemsByCategory(@PathVariable("category") String category);

    @GetMapping("/api/catalog/items/brand/{brand}")
    ResponseEntity<List<CatalogItemDTO>> getItemsByBrand(@PathVariable("brand") String brand);

    @GetMapping("/api/catalog/items/price-range")
    ResponseEntity<List<CatalogItemDTO>> getItemsByPriceRange(
            @RequestParam("min") Double minPrice,
            @RequestParam("max") Double maxPrice);
}


