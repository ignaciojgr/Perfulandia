package cl.duoc.ms_catalogo_bs.model.dto;

import lombok.*;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogItemDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String sku;
    private String categoryName;
    private String categoryDescription;
    private String brandName;
    private String brandDescription;
    private String primaryImageUrl;
    private java.util.List<String> imageUrls;
    private java.util.List<String> variantNames;
    private java.util.List<Double> variantPrices;
    private Boolean isActive;
    private Integer stockQuantity;
    private Integer minimumStockLevel;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String variantNamesAsString;
    private String variantPricesAsString;
    private String imageUrlsAsString;
}
