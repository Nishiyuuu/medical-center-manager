package pl.kul.medicalcenter.statemachine;

import pl.kul.medicalcenter.appointment.AppointmentStatus;

public final class InvalidStatusTransitionException extends RuntimeException {
    private final AppointmentStatus currentStatus;
    private final AppointmentStatus targetStatus;

    public InvalidStatusTransitionException(AppointmentStatus currentStatus, AppointmentStatus targetStatus) {
        super("Invalid appointment status transition: " + currentStatus + " -> " + targetStatus);
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }

    public AppointmentStatus getCurrentStatus() {
        return currentStatus;
    }

    public AppointmentStatus getTargetStatus() {
        return targetStatus;
    }
}
