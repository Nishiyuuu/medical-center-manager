package pl.kul.medicalcenter.patient;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PatientServiceTest {
    private final PatientRepository patientRepository = mock(PatientRepository.class);
    private final PatientService patientService = new PatientService(patientRepository);

    @Test
    void shouldAddPatient() {
        Patient savedPatient = new Patient(1L, "Anna", "Nowak", "123456789", "anna@example.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        Patient patient = patientService.addPatient("Anna", "Nowak", "123456789", "anna@example.com");

        assertThat(patient).isEqualTo(savedPatient);
        verify(patientRepository).save(new Patient(null, "Anna", "Nowak", "123456789", "anna@example.com"));
    }

    @Test
    void shouldRejectPatientWithEmptyFirstName() {
        assertThatThrownBy(() -> patientService.addPatient("", "Nowak", "123456789", "anna@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("First name cannot be empty");
    }
}
