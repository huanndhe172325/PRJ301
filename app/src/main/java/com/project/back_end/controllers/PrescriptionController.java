package com.project.back_end.controllers;

import com.project.back_end.models.Prescription;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller for managing prescriptions.
 * <p>
 * This controller provides endpoints for doctors to:
 * <ul>
 *   <li>Save a prescription for a given appointment.</li>
 *   <li>Retrieve a prescription by appointment ID.</li>
 * </ul>
 * All actions require a valid JWT token associated with a doctor.
 */
@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final Service service;

    /**
     * Constructor-based dependency injection.
     *
     * @param prescriptionService handles prescription logic
     * @param appointmentService handles appointment status updates
     * @param service handles token validation
     */
    public PrescriptionController(PrescriptionService prescriptionService,
                                  AppointmentService appointmentService,
                                  Service service) {
        this.prescriptionService = prescriptionService;
        this.appointmentService = appointmentService;
        this.service = service;
    }

    /**
     * Save a new prescription for an appointment.
     * Requires a valid doctor token.
     *
     * @param prescription the prescription details
     * @param token        JWT token for doctor validation
     * @return HTTP 200 with saved prescription or HTTP 401 if unauthorized
     */
    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescription(@Valid @RequestBody Prescription prescription,
                                              @PathVariable String token) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");
        }

        // Update appointment status to 'Completed' (e.g., 1)
        appointmentService.changeAppointmentStatus(prescription.getAppointmentId(), 1);

        return prescriptionService.savePrescription(prescription);
    }

    /**
     * Retrieve a prescription by appointment ID.
     * Requires a valid doctor token.
     *
     * @param appointmentId appointment identifier
     * @param token         JWT token for doctor validation
     * @return HTTP 200 with prescription or HTTP 401 if unauthorized
     */
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(@PathVariable Long appointmentId,
                                             @PathVariable String token) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired token.");
        }

        return prescriptionService.getPrescription(appointmentId);
    }
}
