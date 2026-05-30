package pl.kul.medicalcenter.doctor;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DoctorServiceTest {
    private final DoctorRepository doctorRepository = mock(DoctorRepository.class);
    private final DoctorService doctorService = new DoctorService(doctorRepository);

    @Test
    void shouldAddDoctor() {
        Doctor savedDoctor = new Doctor(1L, "Jan", "Kowalski", "Cardiology");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        Doctor doctor = doctorService.addDoctor("Jan", "Kowalski", "Cardiology");

        assertThat(doctor).isEqualTo(savedDoctor);
        verify(doctorRepository).save(new Doctor(null, "Jan", "Kowalski", "Cardiology"));
    }

    @Test
    void shouldSearchDoctorBySpecialization() {
        Doctor cardiologist = new Doctor(1L, "Jan", "Kowalski", "Cardiology");
        Doctor neurologist = new Doctor(2L, "Maria", "Zielinska", "Neurology");
        when(doctorRepository.findAll()).thenReturn(List.of(cardiologist, neurologist));

        List<Doctor> doctors = doctorService.searchDoctorsBySpecialization("cardio");

        assertThat(doctors).containsExactly(cardiologist);
    }
}
