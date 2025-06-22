package cl.duoc.ms_catalogo_bff.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {
    @JsonProperty("producto_id")
    private Integer id;
    @JsonProperty("producto_nombre")
    private String name;
    @JsonProperty("producto_descripcion")
    private String description;
    @JsonProperty("producto_precio")
    private Double price;
    @JsonProperty("producto_imagen_url")
    private String imageUrl;
    @JsonProperty("producto_activo")
    private Boolean isActive;
    @JsonProperty("producto_marca_id")
    private Integer brandId;
}
