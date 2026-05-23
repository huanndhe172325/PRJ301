package com.project.back_end.repo;

import com.project.back_end.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Retrieves a patient by their email address.
     *
     * @param email the patient's email address
     * @return the matching Patient, or null if no patient is found
     */
    Patient findByEmail(String email);

    /**
     * Retrieves a patient by either their email address or phone number.
     *
     * @param email the patient's email address
     * @param phone the patient's phone number
     * @return the matching Patient, or null if no patient is found
     */
    Patient findByEmailOrPhone(String email, String phone);
}
