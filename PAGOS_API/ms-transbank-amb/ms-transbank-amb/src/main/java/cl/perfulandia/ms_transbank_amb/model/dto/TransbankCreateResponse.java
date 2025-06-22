package cl.perfulandia.ms_transbank_amb.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransbankCreateResponse {
    
    private String token;
    private String url;
}