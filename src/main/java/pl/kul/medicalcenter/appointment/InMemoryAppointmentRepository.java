package pl.kul.medicalcenter.appointment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryAppointmentRepository implements AppointmentRepository {
    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<Long, Appointment> appointments = new ConcurrentHashMap<>();

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }

        Long id = appointment.getId();
        Appointment appointmentToSave = id == null ? appointment.withId(nextId.getAndIncrement()) : appointment;
        appointments.put(appointmentToSave.getId(), appointmentToSave);
        return appointmentToSave;
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(appointments.get(id));
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> allAppointments = new ArrayList<>(appointments.values());
        allAppointments.sort(Comparator.comparing(Appointment::getDateTime).thenComparing(Appointment::getId));
        return allAppointments;
    }
}
