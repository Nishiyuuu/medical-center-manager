package pl.kul.medicalcenter.patient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryPatientRepository implements PatientRepository {
    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<Long, Patient> patients = new ConcurrentHashMap<>();

    @Override
    public Patient save(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        Long id = patient.getId();
        Patient patientToSave = id == null ? patient.withId(nextId.getAndIncrement()) : patient;
        patients.put(patientToSave.getId(), patientToSave);
        return patientToSave;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        patients.remove(id);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(patients.get(id));
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> allPatients = new ArrayList<>(patients.values());
        allPatients.sort(Comparator.comparing(Patient::getId));
        return allPatients;
    }
}
