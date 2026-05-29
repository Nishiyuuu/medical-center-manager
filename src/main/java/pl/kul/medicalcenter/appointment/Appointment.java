package pl.kul.medicalcenter.appointment;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Appointment {
    private final Long id;
    private final Long patientId;
    private final Long doctorId;
    private final LocalDateTime dateTime;
    private final AppointmentStatus status;
    private final String description;

    public Appointment(
            Long id,
            Long patientId,
            Long doctorId,
            LocalDateTime dateTime,
            AppointmentStatus status,
            String description
    ) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.status = status == null ? AppointmentStatus.PLANNED : status;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Appointment withId(Long newId) {
        return new Appointment(newId, patientId, doctorId, dateTime, status, description);
    }

    public Appointment withStatus(AppointmentStatus newStatus) {
        return new Appointment(id, patientId, doctorId, dateTime, newStatus, description);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Appointment appointment)) {
            return false;
        }
        return Objects.equals(id, appointment.id)
                && Objects.equals(patientId, appointment.patientId)
                && Objects.equals(doctorId, appointment.doctorId)
                && Objects.equals(dateTime, appointment.dateTime)
                && status == appointment.status
                && Objects.equals(description, appointment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, doctorId, dateTime, status, description);
    }

    @Override
    public String toString() {
        return "Appointment{"
                + "id=" + id
                + ", patientId=" + patientId
                + ", doctorId=" + doctorId
                + ", dateTime=" + dateTime
                + ", status=" + status
                + ", description='" + description + '\''
                + '}';
    }
}
