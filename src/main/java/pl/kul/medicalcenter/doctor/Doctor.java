package pl.kul.medicalcenter.doctor;

import java.util.Objects;

public final class Doctor {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String specialization;

    public Doctor(Long id, String firstName, String lastName, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public Doctor withId(Long newId) {
        return new Doctor(newId, firstName, lastName, specialization);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Doctor doctor)) {
            return false;
        }
        return Objects.equals(id, doctor.id)
                && Objects.equals(firstName, doctor.firstName)
                && Objects.equals(lastName, doctor.lastName)
                && Objects.equals(specialization, doctor.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, specialization);
    }

    @Override
    public String toString() {
        return "Doctor{"
                + "id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", specialization='" + specialization + '\''
                + '}';
    }
}
