package cl.duoc.ms_catalogo_db.service;

import cl.duoc.ms_catalogo_db.model.dto.CatalogItemDTO;
import cl.duoc.ms_catalogo_db.model.entity.CatalogItem;
import cl.duoc.ms_catalogo_db.model.repository.CatalogItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CatalogItemService {

    @Autowired
    CatalogItemRepository catalogItemRepository;

    public List<CatalogItemDTO> getAllItems() {
        log.info("Fetching all catalog items");
        List<CatalogItem> items = catalogItemRepository.findAll();
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public CatalogItemDTO getItemById(Integer id) {
        log.info("Fetching catalog item with ID: {}", id);
        Optional<CatalogItem> item = catalogItemRepository.findById(id);
        if (item.isPresent()) {
            return convertToDTO(item.get());
        }
        return null;
    }

    public List<CatalogItemDTO> getActiveItems() {
        log.info("Fetching active catalog items");
        List<CatalogItem> items = catalogItemRepository.findByIsActiveTrue();
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public List<CatalogItemDTO> searchByName(String name) {
        log.info("Searching catalog items by name: {}", name);
        List<CatalogItem> items = catalogItemRepository.findByNameContainingIgnoreCase(name);
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public List<CatalogItemDTO> getItemsByCategory(String categoryName) {
        log.info("Fetching catalog items by category: {}", categoryName);
        List<CatalogItem> items = catalogItemRepository.findByCategoryNameIgnoreCase(categoryName);
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public List<CatalogItemDTO> getItemsByBrand(String brandName) {
        log.info("Fetching catalog items by brand: {}", brandName);
        List<CatalogItem> items = catalogItemRepository.findByBrandNameIgnoreCase(brandName);
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public List<CatalogItemDTO> getItemsByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Fetching catalog items by price range: {} - {}", minPrice, maxPrice);
        List<CatalogItem> items = catalogItemRepository.findByPriceBetween(minPrice, maxPrice);
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public CatalogItemDTO getItemBySku(String sku) {
        log.info("Fetching catalog item by SKU: {}", sku);
        CatalogItem item = catalogItemRepository.findBySku(sku);
        if (item != null) {
            return convertToDTO(item);
        }
        return null;
    }

    public List<CatalogItemDTO> getLowStockItems() {
        log.info("Fetching low stock catalog items");
        List<CatalogItem> items = catalogItemRepository.findLowStockItems();
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public List<CatalogItemDTO> searchByCriteria(String name, String categoryName, String brandName, 
                                                Boolean isActive, Double minPrice, Double maxPrice) {
        log.info("Searching catalog items by criteria - name: {}, category: {}, brand: {}, active: {}, price: {}-{}", 
                name, categoryName, brandName, isActive, minPrice, maxPrice);
        List<CatalogItem> items = catalogItemRepository.findByCriteria(name, categoryName, brandName, isActive, minPrice, maxPrice);
        List<CatalogItemDTO> itemDTOs = new ArrayList<>();
        
        for (CatalogItem item : items) {
            itemDTOs.add(convertToDTO(item));
        }
        
        return itemDTOs;
    }

    public CatalogItemDTO createItem(CatalogItemDTO catalogItemDTO) {
        log.info("Creating new catalog item: {}", catalogItemDTO.getName());
        CatalogItem catalogItem = convertToEntity(catalogItemDTO);
        CatalogItem savedItem = catalogItemRepository.save(catalogItem);
        return convertToDTO(savedItem);
    }

    public CatalogItemDTO updateItem(Integer id, CatalogItemDTO catalogItemDTO) {
        log.info("Updating catalog item with ID: {}", id);
        Optional<CatalogItem> existingItemOpt = catalogItemRepository.findById(id);
        if (existingItemOpt.isPresent()) {
            CatalogItem existingItem = existingItemOpt.get();
            updateEntityFromDTO(existingItem, catalogItemDTO);
            CatalogItem savedItem = catalogItemRepository.save(existingItem);
            return convertToDTO(savedItem);
        }
        return null;
    }

    public boolean deleteItem(Integer id) {
        log.info("Deleting catalog item with ID: {}", id);
        if (catalogItemRepository.existsById(id)) {
            catalogItemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public CatalogItemDTO updateStock(Integer id, Integer stockQuantity) {
        log.info("Updating stock for catalog item with ID: {} to quantity: {}", id, stockQuantity);
        Optional<CatalogItem> itemOpt = catalogItemRepository.findById(id);
        if (itemOpt.isPresent()) {
            CatalogItem item = itemOpt.get();
            item.setStockQuantity(stockQuantity);
            CatalogItem savedItem = catalogItemRepository.save(item);
            return convertToDTO(savedItem);
        }
        return null;
    }

    // Conversion methods
    private CatalogItemDTO convertToDTO(CatalogItem entity) {
        CatalogItemDTO dto = new CatalogItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setSku(entity.getSku());
        dto.setCategoryName(entity.getCategoryName());
        dto.setCategoryDescription(entity.getCategoryDescription());
        dto.setBrandName(entity.getBrandName());
        dto.setBrandDescription(entity.getBrandDescription());
        dto.setPrimaryImageUrl(entity.getPrimaryImageUrl());
        dto.setImageUrlsFromString(entity.getImageUrls());
        dto.setVariantNamesFromString(entity.getVariantNames());
        dto.setVariantPricesFromString(entity.getVariantPrices());
        dto.setIsActive(entity.getIsActive());
        dto.setStockQuantity(entity.getStockQuantity());
        dto.setMinimumStockLevel(entity.getMinimumStockLevel());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private CatalogItem convertToEntity(CatalogItemDTO dto) {
        CatalogItem entity = new CatalogItem();
        updateEntityFromDTO(entity, dto);
        return entity;
    }

    private void updateEntityFromDTO(CatalogItem entity, CatalogItemDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setSku(dto.getSku());
        entity.setCategoryName(dto.getCategoryName());
        entity.setCategoryDescription(dto.getCategoryDescription());
        entity.setBrandName(dto.getBrandName());
        entity.setBrandDescription(dto.getBrandDescription());
        entity.setPrimaryImageUrl(dto.getPrimaryImageUrl());
        entity.setImageUrls(dto.getImageUrlsAsString());
        entity.setVariantNames(dto.getVariantNamesAsString());
        entity.setVariantPrices(dto.getVariantPricesAsString());
        entity.setIsActive(dto.getIsActive());
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setMinimumStockLevel(dto.getMinimumStockLevel());
    }
}
