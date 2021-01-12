package Hardeng.Rest.services;

import org.springframework.http.ResponseEntity;

public interface LogoutService {
    
    ResponseEntity<Void> logout(String token);
}