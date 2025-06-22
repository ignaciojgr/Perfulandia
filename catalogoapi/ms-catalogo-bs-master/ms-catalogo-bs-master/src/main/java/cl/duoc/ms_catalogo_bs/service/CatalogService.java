package cl.duoc.ms_catalogo_bs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import lombok.extern.slf4j.Slf4j;

import cl.duoc.ms_catalogo_bs.clients.CatalogDbFeignClient;
import cl.duoc.ms_catalogo_bs.model.dto.CatalogItemDTO;

import java.util.List;

@Service
@Slf4j
public class CatalogService {

    @Autowired
    private CatalogDbFeignClient catalogDbFeignClient;

    public List<CatalogItemDTO> getAllItems() {
        log.info("Fetching all catalog items from database service");
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.getAllItems();
        return response.getBody();
    }    
    
    public CatalogItemDTO getItemById(Long id) {
        log.info("Fetching catalog item with ID: {}", id);
        ResponseEntity<CatalogItemDTO> response = catalogDbFeignClient.getItemById(id);
        return response.getBody();
    }

    public List<CatalogItemDTO> getActiveItems() {
        log.info("Fetching active catalog items");
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.getActiveItems();
        return response.getBody();
    }

    public List<CatalogItemDTO> searchByName(String name) {
        log.info("Searching catalog items by name: {}", name);
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.searchByName(name);
        return response.getBody();
    }

    public List<CatalogItemDTO> getItemsByCategory(String categoryName) {
        log.info("Fetching catalog items by category: {}", categoryName);
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.getItemsByCategory(categoryName);
        return response.getBody();
    }

    public List<CatalogItemDTO> getItemsByBrand(String brandName) {
        log.info("Fetching catalog items by brand: {}", brandName);
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.getItemsByBrand(brandName);
        return response.getBody();
    }

    public CatalogItemDTO getItemBySku(String sku) {
        log.info("Fetching catalog item by SKU: {}", sku);
        ResponseEntity<CatalogItemDTO> response = catalogDbFeignClient.getItemBySku(sku);
        return response.getBody();
    }

    public List<CatalogItemDTO> getLowStockItems() {
        log.info("Fetching low stock catalog items");
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.getLowStockItems();
        return response.getBody();
    }

    public List<CatalogItemDTO> getItemsByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Fetching catalog items by price range: {} - {}", minPrice, maxPrice);
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.getItemsByPriceRange(minPrice, maxPrice);
        return response.getBody();
    }    
    
    public List<CatalogItemDTO> searchItems(String name, String category, String brand, Boolean active, Double minPrice, Double maxPrice) {
        log.info("Searching catalog items with criteria - name: {}, category: {}, brand: {}, active: {}, price: {}-{}", 
                name, category, brand, active, minPrice, maxPrice);
        ResponseEntity<List<CatalogItemDTO>> response = catalogDbFeignClient.searchItems(name, category, brand, active, minPrice, maxPrice);
        return response.getBody();
    }

    public List<CatalogItemDTO> searchItems(String name, String category, String brand, Double minPrice, Double maxPrice) {
        return searchItems(name, category, brand, null, minPrice, maxPrice);
    }

    public CatalogItemDTO createItem(CatalogItemDTO catalogItemDTO) {
        log.info("Creating new catalog item: {}", catalogItemDTO.getName());
        if (catalogItemDTO.getPrice() != null && catalogItemDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        
        if (catalogItemDTO.getStockQuantity() != null && catalogItemDTO.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        ResponseEntity<CatalogItemDTO> response = catalogDbFeignClient.createItem(catalogItemDTO);
        return response.getBody();
    }    
    
    public CatalogItemDTO updateItem(Long id, CatalogItemDTO catalogItemDTO) {
        log.info("Updating catalog item with ID: {}", id);
        
        if (catalogItemDTO.getPrice() != null && catalogItemDTO.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        ResponseEntity<CatalogItemDTO> response = catalogDbFeignClient.updateItem(id, catalogItemDTO);
        return response.getBody();
    }

    public CatalogItemDTO updateStock(Long id, Integer stockQuantity) {
        log.info("Updating stock for catalog item with ID: {} to quantity: {}", id, stockQuantity);
        
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }

        ResponseEntity<CatalogItemDTO> response = catalogDbFeignClient.updateStock(id, stockQuantity);
        return response.getBody();
    }

    public boolean deleteItem(Long id) {
        log.info("Deleting catalog item with ID: {}", id);
        ResponseEntity<Void> response = catalogDbFeignClient.deleteItem(id);
        return response.getStatusCode().is2xxSuccessful();
    }
}


