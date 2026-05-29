package pl.kul.medicalcenter.doctor;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public final class DoctorService {
    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = Objects.requireNonNull(doctorRepository, "doctorRepository cannot be null");
    }

    public Doctor addDoctor(String firstName, String lastName, String specialization) {
        Doctor doctor = new Doctor(null, firstName, lastName, specialization);
        validate(doctor);
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Doctor doctor) {
        Objects.requireNonNull(doctor, "doctor cannot be null");
        if (doctor.getId() == null) {
            throw new IllegalArgumentException("Doctor id cannot be empty");
        }
        validate(doctor);
        if (doctorRepository.findById(doctor.getId()).isEmpty()) {
            throw new IllegalArgumentException("Doctor does not exist: " + doctor.getId());
        }
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id) {
        requireId(id);
        doctorRepository.deleteById(id);
    }

    public Optional<Doctor> findDoctorById(Long id) {
        requireId(id);
        return doctorRepository.findById(id);
    }

    public List<Doctor> listAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        String normalizedSpecialization = normalize(specialization);
        if (normalizedSpecialization.isEmpty()) {
            return listAllDoctors();
        }

        return doctorRepository.findAll().stream()
                .filter(doctor -> contains(doctor.getSpecialization(), normalizedSpecialization))
                .toList();
    }

    private void validate(Doctor doctor) {
        requireNotBlank(doctor.getFirstName(), "First name cannot be empty");
        requireNotBlank(doctor.getLastName(), "Last name cannot be empty");
        requireNotBlank(doctor.getSpecialization(), "Specialization cannot be empty");
    }

    private void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Doctor id cannot be empty");
        }
    }

    private boolean contains(String value, String normalizedQuery) {
        return normalize(value).contains(normalizedQuery);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
