package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
//import com.project.back_end.services.TokenService;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

//@Service // 1. Spring-managed service component
public class Service {

    public final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    // 2. Constructor Injection
    //@Autowired
    public Service(TokenService tokenService,
                   AdminRepository adminRepository,
                   DoctorRepository doctorRepository,
                   PatientRepository patientRepository,
                   DoctorService doctorService,
                   PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    // 3. Validate token for a given role
    public boolean validateToken(String token, String role) {
        try {
            return tokenService.validateToken(token, role);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Validate admin login
    public ResponseEntity<?> validateAdmin(String username, String password) {
        try {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null || !admin.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid username or password.");
            }
            String token = tokenService.generateToken(null, "admin", username);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed due to an internal error.");
        }
    }

    // 5. Filter doctors by name, specialty, and time
    public List<Doctor> filterDoctor(String name, String specialty, String time) {
        if (name != null && specialty != null && time != null) {
            return doctorService.filterDoctorsByNameSpecialtyAndTime(name, specialty, time);
        } else if (name != null && specialty != null) {
            return doctorService.filterDoctorByNameAndSpecialty(name, specialty);
        } else if (name != null && time != null) {
            return doctorService.filterDoctorByNameAndTime(name, time);
        } else if (specialty != null && time != null) {
            return doctorService.filterDoctorByTimeAndSpecialty(specialty, time);
        } else if (name != null) {
            return doctorService.findDoctorByName(name);
        } else if (specialty != null) {
            return doctorService.filterDoctorBySpecialty(specialty);
        } else if (time != null) {
            return doctorService.filterDoctorsByTime(time);
        } else {
            return doctorService.getDoctors();
        }
    }

    // 6. Validate doctor appointment slot availability
    @SuppressWarnings("unlikely-arg-type")
    public int validateAppointment(Long doctorId, LocalDate date, LocalTime time) {
        Optional<Doctor> optional = doctorRepository.findById(doctorId);
        if (optional.isEmpty()) return -1;

        List<String> availableSlots = doctorService.getDoctorAvailability(doctorId, java.sql.Date.valueOf(date));
        return availableSlots.contains(time) ? 1 : 0;
    }

    // 7. Validate new patient (check for duplicates)
    public boolean validatePatient(Patient patient) {
        return patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone()) == null;
    }

    // 8. Validate patient login
    public ResponseEntity<?> validatePatientLogin(String email, String password) {
        try {
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null || !patient.getPassword().equals(password)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid email or password.");
            }
            String token = tokenService.generateToken(patient.getId(), "patient", email);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed due to an internal error.");
        }
    }

    // 9. Filter patient's appointment history
    public List<AppointmentDTO> filterPatient(String token, String condition, String doctorName) {
        try {
            String email = tokenService.extractEmailFromToken(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) return List.of();

            Long patientId = patient.getId();

            if (condition != null && doctorName != null) {
                return patientService.filterByDoctorAndCondition(doctorName, patientId, condition);
            } else if (doctorName != null) {
                return patientService.filterByDoctor(doctorName, patientId);
            } else if (condition != null) {
                return patientService.filterByCondition(patientId, condition);
            } else {
                return patientService.getPatientAppointment(patientId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}

