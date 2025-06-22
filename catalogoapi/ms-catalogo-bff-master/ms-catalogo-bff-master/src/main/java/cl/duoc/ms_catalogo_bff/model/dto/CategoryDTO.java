package cl.duoc.ms_catalogo_bff.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDTO {
    @JsonProperty("categoria_id")
    private Integer id;
    @JsonProperty("categoria_nombre")
    private String name;
    @JsonProperty("categoria_descripcion")
    private String description;
    @JsonProperty("categoria_padre_id")
    private Integer parentId;
}

