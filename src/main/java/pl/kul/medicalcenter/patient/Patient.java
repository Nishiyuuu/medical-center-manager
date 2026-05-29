package pl.kul.medicalcenter.patient;

import java.util.Objects;

public final class Patient {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;
    private final String email;

    public Patient(Long id, String firstName, String lastName, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Patient withId(Long newId) {
        return new Patient(newId, firstName, lastName, phoneNumber, email);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Patient patient)) {
            return false;
        }
        return Objects.equals(id, patient.id)
                && Objects.equals(firstName, patient.firstName)
                && Objects.equals(lastName, patient.lastName)
                && Objects.equals(phoneNumber, patient.phoneNumber)
                && Objects.equals(email, patient.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phoneNumber, email);
    }

    @Override
    public String toString() {
        return "Patient{"
                + "id=" + id
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", phoneNumber='" + phoneNumber + '\''
                + ", email='" + email + '\''
                + '}';
    }
}
