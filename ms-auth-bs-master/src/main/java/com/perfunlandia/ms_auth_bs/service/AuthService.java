package com.perfunlandia.ms_auth_bs.service;

import com.perfunlandia.ms_auth_bs.client.AuthDbClient;
import com.perfunlandia.ms_auth_bs.dto.CreateCredentialDTORequest;
import com.perfunlandia.ms_auth_bs.dto.CredentialDTO;
import com.perfunlandia.ms_auth_bs.dto.CredentialDTOResponse;
import com.perfunlandia.ms_auth_bs.dto.LoginRequestDTO;
import com.perfunlandia.ms_auth_bs.dto.RegisterRequestDTO;
import com.perfunlandia.ms_auth_bs.dto.TokenResponseDTO;
import com.perfunlandia.ms_auth_bs.dto.UpdateRoleRequestDTO;
import com.perfunlandia.ms_auth_bs.dto.UserCreatedResponseDTO;
import com.perfunlandia.ms_auth_bs.dto.ValidationResponseDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Service
public class AuthService {


    @Autowired
    private AuthDbClient authDbClient;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.ms}")
    private long expirationTimeMs;

    private Key key;

    @PostConstruct
    public void initKey(){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public UserCreatedResponseDTO register(RegisterRequestDTO registerRequestDTO){

        CreateCredentialDTORequest requestForDb = new CreateCredentialDTORequest(
            registerRequestDTO.getEmail(),
            registerRequestDTO.getPassword()
        );

        ResponseEntity<CredentialDTOResponse> responseFromDb = authDbClient.createCredential(requestForDb);

        if (!responseFromDb.getStatusCode().is2xxSuccessful() || responseFromDb.getBody()==null){
            throw new RuntimeException("Fallo en el servicio de base de datos.");
        }

        CredentialDTOResponse createdCredential = responseFromDb.getBody();


        return new UserCreatedResponseDTO(
            createdCredential.getUserId(),
            createdCredential.getEmail(),
            "Usuario registrado correctamente."


        );
    }

    public TokenResponseDTO authenticate(LoginRequestDTO loginRequest){
        ResponseEntity<CredentialDTO> response = authDbClient.getCredentialByEmail(loginRequest.getEmail());

        if(!response.getStatusCode().is2xxSuccessful()|| response.getBody()==null){
            throw new RuntimeException("credenciales invalidas");
        }

        CredentialDTO credential = response.getBody();

        if(passwordEncoder.matches(loginRequest.getPassword(), credential.getPassword())){
            String token = generateToken(credential.getEmail(), credential.getUserId(), credential.getRole());
            return new TokenResponseDTO(token);
        }else{  
            throw new RuntimeException("credenciales invalidas");
        }
    }

    private String generateToken(String email, String userId, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("isGuest", "GUEST".equals(role));

        Date now = new Date();
            long expiration = "GUEST".equals(role) ? 24 * 60 * 60 * 1000 : expirationTimeMs;
            Date expirationDate = new Date(now.getTime() + expiration);
        

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public TokenResponseDTO generateGuestToken(){
        String guestId = generateGuestId();
        String token = generateToken("guest@system.local", guestId, "GUEST");
        return new TokenResponseDTO(token);

    }

    private String generateGuestId() {
    return "GUEST-" + System.currentTimeMillis() + "-" + 
           java.util.UUID.randomUUID().toString().substring(0, 8);
}

    public ValidationResponseDTO validateToken(String token){
        System.out.println("\n----[ms-auth-bs] Iniciando validacion de token ");
        System.out.println("Token Recibido:"+ token);
        try {
            Claims claims =Jwts.parser()
                    .verifyWith((SecretKey)key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            String email = claims.getSubject();
            String userId = claims.get("userId",String.class);
            String role = claims.get("role",String.class);

            System.out.println("VALIDACION EXITOSA para el userId:" + userId+ "Con email:"+ email +" con rol:"+role);

            return new ValidationResponseDTO(true, userId, email, role);


        }catch(Exception e){
            System.err.println("!!!!Error al validar el token!!!!");
            e.printStackTrace();
            return new ValidationResponseDTO(false, null, null,null);
        }
    }

    public CredentialDTO updateUserRole(String userId, UpdateRoleRequestDTO request){
        ResponseEntity<CredentialDTO> response = authDbClient.updateUserRole(userId, request);
        if(!response.getStatusCode().is2xxSuccessful()|| response.getBody()==null){
            throw new RuntimeException("Error al actualizar el rol en el servicio de DB.");

        }
        return response.getBody();
    }

}
