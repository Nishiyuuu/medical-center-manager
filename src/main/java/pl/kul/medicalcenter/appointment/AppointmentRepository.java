package pl.kul.medicalcenter.appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Long id);

    List<Appointment> findAll();
}
