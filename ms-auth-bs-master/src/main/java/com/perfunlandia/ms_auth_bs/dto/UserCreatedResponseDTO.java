package com.perfunlandia.ms_auth_bs.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserCreatedResponseDTO {
    private String userId;
    private String email;
    private String message;

}
