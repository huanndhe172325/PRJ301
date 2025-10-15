package com.project.back_end.services;

import com.project.back_end.models.Appointment;
//import com.project.back_end.models.Doctor;
//import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Service // 1. Mark this as a Spring-managed service component
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    @SuppressWarnings("unused")
    private final DoctorRepository doctorRepository;
    @SuppressWarnings("unused")
    private final PatientRepository patientRepository;

    // 2. Constructor injection
    
    //@Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorRepository doctorRepository,
                              PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // 4. Book appointment
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 5. Update appointment
    @Transactional
    public String updateAppointment(Long appointmentId, Appointment updatedAppointment, Long patientId) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment existing = optional.get();
        if (!existing.getPatient().getId().equals(patientId)) {
            return "Unauthorized access";
        }

        LocalDateTime newTime = updatedAppointment.getAppointmentTime();
        Long doctorId = updatedAppointment.getDoctor().getId();

        // Check for time conflict
        List<Appointment> conflicts = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId,
                newTime.minusMinutes(59),
                newTime.plusMinutes(59)
        );

        if (!conflicts.isEmpty()) return "Doctor is not available at the selected time";

        // Update and save
        existing.setDoctor(updatedAppointment.getDoctor());
        existing.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existing.setStatus(updatedAppointment.getStatus());

        appointmentRepository.save(existing);
        return "Appointment updated successfully";
    }

    // 6. Cancel appointment
    @Transactional
    public String cancelAppointment(Long appointmentId, Long patientId) {
        Optional<Appointment> optional = appointmentRepository.findById(appointmentId);
        if (optional.isEmpty()) return "Appointment not found";

        Appointment appointment = optional.get();
        if (!appointment.getPatient().getId().equals(patientId)) {
            return "Unauthorized cancellation";
        }

        appointmentRepository.delete(appointment);
        return "Appointment canceled successfully";
    }

    // 7. Get appointments for a doctor (optional filter by patient name)
    @Transactional
    public List<Appointment> getAppointmentsForDoctorOnDate(Long doctorId, LocalDate date, String patientName) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        if (patientName != null && !patientName.isEmpty()) {
            return appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, start, end
            );
        } else {
            return appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId, start, end
            );
        }
    }

    // 8. Change status of appointment
    @Transactional
    public void changeAppointmentStatus(Long appointmentId, int status) {
        appointmentRepository.updateStatus(status, appointmentId);
    }
}
