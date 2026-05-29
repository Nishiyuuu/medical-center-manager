package pl.kul.medicalcenter.doctor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryDoctorRepository implements DoctorRepository {
    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<Long, Doctor> doctors = new ConcurrentHashMap<>();

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }

        Long id = doctor.getId();
        Doctor doctorToSave = id == null ? doctor.withId(nextId.getAndIncrement()) : doctor;
        doctors.put(doctorToSave.getId(), doctorToSave);
        return doctorToSave;
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            return;
        }

        doctors.remove(id);
    }

    @Override
    public Optional<Doctor> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(doctors.get(id));
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> allDoctors = new ArrayList<>(doctors.values());
        allDoctors.sort(Comparator.comparing(Doctor::getId));
        return allDoctors;
    }
}
