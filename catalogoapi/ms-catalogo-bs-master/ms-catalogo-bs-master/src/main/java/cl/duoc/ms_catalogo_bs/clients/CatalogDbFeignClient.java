package cl.duoc.ms_catalogo_bs.clients;

import cl.duoc.ms_catalogo_bs.model.dto.CatalogItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@FeignClient(name = "ms-catalogo-db", url = "http://localhost:8790")
public interface CatalogDbFeignClient {

    @GetMapping("/api/catalog")
    ResponseEntity<List<CatalogItemDTO>> getAllItems();    
    
    @GetMapping("/api/catalog/{id}")
    ResponseEntity<CatalogItemDTO> getItemById(@PathVariable("id") Long id);

    @GetMapping("/api/catalog/active")
    ResponseEntity<List<CatalogItemDTO>> getActiveItems();

    @GetMapping("/api/catalog/name/{name}")
    ResponseEntity<List<CatalogItemDTO>> searchByName(@PathVariable("name") String name);

    @GetMapping("/api/catalog/category/{categoryName}")
    ResponseEntity<List<CatalogItemDTO>> getItemsByCategory(@PathVariable("categoryName") String categoryName);

    @GetMapping("/api/catalog/brand/{brandName}")
    ResponseEntity<List<CatalogItemDTO>> getItemsByBrand(@PathVariable("brandName") String brandName);

    @GetMapping("/api/catalog/sku/{sku}")
    ResponseEntity<CatalogItemDTO> getItemBySku(@PathVariable("sku") String sku);

    @GetMapping("/api/catalog/low-stock")
    ResponseEntity<List<CatalogItemDTO>> getLowStockItems();

    @GetMapping("/api/catalog/price-range")
    ResponseEntity<List<CatalogItemDTO>> getItemsByPriceRange(
            @RequestParam("minPrice") Double minPrice,
            @RequestParam("maxPrice") Double maxPrice);

    @GetMapping("/api/catalog/search")
    ResponseEntity<List<CatalogItemDTO>> searchItems(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice);

    @PostMapping("/api/catalog")
    ResponseEntity<CatalogItemDTO> createItem(@RequestBody CatalogItemDTO catalogItemDTO);

    @PutMapping("/api/catalog/{id}")
    ResponseEntity<CatalogItemDTO> updateItem(@PathVariable("id") Long id, @RequestBody CatalogItemDTO catalogItemDTO);

    @PatchMapping("/api/catalog/{id}/stock")
    ResponseEntity<CatalogItemDTO> updateStock(@PathVariable("id") Long id, @RequestParam("stockQuantity") Integer stockQuantity);

    @DeleteMapping("/api/catalog/{id}")
    ResponseEntity<Void> deleteItem(@PathVariable("id") Long id);
}
