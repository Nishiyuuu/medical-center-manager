package pl.kul.medicalcenter.patient;

import java.util.List;
import java.util.Optional;

public interface PatientRepository {
    Patient save(Patient patient);

    void deleteById(Long id);

    Optional<Patient> findById(Long id);

    List<Patient> findAll();
}
