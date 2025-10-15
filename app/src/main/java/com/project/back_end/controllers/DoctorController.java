package com.project.back_end.controllers;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.Service;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // 1. REST API Controller
@RequestMapping("${api.path}doctor") // e.g., /api/doctor
public class DoctorController {

    private final DoctorService doctorService;
    private final Service service;

    // 2. Constructor injection
    //@Autowired
    public DoctorController(DoctorService doctorService, Service service) {
        this.doctorService = doctorService;
        this.service = service;
    }

    // 3. Get doctor availability
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable String token
    ) {
        if (!service.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        List<?> availableSlots = doctorService.getDoctorAvailability(doctorId, Date.valueOf(date));
        return ResponseEntity.ok(Map.of("availableSlots", availableSlots));
    }

    // 4. Get all doctors
    @GetMapping("/all")
    public ResponseEntity<?> getDoctor() {
        List<Doctor> doctors = doctorService.getDoctors();
        return ResponseEntity.ok(Map.of("doctors", doctors));
    }

    // 5. Save a new doctor (admin-only)
    @PostMapping("/register/{token}")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor, @PathVariable String token) {
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        int result = doctorService.saveDoctor(doctor);
        return switch (result) {
            case -1 -> ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor with email already exists.");
            case 1 -> ResponseEntity.status(HttpStatus.CREATED).body("Doctor registered successfully.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while registering doctor.");
        };
    }

    // 6. Doctor login
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(@RequestBody Login login) {
        return ResponseEntity.ok(doctorService.validateDoctor(login.getEmail(), login.getPassword()));
    }

    // 7. Update doctor (admin-only)
    @PutMapping("/update/{token}/{doctorId}")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor updatedDoctor,
                                          @PathVariable String token,
                                          @PathVariable Long doctorId) {
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        int result = doctorService.updateDoctor(doctorId, updatedDoctor);
        return switch (result) {
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
            case 1 -> ResponseEntity.ok("Doctor updated successfully.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while updating doctor.");
        };
    }

    // 8. Delete doctor (admin-only)
    @DeleteMapping("/delete/{token}/{doctorId}")
    public ResponseEntity<?> deleteDoctor(@PathVariable String token, @PathVariable Long doctorId) {
        if (!service.validateToken(token, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        int result = doctorService.deleteDoctor(doctorId);
        return switch (result) {
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
            case 1 -> ResponseEntity.ok("Doctor and associated appointments deleted successfully.");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting doctor.");
        };
    }

    // 9. Filter doctors (by name, time, specialty)
    @GetMapping("/filter")
    public ResponseEntity<?> filterDoctor(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String speciality
    ) {
        List<Doctor> doctors = service.filterDoctor(name, speciality, time);
        return ResponseEntity.ok(Map.of("doctors", doctors));
    }
}
