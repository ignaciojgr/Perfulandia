package com.perfunlandia.ms_auth_bs.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CredentialDTOResponse {
    private long id;
    private String email;
    private String password;
    private String userId;
    private String role;

}
