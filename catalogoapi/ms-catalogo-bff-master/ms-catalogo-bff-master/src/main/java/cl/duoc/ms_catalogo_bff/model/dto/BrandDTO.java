package cl.duoc.ms_catalogo_bff.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BrandDTO {
    @JsonProperty("marca_id")
    private Integer id;
    @JsonProperty("marca_nombre")
    private String name;
    @JsonProperty("marca_descripcion")
    private String description;
}
