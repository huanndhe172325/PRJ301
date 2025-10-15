package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service // 1. Marks this as a Spring-managed service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    // 2. Constructor Injection
    //@Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    // 3. Save a new prescription
    public ResponseEntity<?> savePrescription(Prescription prescription) {
        try {
            List<Prescription> existing = prescriptionRepository.findByAppointmentId(prescription.getAppointmentId());

            if (!existing.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Prescription already exists for this appointment.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            prescriptionRepository.save(prescription);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Prescription saved successfully.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to save prescription.");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4. Retrieve prescription by appointment ID
    public ResponseEntity<?> getPrescription(Long appointmentId) {
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByAppointmentId(appointmentId);

            if (prescriptions.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No prescription found for this appointment.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Assuming only one prescription per appointment is allowed
            Prescription prescription = prescriptions.get(0);
            Map<String, Object> response = new HashMap<>();
            response.put("prescription", prescription);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Error retrieving prescription.");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

