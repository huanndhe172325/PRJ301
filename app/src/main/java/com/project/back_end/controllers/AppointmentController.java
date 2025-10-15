package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.Service;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // 1. Mark as REST controller
@RequestMapping("/appointments") // Base path for appointment operations
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Service service;

    // 2. Constructor injection
    //@Autowired
    public AppointmentController(AppointmentService appointmentService, Service service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }

    // 3. Get appointments for doctor by date and optional patient name
    @GetMapping("/{token}/{date}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String token,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String patientName
    ) {
        if (!service.validateToken(token, "doctor")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        Long doctorId = service.tokenService.extractDoctorIdFromToken(token); // optional utility
        if (doctorId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Doctor ID missing or invalid.");
        }

        List<Appointment> appointments = appointmentService.getAppointmentsForDoctorOnDate(doctorId, date, patientName);
        return ResponseEntity.ok(appointments);
    }

    // 4. Book a new appointment
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(@PathVariable String token, @RequestBody Appointment appointment) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        int validationCode = service.validateAppointment(appointment.getDoctor().getId(), appointment.getAppointmentTime().toLocalDate(), appointment.getAppointmentTime().toLocalTime());

        if (validationCode == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doctor not found.");
        } else if (validationCode == 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Appointment slot is not available.");
        }

        int result = appointmentService.bookAppointment(appointment);
        return result == 1
                ? ResponseEntity.status(HttpStatus.CREATED).body("Appointment booked successfully.")
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book appointment.");
    }

    // 5. Update existing appointment
    @PutMapping("/update/{token}/{appointmentId}/{patientId}")
    public ResponseEntity<?> updateAppointment(@PathVariable String token,
                                               @PathVariable Long appointmentId,
                                               @PathVariable Long patientId,
                                               @RequestBody Appointment updatedAppointment) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        String result = appointmentService.updateAppointment(appointmentId, updatedAppointment, patientId);

        return result.equals("Appointment updated successfully")
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // 6. Cancel an appointment
    @DeleteMapping("/cancel/{token}/{appointmentId}/{patientId}")
    public ResponseEntity<?> cancelAppointment(@PathVariable String token,
                                               @PathVariable Long appointmentId,
                                               @PathVariable Long patientId) {
        if (!service.validateToken(token, "patient")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }

        String result = appointmentService.cancelAppointment(appointmentId, patientId);
        return result.equals("Appointment canceled successfully")
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}

