package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.client.CatalogApiClient;
import cl.perfulandia.ms_orders_bs.model.dto.CatalogItemDTO;
import cl.perfulandia.ms_orders_bs.model.dto.OrderItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class CatalogEnrichmentService {
    
    @Autowired
    CatalogApiClient catalogApiClient;

    public void enrichOrderItems(List<OrderItemDTO> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return;
        }
        
        for (OrderItemDTO item : orderItems) {
            try {
                enrichSingleOrderItem(item);
            } catch (Exception e) {
                log.error("Failed to enrich order item with productId {}: {}", item.getProductId(), e.getMessage());
                
            }
        }
    }
    private void enrichSingleOrderItem(OrderItemDTO orderItem) {
        Long productId = Long.parseLong(orderItem.getProductId());
        
        log.info("Fetching catalog information for product ID: {}", productId);
        
        ResponseEntity<CatalogItemDTO> response = catalogApiClient.getItemById(productId);
        CatalogItemDTO catalogItem = response.getBody();
        
        if (catalogItem != null) {
            orderItem.setProductName(catalogItem.getName());
            orderItem.setProductDescription(catalogItem.getDescription());
            orderItem.setProductSku(catalogItem.getSku());
            orderItem.setCategoryName(catalogItem.getCategoryName());
            orderItem.setBrandName(catalogItem.getBrandName());
            orderItem.setPrimaryImageUrl(catalogItem.getPrimaryImageUrl());
            orderItem.setImageUrls(catalogItem.getImageUrls());
            orderItem.setAvailableStock(catalogItem.getStockQuantity());
            orderItem.setIsActive(catalogItem.getIsActive());
            if (catalogItem.getPrice() != null) {
                orderItem.setUnitPrice(BigDecimal.valueOf(catalogItem.getPrice()));
                log.debug("Set unit price from catalog: {} for product {}", 
                         catalogItem.getPrice(), productId);
            } else {
                log.warn("No price available in catalog for product ID: {}", productId);
                throw new RuntimeException("Product price not available in catalog: " + productId);
            }
            
            log.info("Successfully enriched order item: {} - {}", productId, catalogItem.getName());
        } else {
            log.warn("No catalog information found for product ID: {}", productId);
        }
    }

    public boolean validateOrderItems(List<OrderItemDTO> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            return false;
        }
          for (OrderItemDTO item : orderItems) {
            try {
                Long productId = Long.parseLong(item.getProductId());
                ResponseEntity<CatalogItemDTO> response = catalogApiClient.getItemById(productId);
                CatalogItemDTO catalogItem = response.getBody();
                
                if (catalogItem == null) {
                    log.error("Product not found in catalog: {}", productId);
                    return false;
                }
                
                if (!catalogItem.getIsActive()) {
                    log.error("Product is inactive: {}", productId);
                    return false;
                }
                
                if (catalogItem.getStockQuantity() < item.getQuantity()) {
                    log.error("Insufficient stock for product {}: requested={}, available={}", 
                             productId, item.getQuantity(), catalogItem.getStockQuantity());
                    return false;
                }
                
            } catch (Exception e) {
                log.error("Validation failed for product {}: {}", item.getProductId(), e.getMessage());
                return false;
            }
        }
        
        return true;
    }
}
