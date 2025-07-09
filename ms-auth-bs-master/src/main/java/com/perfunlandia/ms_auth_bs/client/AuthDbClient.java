package com.perfunlandia.ms_auth_bs.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.perfunlandia.ms_auth_bs.dto.CreateCredentialDTORequest;
import com.perfunlandia.ms_auth_bs.dto.CredentialDTO;
import com.perfunlandia.ms_auth_bs.dto.CredentialDTOResponse;
import com.perfunlandia.ms_auth_bs.dto.UpdateRoleRequestDTO;



@FeignClient(name= "ms-auth-db", url="http://localhost:8081")

public interface AuthDbClient {

    @GetMapping("/internal/db/auth/credentials/{email}")
    ResponseEntity<CredentialDTO> getCredentialByEmail(@PathVariable("email")String email);

    @PostMapping("/internal/db/auth/credentials")
    ResponseEntity<CredentialDTOResponse> createCredential(@RequestBody CreateCredentialDTORequest request);
    
    @PutMapping("/internal/db/auth/credentials/{userId}/role")
    ResponseEntity<CredentialDTO> updateUserRole(
        @PathVariable("userId") String userId,
        @RequestBody UpdateRoleRequestDTO request);


}
