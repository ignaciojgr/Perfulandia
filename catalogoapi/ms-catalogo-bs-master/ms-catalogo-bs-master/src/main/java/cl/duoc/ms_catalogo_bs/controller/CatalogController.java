package cl.duoc.ms_catalogo_bs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.duoc.ms_catalogo_bs.model.dto.CatalogItemDTO;
import cl.duoc.ms_catalogo_bs.service.CatalogService;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @GetMapping("/items")
    public ResponseEntity<List<CatalogItemDTO>> getAllItems() {
        List<CatalogItemDTO> items = catalogService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<CatalogItemDTO> getItemById(@PathVariable("id") Long id) {
        CatalogItemDTO item = catalogService.getItemById(id);
        return (item != null) 
            ? new ResponseEntity<>(item, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/items/sku/{sku}")
    public ResponseEntity<CatalogItemDTO> getItemBySku(@PathVariable("sku") String sku) {
        CatalogItemDTO item = catalogService.getItemBySku(sku);
        return (item != null) 
            ? new ResponseEntity<>(item, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/items")
    public ResponseEntity<CatalogItemDTO> createItem(@RequestBody CatalogItemDTO catalogItemDTO) {
        CatalogItemDTO createdItem = catalogService.createItem(catalogItemDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CatalogItemDTO> updateItem(@PathVariable("id") Long id, @RequestBody CatalogItemDTO catalogItemDTO) {
        CatalogItemDTO updatedItem = catalogService.updateItem(id, catalogItemDTO);
        return (updatedItem != null) 
            ? new ResponseEntity<>(updatedItem, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/items/{id}/stock")
    public ResponseEntity<CatalogItemDTO> updateStock(@PathVariable("id") Long id, @RequestBody Integer stockQuantity) {
        CatalogItemDTO updatedItem = catalogService.updateStock(id, stockQuantity);
        return (updatedItem != null) 
            ? new ResponseEntity<>(updatedItem, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        boolean deleted = catalogService.deleteItem(id);
        return deleted 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/items/search")
    public ResponseEntity<List<CatalogItemDTO>> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<CatalogItemDTO> items = catalogService.searchItems(name, category, brand, minPrice, maxPrice);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByCategory(@PathVariable("category") String category) {
        List<CatalogItemDTO> items = catalogService.getItemsByCategory(category);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/brand/{brand}")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByBrand(@PathVariable("brand") String brand) {
        List<CatalogItemDTO> items = catalogService.getItemsByBrand(brand);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/price-range")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByPriceRange(
            @RequestParam("min") Double minPrice,
            @RequestParam("max") Double maxPrice) {
        List<CatalogItemDTO> items = catalogService.getItemsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
