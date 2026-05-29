package pl.kul.medicalcenter;

import javafx.application.Application;
import pl.kul.medicalcenter.ui.MedicalCenterManagerUi;

public final class MedicalCenterManagerApplication {
    private MedicalCenterManagerApplication() {
    }

    public static void main(String[] args) {
        Application.launch(MedicalCenterManagerUi.class, args);
    }
}
