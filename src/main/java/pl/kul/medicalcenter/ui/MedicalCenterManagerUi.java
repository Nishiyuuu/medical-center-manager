package pl.kul.medicalcenter.ui;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pl.kul.medicalcenter.appointment.Appointment;
import pl.kul.medicalcenter.appointment.AppointmentRepository;
import pl.kul.medicalcenter.appointment.AppointmentService;
import pl.kul.medicalcenter.appointment.AppointmentStatus;
import pl.kul.medicalcenter.appointment.InMemoryAppointmentRepository;
import pl.kul.medicalcenter.doctor.Doctor;
import pl.kul.medicalcenter.doctor.DoctorService;
import pl.kul.medicalcenter.doctor.InMemoryDoctorRepository;
import pl.kul.medicalcenter.patient.InMemoryPatientRepository;
import pl.kul.medicalcenter.patient.Patient;
import pl.kul.medicalcenter.patient.PatientService;
import pl.kul.medicalcenter.report.ReportService;
import pl.kul.medicalcenter.statemachine.InvalidStatusTransitionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class MedicalCenterManagerUi extends Application {
    private static final String APPLICATION_TITLE = "Medical Center Manager";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

    private final PatientService patientService = new PatientService(new InMemoryPatientRepository());
    private final DoctorService doctorService = new DoctorService(new InMemoryDoctorRepository());
    private final AppointmentRepository appointmentRepository = new InMemoryAppointmentRepository();
    private final AppointmentService appointmentService = new AppointmentService(appointmentRepository);
    private final ReportService reportService = new ReportService(appointmentRepository);

    private final ObservableList<Patient> patientRows = FXCollections.observableArrayList();
    private final ObservableList<Doctor> doctorRows = FXCollections.observableArrayList();
    private final ObservableList<Appointment> appointmentRows = FXCollections.observableArrayList();
    private final ObservableList<Patient> patientOptions = FXCollections.observableArrayList();
    private final ObservableList<Doctor> doctorOptions = FXCollections.observableArrayList();

    private final TableView<Patient> patientTable = new TableView<>(patientRows);
    private final TableView<Doctor> doctorTable = new TableView<>(doctorRows);
    private final TableView<Appointment> appointmentTable = new TableView<>(appointmentRows);

    private final TextField patientSearchField = textField("Search by name or phone number");
    private final TextField doctorSearchField = textField("Search by specialization");
    private final ComboBox<Patient> appointmentPatientField = new ComboBox<>(patientOptions);
    private final ComboBox<Doctor> appointmentDoctorField = new ComboBox<>(doctorOptions);
    private final Map<AppointmentStatus, Label> appointmentStatusLabels = new EnumMap<>(AppointmentStatus.class);
    private final Label cancelledAppointmentsLabel = new Label("0");
    private final Label completedAppointmentsLabel = new Label("0");

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane(
                createPatientsTab(),
                createDoctorsTab(),
                createAppointmentsTab(),
                createReportsTab()
        );
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        VBox root = new VBox(tabPane);
        root.getStyleClass().add("app-root");
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 1100, 720);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle(APPLICATION_TITLE);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();

        refreshAll();
    }

    private Tab createPatientsTab() {
        TextField firstNameField = textField("First name");
        TextField lastNameField = textField("Last name");
        TextField phoneNumberField = textField("Phone number");
        TextField emailField = textField("Email");
        Button addButton = new Button("Add patient");
        addButton.setOnAction(event -> runSafely("Could not add patient", () -> {
            patientService.addPatient(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneNumberField.getText(),
                    emailField.getText()
            );
            clear(firstNameField, lastNameField, phoneNumberField, emailField);
            refreshAll();
        }));

        GridPane form = formGrid();
        addFormRow(form, 0, "First name", firstNameField);
        addFormRow(form, 1, "Last name", lastNameField);
        addFormRow(form, 2, "Phone", phoneNumberField);
        addFormRow(form, 3, "Email", emailField);
        form.add(addButton, 1, 4);

        patientSearchField.textProperty().addListener((observable, oldValue, newValue) -> refreshPatients());
        configurePatientTable();

        VBox content = tabContent(
                sectionTitle("Add patient"),
                form,
                sectionTitle("Patients"),
                patientSearchField,
                patientTable
        );
        VBox.setVgrow(patientTable, Priority.ALWAYS);
        return tab("Patients", content);
    }

    private Tab createDoctorsTab() {
        TextField firstNameField = textField("First name");
        TextField lastNameField = textField("Last name");
        TextField specializationField = textField("Specialization");
        Button addButton = new Button("Add doctor");
        addButton.setOnAction(event -> runSafely("Could not add doctor", () -> {
            doctorService.addDoctor(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    specializationField.getText()
            );
            clear(firstNameField, lastNameField, specializationField);
            refreshAll();
        }));

        GridPane form = formGrid();
        addFormRow(form, 0, "First name", firstNameField);
        addFormRow(form, 1, "Last name", lastNameField);
        addFormRow(form, 2, "Specialization", specializationField);
        form.add(addButton, 1, 3);

        doctorSearchField.textProperty().addListener((observable, oldValue, newValue) -> refreshDoctors());
        configureDoctorTable();

        VBox content = tabContent(
                sectionTitle("Add doctor"),
                form,
                sectionTitle("Doctors"),
                doctorSearchField,
                doctorTable
        );
        VBox.setVgrow(doctorTable, Priority.ALWAYS);
        return tab("Doctors", content);
    }

    private Tab createAppointmentsTab() {
        configurePatientComboBox(appointmentPatientField);
        configureDoctorComboBox(appointmentDoctorField);

        DatePicker dateField = new DatePicker(LocalDate.now());
        TextField timeField = textField("09:00");
        timeField.setText("09:00");
        TextField descriptionField = textField("Description");
        Button createButton = new Button("Create appointment");
        createButton.setOnAction(event -> runSafely("Could not create appointment", () -> {
            Patient patient = requireSelectedPatient();
            Doctor doctor = requireSelectedDoctor();
            LocalDateTime dateTime = readAppointmentDateTime(dateField, timeField);
            appointmentService.createAppointment(
                    patient.getId(),
                    doctor.getId(),
                    dateTime,
                    descriptionField.getText()
            );
            descriptionField.clear();
            refreshAppointments();
            refreshReports();
        }));

        GridPane form = formGrid();
        addFormRow(form, 0, "Patient", appointmentPatientField);
        addFormRow(form, 1, "Doctor", appointmentDoctorField);
        addFormRow(form, 2, "Date", dateField);
        addFormRow(form, 3, "Time", timeField);
        addFormRow(form, 4, "Description", descriptionField);
        form.add(createButton, 1, 5);

        configureAppointmentTable();
        HBox actions = new HBox(
                8,
                statusButton("Confirm", AppointmentStatus.CONFIRMED),
                statusButton("Start", AppointmentStatus.IN_PROGRESS),
                statusButton("Complete", AppointmentStatus.COMPLETED),
                statusButton("Cancel", AppointmentStatus.CANCELLED)
        );
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox content = tabContent(
                sectionTitle("Create appointment"),
                form,
                sectionTitle("Appointments"),
                appointmentTable,
                actions
        );
        VBox.setVgrow(appointmentTable, Priority.ALWAYS);
        return tab("Appointments", content);
    }

    private Tab createReportsTab() {
        GridPane statusGrid = formGrid();
        int row = 0;
        for (AppointmentStatus status : AppointmentStatus.values()) {
            Label valueLabel = new Label("0");
            appointmentStatusLabels.put(status, valueLabel);
            statusGrid.add(new Label(status.name()), 0, row);
            statusGrid.add(valueLabel, 1, row);
            row++;
        }

        GridPane summaryGrid = formGrid();
        summaryGrid.add(new Label("Cancelled appointments"), 0, 0);
        summaryGrid.add(cancelledAppointmentsLabel, 1, 0);
        summaryGrid.add(new Label("Completed appointments"), 0, 1);
        summaryGrid.add(completedAppointmentsLabel, 1, 1);

        Button refreshButton = new Button("Refresh reports");
        refreshButton.setOnAction(event -> refreshReports());

        VBox content = tabContent(
                sectionTitle("Appointment count by status"),
                statusGrid,
                sectionTitle("Summary"),
                summaryGrid,
                refreshButton
        );
        return tab("Reports", content);
    }

    private void configurePatientTable() {
        setColumns(patientTable, List.<TableColumn<Patient, ?>>of(
                column("ID", Patient::getId),
                column("First name", Patient::getFirstName),
                column("Last name", Patient::getLastName),
                column("Phone", Patient::getPhoneNumber),
                column("Email", Patient::getEmail)
        ));
        patientTable.setPlaceholder(new Label("No patients"));
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void configureDoctorTable() {
        setColumns(doctorTable, List.<TableColumn<Doctor, ?>>of(
                column("ID", Doctor::getId),
                column("First name", Doctor::getFirstName),
                column("Last name", Doctor::getLastName),
                column("Specialization", Doctor::getSpecialization)
        ));
        doctorTable.setPlaceholder(new Label("No doctors"));
        doctorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void configureAppointmentTable() {
        setColumns(appointmentTable, List.<TableColumn<Appointment, ?>>of(
                column("ID", Appointment::getId),
                column("Patient", appointment -> formatPatient(appointment.getPatientId())),
                column("Doctor", appointment -> formatDoctor(appointment.getDoctorId())),
                column("Date and time", appointment -> formatDateTime(appointment.getDateTime())),
                column("Status", appointment -> appointment.getStatus().name()),
                column("Description", Appointment::getDescription)
        ));
        appointmentTable.setPlaceholder(new Label("No appointments"));
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private Button statusButton(String text, AppointmentStatus status) {
        Button button = new Button(text);
        button.setOnAction(event -> changeSelectedAppointmentStatus(status));
        return button;
    }

    private void changeSelectedAppointmentStatus(AppointmentStatus status) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            showError("No appointment selected", "Select an appointment first.");
            return;
        }

        runSafely("Could not change appointment status", () -> {
            Appointment updatedAppointment = appointmentService.changeStatus(selectedAppointment.getId(), status);
            refreshAppointments();
            refreshReports();
            selectAppointment(updatedAppointment.getId());
        });
    }

    private void refreshAll() {
        refreshPatients();
        refreshDoctors();
        refreshAppointments();
        refreshReports();
    }

    private void refreshPatients() {
        List<Patient> allPatients = patientService.listAllPatients();
        patientOptions.setAll(allPatients);
        patientRows.setAll(patientService.searchPatients(patientSearchField.getText()));
    }

    private void refreshDoctors() {
        List<Doctor> allDoctors = doctorService.listAllDoctors();
        doctorOptions.setAll(allDoctors);
        doctorRows.setAll(doctorService.searchDoctorsBySpecialization(doctorSearchField.getText()));
    }

    private void refreshAppointments() {
        appointmentRows.setAll(appointmentService.listAllAppointments());
    }

    private void refreshReports() {
        for (AppointmentStatus status : AppointmentStatus.values()) {
            Label label = appointmentStatusLabels.get(status);
            if (label != null) {
                label.setText(String.valueOf(reportService.countAppointmentsByStatus(status)));
            }
        }
        cancelledAppointmentsLabel.setText(String.valueOf(reportService.listCancelledAppointments().size()));
        completedAppointmentsLabel.setText(String.valueOf(reportService.countCompletedAppointments()));
    }

    private Patient requireSelectedPatient() {
        Patient patient = appointmentPatientField.getValue();
        if (patient == null) {
            throw new IllegalArgumentException("Choose a patient");
        }
        return patient;
    }

    private Doctor requireSelectedDoctor() {
        Doctor doctor = appointmentDoctorField.getValue();
        if (doctor == null) {
            throw new IllegalArgumentException("Choose a doctor");
        }
        return doctor;
    }

    private LocalDateTime readAppointmentDateTime(DatePicker dateField, TextField timeField) {
        LocalDate date = dateField.getValue();
        if (date == null) {
            throw new IllegalArgumentException("Choose appointment date");
        }
        LocalTime time = LocalTime.parse(timeField.getText().trim(), TIME_FORMATTER);
        return LocalDateTime.of(date, time);
    }

    private void selectAppointment(Long appointmentId) {
        for (int index = 0; index < appointmentRows.size(); index++) {
            Appointment appointment = appointmentRows.get(index);
            if (appointmentId.equals(appointment.getId())) {
                appointmentTable.getSelectionModel().select(index);
                appointmentTable.scrollTo(index);
                return;
            }
        }
    }

    private String formatPatient(Long patientId) {
        if (patientId == null) {
            return "";
        }
        return patientService.findPatientById(patientId)
                .map(this::formatPatient)
                .orElse(String.valueOf(patientId));
    }

    private String formatPatient(Patient patient) {
        return patient.getId() + " - " + patient.getFirstName() + " " + patient.getLastName();
    }

    private String formatDoctor(Long doctorId) {
        if (doctorId == null) {
            return "";
        }
        return doctorService.findDoctorById(doctorId)
                .map(this::formatDoctor)
                .orElse(String.valueOf(doctorId));
    }

    private String formatDoctor(Doctor doctor) {
        return doctor.getId() + " - " + doctor.getFirstName() + " " + doctor.getLastName();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    private void configurePatientComboBox(ComboBox<Patient> comboBox) {
        comboBox.setPromptText("Patient");
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Patient patient) {
                return patient == null ? "" : formatPatient(patient);
            }

            @Override
            public Patient fromString(String value) {
                return null;
            }
        });
    }

    private void configureDoctorComboBox(ComboBox<Doctor> comboBox) {
        comboBox.setPromptText("Doctor");
        comboBox.setMaxWidth(Double.MAX_VALUE);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Doctor doctor) {
                return doctor == null ? "" : formatDoctor(doctor);
            }

            @Override
            public Doctor fromString(String value) {
                return null;
            }
        });
    }

    private <S, T> TableColumn<S, T> column(String title, Function<S, T> valueFactory) {
        TableColumn<S, T> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(valueFactory.apply(cell.getValue())));
        return column;
    }

    private <S> void setColumns(TableView<S> tableView, List<TableColumn<S, ?>> columns) {
        tableView.getColumns().setAll(columns);
    }

    private Tab tab(String title, VBox content) {
        Tab tab = new Tab(title);
        tab.setContent(content);
        return tab;
    }

    private VBox tabContent(javafx.scene.Node... nodes) {
        VBox content = new VBox(12, nodes);
        content.setPadding(new Insets(16));
        content.getStyleClass().add("tab-content");
        return content;
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private GridPane formGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.getStyleClass().add("form-grid");
        return grid;
    }

    private void addFormRow(GridPane grid, int row, String label, javafx.scene.Node field) {
        grid.add(new Label(label), 0, row);
        grid.add(field, 1, row);
        GridPane.setHgrow(field, Priority.ALWAYS);
    }

    private TextField textField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setMaxWidth(Double.MAX_VALUE);
        return textField;
    }

    private void clear(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void runSafely(String title, Runnable action) {
        try {
            action.run();
        } catch (InvalidStatusTransitionException exception) {
            showError(title, exception.getMessage());
        } catch (RuntimeException exception) {
            showError(title, exception.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message == null || message.isBlank() ? "Unexpected error" : message);
        alert.showAndWait();
    }
}
