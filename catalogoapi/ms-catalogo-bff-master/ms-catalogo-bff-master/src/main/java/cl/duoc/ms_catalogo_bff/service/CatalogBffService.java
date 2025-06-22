package cl.duoc.ms_catalogo_bff.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.ms_catalogo_bff.clients.CatalogoBsFeignClient;
import cl.duoc.ms_catalogo_bff.model.dto.CatalogItemDTO;
import java.util.List;

@Service
public class CatalogBffService {

    @Autowired
    CatalogoBsFeignClient catalogoBsFeignClient;

    public List<CatalogItemDTO> getAllItems() {
        return catalogoBsFeignClient.getAllItems().getBody();
    }

    public CatalogItemDTO getItemById(Long id) {
        return catalogoBsFeignClient.getItemById(id).getBody();
    }

    public CatalogItemDTO createItem(CatalogItemDTO catalogItemDTO) {
        return catalogoBsFeignClient.createItem(catalogItemDTO).getBody();
    }

    public CatalogItemDTO updateItem(Long id, CatalogItemDTO catalogItemDTO) {
        return catalogoBsFeignClient.updateItem(id, catalogItemDTO).getBody();
    }

    public boolean deleteItem(Long id) {
        try {
            catalogoBsFeignClient.deleteItem(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<CatalogItemDTO> searchItems(String name, String category, String brand, Double minPrice, Double maxPrice) {
        return catalogoBsFeignClient.searchItems(name, category, brand, minPrice, maxPrice).getBody();
    }

    public List<CatalogItemDTO> getItemsByCategory(String category) {
        return catalogoBsFeignClient.getItemsByCategory(category).getBody();
    }

    public List<CatalogItemDTO> getItemsByBrand(String brand) {
        return catalogoBsFeignClient.getItemsByBrand(brand).getBody();
    }

    public List<CatalogItemDTO> getItemsByPriceRange(Double minPrice, Double maxPrice) {
        return catalogoBsFeignClient.getItemsByPriceRange(minPrice, maxPrice).getBody();
    }
}

