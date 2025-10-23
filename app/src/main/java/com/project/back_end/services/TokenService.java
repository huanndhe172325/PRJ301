package com.project.back_end.services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public TokenService(AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    /** 
     * Lấy key bí mật dùng để ký token 
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Sinh token JWT dựa trên email của người dùng
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 ngày
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Trích xuất email (subject) từ token
     */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Xác minh và parse token, trả về Claims
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Xác thực token với loại người dùng cụ thể (admin, doctor, patient)
     */
    public boolean validateToken(String token, String userType) {
        try {
            String extractedEmail = extractEmail(token);
            if (extractedEmail == null) return false;

            switch (userType.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(extractedEmail) != null;
                case "doctor":
                    return doctorRepository.findByEmail(extractedEmail) != null;
                case "patient":
                    return patientRepository.findByEmail(extractedEmail) != null;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * ✅ Đã implement — trích xuất email từ token (bản rút gọn)
     */
    public String extractEmailFromToken(String token) {
        return extractEmail(token);
    }

    /**
     * ✅ Đã implement — sinh token có thể thêm thông tin khác (ví dụ role, username)
     */
    public String generateToken(Object object, String role, String username) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("objectType", object.getClass().getSimpleName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * ✅ Đã implement — trích xuất Doctor ID từ token (nếu có)
     */
    public Long extractDoctorIdFromToken(String token) {
        try {
            Claims claims = parseClaims(token);
            if (claims.containsKey("doctorId")) {
                return claims.get("doctorId", Long.class);
            }
            // Nếu không có doctorId trong claims, có thể tra ngược từ email
            String email = claims.getSubject();
            Doctor doctor = doctorRepository.findByEmail(email);
            return doctor != null ? doctor.getDoctorId() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
