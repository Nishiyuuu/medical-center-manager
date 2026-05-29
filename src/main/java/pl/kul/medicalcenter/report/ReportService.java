package pl.kul.medicalcenter.report;

import pl.kul.medicalcenter.appointment.Appointment;
import pl.kul.medicalcenter.appointment.AppointmentRepository;
import pl.kul.medicalcenter.appointment.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public final class ReportService {
    private final AppointmentRepository appointmentRepository;

    public ReportService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = Objects.requireNonNull(
                appointmentRepository,
                "appointmentRepository cannot be null"
        );
    }

    public long countAppointmentsByDate(LocalDate date) {
        Objects.requireNonNull(date, "date cannot be null");
        return appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getDateTime() != null)
                .filter(appointment -> date.equals(appointment.getDateTime().toLocalDate()))
                .count();
    }

    public List<Appointment> listAppointmentsForDoctor(Long doctorId) {
        requireDoctorId(doctorId);
        return appointmentRepository.findAll().stream()
                .filter(appointment -> doctorId.equals(appointment.getDoctorId()))
                .toList();
    }

    public List<Appointment> listCancelledAppointments() {
        return appointmentRepository.findAll().stream()
                .filter(appointment -> AppointmentStatus.CANCELLED == appointment.getStatus())
                .toList();
    }

    public long countAppointmentsByStatus(AppointmentStatus status) {
        Objects.requireNonNull(status, "status cannot be null");
        return appointmentRepository.findAll().stream()
                .filter(appointment -> status == appointment.getStatus())
                .count();
    }

    public long countCompletedAppointments() {
        return countAppointmentsByStatus(AppointmentStatus.COMPLETED);
    }

    private void requireDoctorId(Long doctorId) {
        if (doctorId == null) {
            throw new IllegalArgumentException("Doctor id cannot be empty");
        }
    }
}
