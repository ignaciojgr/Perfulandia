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
public class BrandDTO {

    @JsonProperty(value = "marca_id")
    private Integer id;
    @JsonProperty(value = "marca_nombre")
    private String name;
    @JsonProperty(value = "marca_descripcion")
    private String description;
}

