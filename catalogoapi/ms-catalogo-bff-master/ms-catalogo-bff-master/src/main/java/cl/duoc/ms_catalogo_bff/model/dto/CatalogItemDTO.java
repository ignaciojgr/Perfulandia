package cl.duoc.ms_catalogo_bff.model.dto;

import java.time.OffsetDateTime;

public class CatalogItemDTO {
    private Long id;
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
    private java.util.List<String> imageUrls;
    
    // Variants
    private java.util.List<String> variantNames;
    private java.util.List<Double> variantPrices;
    
    // Status and inventory
    private Boolean isActive;
    private Integer stockQuantity;
    private Integer minimumStockLevel;
    
    // Timestamps
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Helper fields for string representations (if needed for compatibility)
    private String variantNamesAsString;
    private String variantPricesAsString;
    private String imageUrlsAsString;

    // Constructors
    public CatalogItemDTO() {}

    public CatalogItemDTO(Long id, String name, String description, Double price, String sku,
                         String categoryName, String categoryDescription, String brandName, String brandDescription,
                         String primaryImageUrl, java.util.List<String> imageUrls, java.util.List<String> variantNames, java.util.List<Double> variantPrices,
                         Boolean isActive, Integer stockQuantity, Integer minimumStockLevel,
                         OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.brandName = brandName;
        this.brandDescription = brandDescription;
        this.primaryImageUrl = primaryImageUrl;
        this.imageUrls = imageUrls;
        this.variantNames = variantNames;
        this.variantPrices = variantPrices;
        this.isActive = isActive;
        this.stockQuantity = stockQuantity;
        this.minimumStockLevel = minimumStockLevel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryDescription() { return categoryDescription; }
    public void setCategoryDescription(String categoryDescription) { this.categoryDescription = categoryDescription; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getBrandDescription() { return brandDescription; }
    public void setBrandDescription(String brandDescription) { this.brandDescription = brandDescription; }

    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }

    public java.util.List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(java.util.List<String> imageUrls) { this.imageUrls = imageUrls; }

    public java.util.List<String> getVariantNames() { return variantNames; }
    public void setVariantNames(java.util.List<String> variantNames) { this.variantNames = variantNames; }

    public java.util.List<Double> getVariantPrices() { return variantPrices; }
    public void setVariantPrices(java.util.List<Double> variantPrices) { this.variantPrices = variantPrices; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    public Integer getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(Integer minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getVariantNamesAsString() { return variantNamesAsString; }
    public void setVariantNamesAsString(String variantNamesAsString) { this.variantNamesAsString = variantNamesAsString; }

    public String getVariantPricesAsString() { return variantPricesAsString; }
    public void setVariantPricesAsString(String variantPricesAsString) { this.variantPricesAsString = variantPricesAsString; }

    public String getImageUrlsAsString() { return imageUrlsAsString; }
    public void setImageUrlsAsString(String imageUrlsAsString) { this.imageUrlsAsString = imageUrlsAsString; }

    @Override
    public String toString() {
        return "CatalogItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", brandName='" + brandName + '\'' +
                ", primaryImageUrl='" + primaryImageUrl + '\'' +
                ", isActive=" + isActive +
                ", stockQuantity=" + stockQuantity +
                ", minimumStockLevel=" + minimumStockLevel +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
