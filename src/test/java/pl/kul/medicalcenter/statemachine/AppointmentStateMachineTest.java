package pl.kul.medicalcenter.statemachine;

import org.junit.jupiter.api.Test;
import pl.kul.medicalcenter.appointment.Appointment;
import pl.kul.medicalcenter.appointment.AppointmentStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppointmentStateMachineTest {
    private final AppointmentStateMachine stateMachine = new AppointmentStateMachine();

    @Test
    void shouldAllowPlannedToConfirmed() {
        Appointment updatedAppointment = stateMachine.changeStatus(
                appointmentWithStatus(AppointmentStatus.PLANNED),
                AppointmentStatus.CONFIRMED
        );

        assertThat(updatedAppointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    void shouldAllowConfirmedToInProgress() {
        Appointment updatedAppointment = stateMachine.changeStatus(
                appointmentWithStatus(AppointmentStatus.CONFIRMED),
                AppointmentStatus.IN_PROGRESS
        );

        assertThat(updatedAppointment.getStatus()).isEqualTo(AppointmentStatus.IN_PROGRESS);
    }

    @Test
    void shouldAllowInProgressToCompleted() {
        Appointment updatedAppointment = stateMachine.changeStatus(
                appointmentWithStatus(AppointmentStatus.IN_PROGRESS),
                AppointmentStatus.COMPLETED
        );

        assertThat(updatedAppointment.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }

    @Test
    void shouldRejectCompletedToPlanned() {
        Appointment appointment = appointmentWithStatus(AppointmentStatus.COMPLETED);

        assertThatThrownBy(() -> stateMachine.changeStatus(appointment, AppointmentStatus.PLANNED))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("COMPLETED -> PLANNED");
    }

    @Test
    void shouldRejectCancelledToConfirmed() {
        Appointment appointment = appointmentWithStatus(AppointmentStatus.CANCELLED);

        assertThatThrownBy(() -> stateMachine.changeStatus(appointment, AppointmentStatus.CONFIRMED))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("CANCELLED -> CONFIRMED");
    }

    private Appointment appointmentWithStatus(AppointmentStatus status) {
        return new Appointment(1L, 10L, 20L, LocalDateTime.of(2026, 5, 29, 9, 0), status, "Visit");
    }
}
