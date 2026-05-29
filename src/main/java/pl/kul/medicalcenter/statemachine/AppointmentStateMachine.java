package pl.kul.medicalcenter.statemachine;

import pl.kul.medicalcenter.appointment.Appointment;
import pl.kul.medicalcenter.appointment.AppointmentStatus;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class AppointmentStateMachine {
    private static final Map<AppointmentStatus, Set<AppointmentStatus>> ALLOWED_TRANSITIONS = Map.of(
            AppointmentStatus.PLANNED, Set.of(AppointmentStatus.CONFIRMED, AppointmentStatus.CANCELLED),
            AppointmentStatus.CONFIRMED, Set.of(AppointmentStatus.IN_PROGRESS, AppointmentStatus.CANCELLED),
            AppointmentStatus.IN_PROGRESS, Set.of(AppointmentStatus.COMPLETED),
            AppointmentStatus.COMPLETED, Set.of(),
            AppointmentStatus.CANCELLED, Set.of()
    );

    public Appointment changeStatus(Appointment appointment, AppointmentStatus newStatus) {
        Objects.requireNonNull(appointment, "appointment cannot be null");
        validateTransition(appointment.getStatus(), newStatus);
        return appointment.withStatus(newStatus);
    }

    public void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        Objects.requireNonNull(currentStatus, "currentStatus cannot be null");
        Objects.requireNonNull(newStatus, "newStatus cannot be null");

        if (!isTransitionAllowed(currentStatus, newStatus)) {
            throw new InvalidStatusTransitionException(currentStatus, newStatus);
        }
    }

    public boolean isTransitionAllowed(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        if (currentStatus == null || newStatus == null) {
            return false;
        }
        return ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of()).contains(newStatus);
    }
}
