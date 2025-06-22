package cl.duoc.ms_catalogo_bs.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    @JsonProperty(value = "producto_id")
    private Integer id;
    @JsonProperty(value = "producto_nombre")
    private String name;
    @JsonProperty(value = "producto_descripcion")
    private String description;
    @JsonProperty(value = "producto_precio")
    private Double price;
    @JsonProperty(value = "producto_imagen_url")
    private String imageUrl;
    @JsonProperty(value = "producto_activo")
    private Boolean isActive;
    @JsonProperty(value = "producto_marca_id")
    private Integer brandId;
}

