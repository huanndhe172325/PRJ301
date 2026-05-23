package com.project.back_end.services;

import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    private static final DateTimeFormatter SLOT_PARSE_FORMATTER =
            DateTimeFormatter.ofPattern("H:mm");

    private static final DateTimeFormatter SLOT_OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    /**
     * Get available appointment slots of a doctor on a specific date.
     *
     * @param doctorId the doctor's ID
     * @param date     the selected appointment date
     * @return list of available time slots in HH:mm format
     */
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, Date date) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Doctor not found with ID: " + doctorId
                ));

        LocalDate selectedDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDateTime startOfDay = selectedDate.atStartOfDay();
        LocalDateTime endOfDay = selectedDate.plusDays(1)
                .atStartOfDay()
                .minusNanos(1);

        List<Appointment> bookedAppointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        startOfDay,
                        endOfDay
                );

        Set<String> bookedSlots = bookedAppointments.stream()
                .filter(Objects::nonNull)
                .map(Appointment::getAppointmentTime)
                .filter(Objects::nonNull)
                .map(appointmentTime ->
                        appointmentTime.toLocalTime().format(SLOT_OUTPUT_FORMATTER)
                )
                .collect(Collectors.toSet());

        List<String> allSlots = Optional.ofNullable(doctor.getAvailableTimes())
                .orElse(Collections.emptyList());

        return allSlots.stream()
                .filter(Objects::nonNull)
                .map(this::normalizeTimeSlot)
                .filter(slot -> !bookedSlots.contains(slot))
                .distinct()
                .sorted(Comparator.comparing(
                        slot -> LocalTime.parse(slot, SLOT_OUTPUT_FORMATTER)
                ))
                .collect(Collectors.toList());
    }

    /**
     * Save a new doctor.
     *
     * @param doctor doctor information
     * @return 1 if saved successfully, -1 if email already exists, 0 if failed
     */
    @Transactional
    public int saveDoctor(Doctor doctor) {
        if (doctor == null) {
            return 0;
        }

        if (doctor.getEmail() == null || doctor.getEmail().isBlank()) {
            return 0;
        }

        Doctor existingDoctor = doctorRepository.findByEmail(doctor.getEmail().trim());
        if (existingDoctor != null) {
            return -1;
        }

        try {
            doctor.setEmail(doctor.getEmail().trim());
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Update doctor information.
     *
     * @param id      doctor ID
     * @param updated updated doctor information
     * @return 1 if updated successfully, -1 if doctor not found, 0 if failed
     */
    @Transactional
    public int updateDoctor(Long id, Doctor updated) {
        if (id == null || updated == null) {
            return 0;
        }

        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
        if (optionalDoctor.isEmpty()) {
            return -1;
        }

        try {
            Doctor doctor = optionalDoctor.get();

            doctor.setName(updated.getName());
            doctor.setEmail(updated.getEmail());
            doctor.setPhone(updated.getPhone());
            doctor.setSpecialty(updated.getSpecialty());
            doctor.setAvailableTimes(updated.getAvailableTimes());

            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get all doctors.
     *
     * @return list of all doctors
     */
    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    /**
     * Delete a doctor and all appointments related to that doctor.
     *
     * @param id doctor ID
     * @return 1 if deleted successfully, -1 if doctor not found, 0 if failed
     */
    @Transactional
    public int deleteDoctor(Long id) {
        if (id == null) {
            return 0;
        }

        if (!doctorRepository.existsById(id)) {
            return -1;
        }

        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Validate doctor login credentials.
     *
     * @param email    doctor's email
     * @param password doctor's password
     * @return structured response containing status, message, and token if login succeeds
     */
    @Transactional
    public ResponseEntity<Map<String, Object>> validateDoctor(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            response.put("status", "error");
            response.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }

        Doctor doctor = doctorRepository.findByEmail(email.trim());

        if (doctor == null || !Objects.equals(doctor.getPassword(), password)) {
            response.put("status", "error");
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = tokenService.generateToken(String.valueOf(doctor.getId()));

        response.put("status", "success");
        response.put("message", "Login successful");
        response.put("token", token);
        response.put("doctorId", doctor.getId());
        response.put("email", doctor.getEmail());
        response.put("name", doctor.getName());
        response.put("specialty", doctor.getSpecialty());

        return ResponseEntity.ok(response);
    }

    /**
     * Find doctors by name.
     *
     * @param name doctor's name or part of name
     * @return matching doctors
     */
    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        if (name == null || name.isBlank()) {
            return Collections.emptyList();
        }

        return doctorRepository.findByNameLike("%" + name.trim() + "%");
    }

    /**
     * Filter doctors by name, specialty, and AM/PM availability.
     */
    @Transactional
    public List<Doctor> filterDoctorsByNameSpecialtyAndTime(String name,
                                                            String specialty,
                                                            String timePeriod) {
        if (name == null || specialty == null || timePeriod == null) {
            return Collections.emptyList();
        }

        List<Doctor> doctors =
                doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name.trim(),
                        specialty.trim()
                );

        return filterDoctorsByTime(doctors, timePeriod);
    }

    /**
     * Filter a given list of doctors by AM or PM availability.
     *
     * @param doctors    list of doctors
     * @param timePeriod AM or PM
     * @return doctors who have available slots in the selected time period
     */
    public List<Doctor> filterDoctorsByTime(List<Doctor> doctors, String timePeriod) {
        if (doctors == null || doctors.isEmpty()) {
            return Collections.emptyList();
        }

        validateTimePeriod(timePeriod);

        return doctors.stream()
                .filter(Objects::nonNull)
                .filter(doctor -> doctor.getAvailableTimes() != null)
                .filter(doctor -> doctor.getAvailableTimes().stream()
                        .filter(Objects::nonNull)
                        .anyMatch(timeStr -> isTimeInPeriod(timeStr, timePeriod)))
                .collect(Collectors.toList());
    }

    /**
     * Filter doctors by name and AM/PM availability.
     */
    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(String name, String timePeriod) {
        if (name == null || name.isBlank()) {
            return Collections.emptyList();
        }

        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name.trim() + "%");
        return filterDoctorsByTime(doctors, timePeriod);
    }

    /**
     * Filter doctors by name and specialty.
     */
    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecialty(String name, String specialty) {
        if (name == null || name.isBlank() || specialty == null || specialty.isBlank()) {
            return Collections.emptyList();
        }

        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                name.trim(),
                specialty.trim()
        );
    }

    /**
     * Filter doctors by specialty and AM/PM availability.
     */
    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecialty(String specialty, String timePeriod) {
        if (specialty == null || specialty.isBlank()) {
            return Collections.emptyList();
        }

        List<Doctor> doctors =
                doctorRepository.findBySpecialtyIgnoreCase(specialty.trim());

        return filterDoctorsByTime(doctors, timePeriod);
    }

    /**
     * Filter doctors by specialty.
     */
    @Transactional
    public List<Doctor> filterDoctorBySpecialty(String specialty) {
        if (specialty == null || specialty.isBlank()) {
            return Collections.emptyList();
        }

        return doctorRepository.findBySpecialtyIgnoreCase(specialty.trim());
    }

    /**
     * Filter all doctors by AM/PM availability.
     */
    @Transactional
    public List<Doctor> filterDoctorsByTime(String timePeriod) {
        validateTimePeriod(timePeriod);

        List<Doctor> allDoctors = doctorRepository.findAll();
        return filterDoctorsByTime(allDoctors, timePeriod);
    }

    /**
     * Normalize time slot into HH:mm format.
     *
     * Example:
     * 9:00  -> 09:00
     * 09:00 -> 09:00
     */
    private String normalizeTimeSlot(String slot) {
        if (slot == null || slot.isBlank()) {
            throw new IllegalArgumentException("Time slot cannot be null or blank");
        }

        try {
            return LocalTime.parse(slot.trim(), SLOT_PARSE_FORMATTER)
                    .format(SLOT_OUTPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid time slot format. Expected format is HH:mm",
                    e
            );
        }
    }

    /**
     * Check whether a time slot belongs to AM or PM.
     */
    private boolean isTimeInPeriod(String timeStr, String timePeriod) {
        try {
            LocalTime time = LocalTime.parse(
                    normalizeTimeSlot(timeStr),
                    SLOT_OUTPUT_FORMATTER
            );

            if (timePeriod.equalsIgnoreCase("AM")) {
                return time.isBefore(LocalTime.NOON);
            }

            return !time.isBefore(LocalTime.NOON);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validate time period input.
     */
    private void validateTimePeriod(String timePeriod) {
        if (timePeriod == null ||
                (!timePeriod.equalsIgnoreCase("AM") &&
                 !timePeriod.equalsIgnoreCase("PM"))) {
            throw new IllegalArgumentException("Time period must be AM or PM");
        }
    }
}
