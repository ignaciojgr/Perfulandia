package com.perfunlandia.ms_auth_bs.controller;


import com.perfunlandia.ms_auth_bs.dto.CredentialDTO;
import com.perfunlandia.ms_auth_bs.dto.LoginRequestDTO;
import com.perfunlandia.ms_auth_bs.dto.RegisterRequestDTO;
import com.perfunlandia.ms_auth_bs.dto.TokenResponseDTO;
import com.perfunlandia.ms_auth_bs.dto.UpdateRoleRequestDTO;
import com.perfunlandia.ms_auth_bs.dto.UserCreatedResponseDTO;
import com.perfunlandia.ms_auth_bs.dto.ValidationResponseDTO;
import com.perfunlandia.ms_auth_bs.service.AuthService;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/internal/bs/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResponseDTO> authenticate(@RequestBody LoginRequestDTO loginRequest) {
        try {
            TokenResponseDTO tokenResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreatedResponseDTO>registerUser(@RequestBody RegisterRequestDTO registerRequest){

        UserCreatedResponseDTO newUser = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }
    
    @PostMapping("/validate-token")
    public ResponseEntity<ValidationResponseDTO> validateToken(@RequestBody String token) {
        ValidationResponseDTO validationResponse = authService.validateToken(token);
        if (!validationResponse.isValid()) {
            // Si el token no es válido, devolvemos un 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validationResponse);
        }
        return ResponseEntity.ok(validationResponse);
    }

    @PutMapping("/users/{userId}/role")
    public ResponseEntity<CredentialDTO> updateUserRole(
            @PathVariable String userId,
            @RequestBody UpdateRoleRequestDTO request){
        
        CredentialDTO updatedUser = authService.updateUserRole(userId, request);
        return ResponseEntity.ok(updatedUser);
                
        }
    
    
    
    


}
