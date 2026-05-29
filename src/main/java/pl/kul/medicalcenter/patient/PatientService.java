package pl.kul.medicalcenter.patient;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public final class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = Objects.requireNonNull(patientRepository, "patientRepository cannot be null");
    }

    public Patient addPatient(String firstName, String lastName, String phoneNumber, String email) {
        Patient patient = new Patient(null, firstName, lastName, phoneNumber, email);
        validate(patient);
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Patient patient) {
        Objects.requireNonNull(patient, "patient cannot be null");
        if (patient.getId() == null) {
            throw new IllegalArgumentException("Patient id cannot be empty");
        }
        validate(patient);
        if (patientRepository.findById(patient.getId()).isEmpty()) {
            throw new IllegalArgumentException("Patient does not exist: " + patient.getId());
        }
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        requireId(id);
        patientRepository.deleteById(id);
    }

    public Optional<Patient> findPatientById(Long id) {
        requireId(id);
        return patientRepository.findById(id);
    }

    public List<Patient> searchPatients(String query) {
        String normalizedQuery = normalize(query);
        if (normalizedQuery.isEmpty()) {
            return listAllPatients();
        }

        return patientRepository.findAll().stream()
                .filter(patient -> contains(patient.getFirstName(), normalizedQuery)
                        || contains(patient.getLastName(), normalizedQuery)
                        || contains(fullName(patient), normalizedQuery)
                        || contains(patient.getPhoneNumber(), normalizedQuery))
                .toList();
    }

    public List<Patient> listAllPatients() {
        return patientRepository.findAll();
    }

    private void validate(Patient patient) {
        requireNotBlank(patient.getFirstName(), "First name cannot be empty");
        requireNotBlank(patient.getLastName(), "Last name cannot be empty");
        requireNotBlank(patient.getPhoneNumber(), "Phone number cannot be empty");
    }

    private void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Patient id cannot be empty");
        }
    }

    private String fullName(Patient patient) {
        return patient.getFirstName() + " " + patient.getLastName();
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
