package cl.duoc.ms_catalogo_db.controller;

import cl.duoc.ms_catalogo_db.model.dto.CatalogItemDTO;
import cl.duoc.ms_catalogo_db.service.CatalogItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@Slf4j
public class CatalogItemController {

    @Autowired
    CatalogItemService catalogItemService;

    @GetMapping
    public ResponseEntity<List<CatalogItemDTO>> getAllItems() {
        log.info("GET /api/catalog - Fetching all catalog items");
        List<CatalogItemDTO> items = catalogItemService.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatalogItemDTO> getItemById(@PathVariable("id") Integer id) {
        log.info("GET /api/catalog/{} - Fetching catalog item", id);
        CatalogItemDTO item = catalogItemService.getItemById(id);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<CatalogItemDTO>> getActiveItems() {
        log.info("GET /api/catalog/active - Fetching active catalog items");
        List<CatalogItemDTO> items = catalogItemService.getActiveItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CatalogItemDTO>> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        
        log.info("GET /api/catalog/search - Searching with criteria: name={}, category={}, brand={}, active={}, minPrice={}, maxPrice={}", 
                name, category, brand, active, minPrice, maxPrice);
        
        List<CatalogItemDTO> items = catalogItemService.searchByCriteria(name, category, brand, active, minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<CatalogItemDTO>> searchByName(@PathVariable("name") String name) {
        log.info("GET /api/catalog/name/{} - Searching by name", name);
        List<CatalogItemDTO> items = catalogItemService.searchByName(name);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByCategory(@PathVariable("categoryName") String categoryName) {
        log.info("GET /api/catalog/category/{} - Fetching items by category", categoryName);
        List<CatalogItemDTO> items = catalogItemService.getItemsByCategory(categoryName);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/brand/{brandName}")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByBrand(@PathVariable("brandName") String brandName) {
        log.info("GET /api/catalog/brand/{} - Fetching items by brand", brandName);
        List<CatalogItemDTO> items = catalogItemService.getItemsByBrand(brandName);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByPriceRange(
            @RequestParam("minPrice") Double minPrice,
            @RequestParam("maxPrice") Double maxPrice) {
        log.info("GET /api/catalog/price-range - Fetching items by price range: {} - {}", minPrice, maxPrice);
        List<CatalogItemDTO> items = catalogItemService.getItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<CatalogItemDTO> getItemBySku(@PathVariable("sku") String sku) {
        log.info("GET /api/catalog/sku/{} - Fetching item by SKU", sku);
        CatalogItemDTO item = catalogItemService.getItemBySku(sku);
        if (item != null) {
            return ResponseEntity.ok(item);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<CatalogItemDTO>> getLowStockItems() {
        log.info("GET /api/catalog/low-stock - Fetching low stock items");
        List<CatalogItemDTO> items = catalogItemService.getLowStockItems();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<CatalogItemDTO> createItem(@RequestBody CatalogItemDTO catalogItemDTO) {
        log.info("POST /api/catalog - Creating new catalog item: {}", catalogItemDTO.getName());
        CatalogItemDTO createdItem = catalogItemService.createItem(catalogItemDTO);
        if (createdItem != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CatalogItemDTO> updateItem(
            @PathVariable("id") Integer id,
            @RequestBody CatalogItemDTO catalogItemDTO) {
        log.info("PUT /api/catalog/{} - Updating catalog item", id);
        CatalogItemDTO updatedItem = catalogItemService.updateItem(id, catalogItemDTO);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<CatalogItemDTO> updateStock(
            @PathVariable("id") Integer id,
            @RequestParam("stockQuantity") Integer stockQuantity) {
        log.info("PATCH /api/catalog/{}/stock - Updating stock to {}", id, stockQuantity);
        CatalogItemDTO updatedItem = catalogItemService.updateStock(id, stockQuantity);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Integer id) {
        log.info("DELETE /api/catalog/{} - Deleting catalog item", id);
        boolean deleted = catalogItemService.deleteItem(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
