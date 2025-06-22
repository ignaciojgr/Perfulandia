package cl.duoc.ms_catalogo_db.model.dto;

import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogItemDTO {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String sku;
    
    // Category Information
    private String categoryName;
    private String categoryDescription;
    
    // Brand Information
    private String brandName;
    private String brandDescription;
    
    // Images
    private String primaryImageUrl;
    private List<String> imageUrls;
    
    // Variants
    private List<String> variantNames;
    private List<Double> variantPrices;
    
    // Status and inventory
    private Boolean isActive;
    private Integer stockQuantity;
    private Integer minimumStockLevel;
    
    // Timestamps
    private Date createdAt;
    private Date updatedAt;

    // Helper methods to convert between string representations and lists
    public void setImageUrlsFromString(String imageUrls) {
        if (imageUrls != null && !imageUrls.trim().isEmpty()) {
            this.imageUrls = Arrays.asList(imageUrls.split(","));
        }
    }

    public String getImageUrlsAsString() {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            return String.join(",", imageUrls);
        }
        return null;
    }

    public void setVariantNamesFromString(String variantNames) {
        if (variantNames != null && !variantNames.trim().isEmpty()) {
            this.variantNames = Arrays.asList(variantNames.split(","));
        }
    }

    public String getVariantNamesAsString() {
        if (variantNames != null && !variantNames.isEmpty()) {
            return String.join(",", variantNames);
        }
        return null;
    }

    public void setVariantPricesFromString(String variantPrices) {
        if (variantPrices != null && !variantPrices.trim().isEmpty()) {
            this.variantPrices = Arrays.stream(variantPrices.split(","))
                    .map(Double::parseDouble)
                    .toList();
        }
    }

    public String getVariantPricesAsString() {
        if (variantPrices != null && !variantPrices.isEmpty()) {
            return variantPrices.stream()
                    .map(String::valueOf)
                    .reduce((a, b) -> a + "," + b)
                    .orElse(null);
        }
        return null;
    }
}
