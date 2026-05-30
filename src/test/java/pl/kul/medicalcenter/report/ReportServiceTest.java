package pl.kul.medicalcenter.report;

import org.junit.jupiter.api.Test;
import pl.kul.medicalcenter.appointment.Appointment;
import pl.kul.medicalcenter.appointment.AppointmentRepository;
import pl.kul.medicalcenter.appointment.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReportServiceTest {
    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final ReportService reportService = new ReportService(appointmentRepository);

    @Test
    void shouldCountAppointmentsByStatus() {
        when(appointmentRepository.findAll()).thenReturn(List.of(
                appointment(1L, AppointmentStatus.PLANNED),
                appointment(2L, AppointmentStatus.COMPLETED),
                appointment(3L, AppointmentStatus.COMPLETED)
        ));

        long completedCount = reportService.countAppointmentsByStatus(AppointmentStatus.COMPLETED);

        assertThat(completedCount).isEqualTo(2);
    }

    @Test
    void shouldReturnCancelledAppointments() {
        Appointment plannedAppointment = appointment(1L, AppointmentStatus.PLANNED);
        Appointment cancelledAppointment = appointment(2L, AppointmentStatus.CANCELLED);
        when(appointmentRepository.findAll()).thenReturn(List.of(plannedAppointment, cancelledAppointment));

        List<Appointment> cancelledAppointments = reportService.listCancelledAppointments();

        assertThat(cancelledAppointments).containsExactly(cancelledAppointment);
    }

    private Appointment appointment(Long id, AppointmentStatus status) {
        return new Appointment(id, 10L, 20L, LocalDateTime.of(2026, 5, 29, 10, 0), status, "Visit");
    }
}
