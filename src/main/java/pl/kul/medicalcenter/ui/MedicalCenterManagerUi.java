package pl.kul.medicalcenter.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class MedicalCenterManagerUi extends Application {
    private static final String APPLICATION_TITLE = "Medical Center Manager";

    @Override
    public void start(Stage stage) {
        Label title = new Label(APPLICATION_TITLE);
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Initial JavaFX project structure is ready.");
        subtitle.getStyleClass().add("app-subtitle");

        VBox root = new VBox(12, title, subtitle);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(32));
        root.getStyleClass().add("app-root");

        Scene scene = new Scene(root, 720, 420);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setTitle(APPLICATION_TITLE);
        stage.setMinWidth(640);
        stage.setMinHeight(360);
        stage.setScene(scene);
        stage.show();
    }
}
