package com.project.back_end.mvc;

import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    public boolean validateToken(String token, String role) {
        // Dummy validation logic for demonstration
        // In production, you'd query a user/token database or cache
        if ("admin".equals(role) && "admin-token".equals(token)) {
            return true;
        }
        if ("doctor".equals(role) && "doctor-token".equals(token)) {
            return true;
        }
        return false;
    }
}
