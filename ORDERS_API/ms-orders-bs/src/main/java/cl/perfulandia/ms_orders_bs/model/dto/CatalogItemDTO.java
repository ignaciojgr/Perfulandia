package cl.perfulandia.ms_orders_bs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogItemDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String sku;
    
    private String categoryName;
    private String categoryDescription;
    
    private String brandName;
    private String brandDescription;
    
    private String primaryImageUrl;
    private List<String> imageUrls;
    
    private List<String> variantNames;
    private List<Double> variantPrices;
    
    private Boolean isActive;
    private Integer stockQuantity;
    private Integer minimumStockLevel;
    
    private Date createdAt;
    private Date updatedAt;

    public CatalogItemDTO(Integer id, String name, String description, Double price, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
    }
}
