package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {

    // 1. Unique identifier for the appointment
    private Long id;

    // 2–3. Doctor information
    private Long doctorId;
    private String doctorName;

    // 4–8. Patient information
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String patientAddress;

    // 9. Scheduled appointment time
    private LocalDateTime appointmentTime;

    // 10. Appointment status (e.g., Scheduled = 0, Completed = 1)
    private int status;

    // 11–13. Derived fields (date, time only, and end time)
    private LocalDate appointmentDate;
    private LocalTime appointmentTimeOnly;
    private LocalDateTime endTime;

    // 14. Constructor
    public AppointmentDTO(Long id, Long doctorId, String doctorName,
                          Long patientId, String patientName, String patientEmail,
                          String patientPhone, String patientAddress,
                          LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;

        // Calculate derived fields
        if (appointmentTime != null) {
            this.appointmentDate = appointmentTime.toLocalDate();
            this.appointmentTimeOnly = appointmentTime.toLocalTime();
            this.endTime = appointmentTime.plusHours(1);
        }
    }

    // 15. Getters

    public Long getId() {
        return id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
