package cl.duoc.ms_catalogo_bff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import cl.duoc.ms_catalogo_bff.model.dto.CatalogItemDTO;
import cl.duoc.ms_catalogo_bff.service.CatalogBffService;
import java.util.List;

@RestController
@RequestMapping("/api/bff/catalog")
public class CatalogBffController {

    @Autowired
    CatalogBffService catalogBffService;

    @GetMapping("/items")
    public ResponseEntity<List<CatalogItemDTO>> getAllItems() {
        List<CatalogItemDTO> items = catalogBffService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<CatalogItemDTO> getItemById(@PathVariable("id") Long id) {
        CatalogItemDTO item = catalogBffService.getItemById(id);
        return (item != null)
            ? new ResponseEntity<>(item, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/items")
    public ResponseEntity<CatalogItemDTO> createItem(@RequestBody CatalogItemDTO catalogItemDTO) {
        CatalogItemDTO createdItem = catalogBffService.createItem(catalogItemDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<CatalogItemDTO> updateItem(@PathVariable("id") Long id, @RequestBody CatalogItemDTO catalogItemDTO) {
        CatalogItemDTO updatedItem = catalogBffService.updateItem(id, catalogItemDTO);
        return (updatedItem != null)
            ? new ResponseEntity<>(updatedItem, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        boolean deleted = catalogBffService.deleteItem(id);
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
        List<CatalogItemDTO> items = catalogBffService.searchItems(name, category, brand, minPrice, maxPrice);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByCategory(@PathVariable("category") String category) {
        List<CatalogItemDTO> items = catalogBffService.getItemsByCategory(category);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/brand/{brand}")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByBrand(@PathVariable("brand") String brand) {
        List<CatalogItemDTO> items = catalogBffService.getItemsByBrand(brand);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/items/price-range")
    public ResponseEntity<List<CatalogItemDTO>> getItemsByPriceRange(
            @RequestParam("min") Double minPrice,
            @RequestParam("max") Double maxPrice) {
        List<CatalogItemDTO> items = catalogBffService.getItemsByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}

