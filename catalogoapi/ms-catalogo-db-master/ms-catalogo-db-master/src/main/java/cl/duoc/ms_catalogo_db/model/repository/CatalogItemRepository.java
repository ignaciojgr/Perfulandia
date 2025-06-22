package cl.duoc.ms_catalogo_db.model.repository;

import cl.duoc.ms_catalogo_db.model.entity.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItem, Integer> {
    
    // Find by name
    List<CatalogItem> findByNameContainingIgnoreCase(String name);
    
    // Find by category
    List<CatalogItem> findByCategoryNameIgnoreCase(String categoryName);
    
    // Find by brand
    List<CatalogItem> findByBrandNameIgnoreCase(String brandName);
    
    // Find active items
    List<CatalogItem> findByIsActiveTrue();
    
    // Find by price range
    List<CatalogItem> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // Find by SKU
    CatalogItem findBySku(String sku);
    
    // Find items with low stock
    @Query("SELECT c FROM CatalogItem c WHERE c.stockQuantity <= c.minimumStockLevel")
    List<CatalogItem> findLowStockItems();
    
    // Search by multiple criteria
    @Query("SELECT c FROM CatalogItem c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:categoryName IS NULL OR LOWER(c.categoryName) = LOWER(:categoryName)) AND " +
           "(:brandName IS NULL OR LOWER(c.brandName) = LOWER(:brandName)) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive) AND " +
           "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR c.price <= :maxPrice)")
    List<CatalogItem> findByCriteria(@Param("name") String name,
                                   @Param("categoryName") String categoryName,
                                   @Param("brandName") String brandName,
                                   @Param("isActive") Boolean isActive,
                                   @Param("minPrice") Double minPrice,
                                   @Param("maxPrice") Double maxPrice);
}
