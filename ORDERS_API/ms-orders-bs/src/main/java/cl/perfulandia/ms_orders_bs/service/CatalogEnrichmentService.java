package cl.perfulandia.ms_orders_bs.service;

import cl.perfulandia.ms_orders_bs.clients.CatalogApiClient;
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
                throw new RuntimeException("Failed to enrich product information for item: " + item.getProductId());               
            }
        }
    }
    private void enrichSingleOrderItem(OrderItemDTO orderItem) {
        Long productId = Long.parseLong(orderItem.getProductId());        
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
            } else {
                throw new RuntimeException("Product price not available in catalog: " + productId);
            }
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
                    return false;
                }
                
                if (!catalogItem.getIsActive()) {
                    return false;
                }
                
                if (catalogItem.getStockQuantity() < item.getQuantity()) {
                    return false;
                }
                
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
