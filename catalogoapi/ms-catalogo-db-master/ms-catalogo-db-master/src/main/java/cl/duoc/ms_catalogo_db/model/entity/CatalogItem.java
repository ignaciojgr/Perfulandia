package cl.duoc.ms_catalogo_db.model.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "catalog_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatalogItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Product Information
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "sku", length = 50)
    private String sku;

    // Category Information
    @Column(name = "category_name", length = 100)
    private String categoryName;

    @Column(name = "category_description", length = 255)
    private String categoryDescription;

    // Brand Information
    @Column(name = "brand_name", length = 100)
    private String brandName;

    @Column(name = "brand_description", length = 255)
    private String brandDescription;

    // Images
    @Column(name = "image_urls", length = 1000)
    private String imageUrls;

    @Column(name = "primary_image_url", length = 255)
    private String primaryImageUrl;

    // Variant Information
    @Column(name = "variant_names", length = 500)
    private String variantNames; // e.g., "Talla S, Talla M, Color Rojo"

    @Column(name = "variant_prices", length = 255)
    private String variantPrices; // e.g., "0.0,5.0,-2.0" (price adjustments)

    // Status and timestamps
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "minimum_stock_level")
    private Integer minimumStockLevel = 0;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
