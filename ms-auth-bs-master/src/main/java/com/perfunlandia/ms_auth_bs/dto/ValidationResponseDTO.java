package com.perfunlandia.ms_auth_bs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ValidationResponseDTO {
    private boolean valid;
    private String userId;
    private String email;
    private String role;

}
