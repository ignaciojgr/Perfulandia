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
public class CategoryDTO {

    @JsonProperty(value = "categoria_id")
    private Integer id;
    @JsonProperty(value = "categoria_nombre")
    private String name;
    @JsonProperty(value = "categoria_descripcion")
    private String description;
    @JsonProperty(value = "categoria_padre_id")
    private Integer parentId;
}

