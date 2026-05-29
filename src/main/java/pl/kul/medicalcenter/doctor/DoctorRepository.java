package pl.kul.medicalcenter.doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository {
    Doctor save(Doctor doctor);

    void deleteById(Long id);

    Optional<Doctor> findById(Long id);

    List<Doctor> findAll();
}
