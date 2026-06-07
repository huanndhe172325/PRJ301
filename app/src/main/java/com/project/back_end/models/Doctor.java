package com.project.back_end.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
/**
 * Represents a doctor in the healthcare management system.
 *
 * This entity stores doctor information including personal details,
 * contact information, specialty, and available appointment times.
 */
@Entity
@Table(name = "doctors")
public class Doctor {

    /**
     * Unique identifier for the doctor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the doctor.
     */
    @NotNull(message = "Doctor name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false)
    private String name;

    /**
     * Medical specialty of the doctor.
     */
    @NotNull(message = "Specialty is required")
    @Size(min = 3, max = 50, message = "Specialty must be between 3 and 50 characters")
    @Column(nullable = false)
    private String specialty;

    /**
     * Email address used for doctor authentication and communication.
     */
    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Doctor account password.
     * Write-only to prevent exposure in API responses.
     */
    @NotNull(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String password;

    /**
     * Contact phone number.
     */
    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    @Column(nullable = false, unique = true)
    private String phone;

    /**
     * List of available appointment time slots.
     */
    @ElementCollection
    @CollectionTable(
        name = "doctor_available_times",
        joinColumns = @JoinColumn(name = "doctor_id")
    )
    @Column(name = "time_slot")
    private List<String> availableTimes;

    /**
     * Default constructor required by JPA.
     */
    public Doctor() {
    }

    /**
     * Creates a doctor with all required information.
     *
     * @param name Doctor's full name
     * @param specialty Medical specialty
     * @param email Email address
     * @param password Account password
     * @param phone Phone number
     * @param availableTimes Available appointment time slots
     */
    public Doctor(String name, String specialty, String email,
                  String password, String phone,
                  List<String> availableTimes) {
        this.name = name;
        this.specialty = specialty;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.availableTimes = availableTimes;
    }

    /**
     * Returns the doctor ID.
     *
     * @return doctor identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns available appointment times.
     *
     * @return list of available time slots
     */
    public List<String> getAvailableTimes() {
        return availableTimes;
    }

    /**
     * Updates available appointment times.
     *
     * @param availableTimes new list of available time slots
     */
    public void setAvailableTimes(List<String> availableTimes) {
        this.availableTimes = availableTimes;
    }
}



