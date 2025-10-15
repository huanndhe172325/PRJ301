package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.services.TokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service // 1. Mark as a Spring Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    // 2. Constructor Injection
    //@Autowired
    public DoctorService(DoctorRepository doctorRepository,
                         AppointmentRepository appointmentRepository,
                         TokenService tokenService) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    // 3. Get availability of doctor for a specific date
    @SuppressWarnings("unlikely-arg-type")
    @Transactional
    public List<String> getDoctorAvailability(Long doctorId, Date date) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) return Collections.emptyList();

        Doctor doctor = optionalDoctor.get();
        List<String> allSlots = doctor.getAvailableTimes(); // assume Set<LocalTime>

        List<Appointment> bookedAppointments = appointmentRepository
                .findByDoctorIdAndAppointmentTimeBetween(
                        doctorId,
                        new java.sql.Timestamp(date.getTime()).toLocalDateTime().withHour(0).withMinute(0),
                        new java.sql.Timestamp(date.getTime()).toLocalDateTime().withHour(23).withMinute(59)
                );

        Set<LocalTime> bookedSlots = bookedAppointments.stream()
                .map(appt -> appt.getAppointmentTime().toLocalTime())
                .collect(Collectors.toSet());

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .sorted()
                .collect(Collectors.toList());
    }

    // 5. Save doctor
    @Transactional
    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) {
            return -1; // Conflict
        }
        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 6. Update doctor
    @Transactional
    public int updateDoctor(Long id, Doctor updated) {
        Optional<Doctor> optional = doctorRepository.findById(id);
        if (optional.isEmpty()) return -1;

        Doctor doctor = optional.get();
        doctor.setName(updated.getName());
        doctor.setEmail(updated.getEmail());
        doctor.setPhone(updated.getPhone());
        doctor.setSpecialty(updated.getSpecialty());
        doctor.setAvailableTimes(updated.getAvailableTimes());

        doctorRepository.save(doctor);
        return 1;
    }

    // 7. Get all doctors
    @Transactional
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    // 8. Delete doctor and their appointments
    @Transactional
    public int deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) return -1;
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // 9. Validate doctor credentials
    @Transactional
    public String validateDoctor(String email, String password) {
        Doctor doctor = doctorRepository.findByEmail(email);
        if (doctor == null || !doctor.getPassword().equals(password)) {
            return "Invalid email or password";
        }
        return tokenService.generateToken(String.valueOf(doctor.getId()));
    }

    // 10. Find doctors by name
    @Transactional
    public List<Doctor> findDoctorByName(String name) {
        return doctorRepository.findByNameLike("%" + name + "%");
    }

    // 11. Filter by name, specialty, and time
    @Transactional
    public List<Doctor> filterDoctorsByNameSpecialtyAndTime(String name, String specialty, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
        return filterDoctorsByTime(doctors, timePeriod);
    }

    // 12. Filter a list of doctors by AM/PM availability
    public List<Doctor> filterDoctorsByTime(List<Doctor> doctors, String timePeriod) {
        return doctors.stream().filter(doctor ->
                doctor.getAvailableTimes().stream().anyMatch(timeStr -> {
                    LocalTime time = LocalTime.parse(timeStr);
                    return timePeriod.equalsIgnoreCase("AM") ? time.isBefore(LocalTime.NOON)
                            : time.isAfter(LocalTime.NOON);
                })
        ).collect(Collectors.toList());
    }

    // 13. Filter by name and time
    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(String name, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findByNameLike("%" + name + "%");
        return filterDoctorsByTime(doctors, timePeriod);
    }

    // 14. Filter by name and specialty
    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecialty(String name, String specialty) {
        return doctorRepository.findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(name, specialty);
    }

    // 15. Filter by specialty and time
    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecialty(String specialty, String timePeriod) {
        List<Doctor> doctors = doctorRepository.findBySpecialtyIgnoreCase(specialty);
        return filterDoctorsByTime(doctors, timePeriod);
    }

    // 16. Filter by specialty
    @Transactional
    public List<Doctor> filterDoctorBySpecialty(String specialty) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty);
    }

    // 17. Filter all doctors by time availability
    @Transactional
    public List<Doctor> filterDoctorsByTime(String timePeriod) {
        List<Doctor> allDoctors = doctorRepository.findAll();
        return filterDoctorsByTime(allDoctors, timePeriod);
    }
}

