package com.perfunlandia.ms_auth_bs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class CreateCredentialDTORequest {

    private String email;
    private String password;

}
