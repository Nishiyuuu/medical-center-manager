package pl.kul.medicalcenter.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = Objects.requireNonNull(
                appointmentRepository,
                "appointmentRepository cannot be null"
        );
    }

    public Appointment createAppointment(
            Long patientId,
            Long doctorId,
            LocalDateTime dateTime,
            String description
    ) {
        Appointment appointment = new Appointment(
                null,
                patientId,
                doctorId,
                dateTime,
                AppointmentStatus.PLANNED,
                description
        );
        validate(appointment);
        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(Long id) {
        requireId(id);
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment does not exist: " + id));

        return appointmentRepository.save(appointment.withStatus(AppointmentStatus.CANCELLED));
    }

    public Optional<Appointment> findAppointmentById(Long id) {
        requireId(id);
        return appointmentRepository.findById(id);
    }

    public List<Appointment> listAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> listAppointmentsByDoctorId(Long doctorId) {
        requireDoctorId(doctorId);
        return appointmentRepository.findAll().stream()
                .filter(appointment -> doctorId.equals(appointment.getDoctorId()))
                .toList();
    }

    public List<Appointment> listAppointmentsByPatientId(Long patientId) {
        requirePatientId(patientId);
        return appointmentRepository.findAll().stream()
                .filter(appointment -> patientId.equals(appointment.getPatientId()))
                .toList();
    }

    public List<Appointment> listAppointmentsByDate(LocalDate date) {
        Objects.requireNonNull(date, "date cannot be null");
        return appointmentRepository.findAll().stream()
                .filter(appointment -> date.equals(appointment.getDateTime().toLocalDate()))
                .toList();
    }

    private void validate(Appointment appointment) {
        requirePatientId(appointment.getPatientId());
        requireDoctorId(appointment.getDoctorId());
        Objects.requireNonNull(appointment.getDateTime(), "dateTime cannot be null");
        Objects.requireNonNull(appointment.getStatus(), "status cannot be null");
    }

    private void requireId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Appointment id cannot be empty");
        }
    }

    private void requirePatientId(Long patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient id cannot be empty");
        }
    }

    private void requireDoctorId(Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor id cannot be empty");
        }
    }
}
