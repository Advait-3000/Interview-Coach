package com.example.interviewcoach;

import com.example.interviewcoach.config.AppConfig;
import com.example.interviewcoach.util.Launcher;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class App extends Application {

    private static final Logger log = LoggerFactory.getLogger(App.class);
    private Stage primaryStage;

    @Override
    public void init() {
        try {
            AppConfig.load();
            log.info("Application configuration loaded");
        } catch (Exception configFailure) {
            log.error("Failed to load application configuration", configFailure);
            Platform.exit();
        }
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Interview Coach");
        Image icon = createDefaultIcon();
        if (icon != null) {
            stage.getIcons().add(icon);
        }

        AppConfig config = AppConfig.getInstance();
        if (!config.isDatabaseAvailable()) {
            showDatabaseUnavailable(stage);
            return;
        }

        try {
            Launcher.navigateToLogin(stage);
            log.info("Application started and login screen shown");
        } catch (Exception startFailure) {
            log.error("Application failed to start UI", startFailure);
            showUnexpectedError(stage, startFailure);
        }
    }

    private Image createDefaultIcon() {
        try {
            // Generate a simple blue circle icon programmatically to avoid
            // requiring an external PNG file on the classpath.
            BufferedImage buffered = new BufferedImage(16, 16,
                    BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D g = buffered.createGraphics();
            g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(0x24, 0x57, 0xC5, 0xFF));
            g.fillOval(0, 0, 16, 16);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(buffered, "png", baos);

            return new Image(
                    new ByteArrayInputStream(baos.toByteArray()),
                    16, 16, true, true);
        } catch (Exception e) {
            log.warn("Could not generate default icon, running without icon", e);
            return null;
        }
    }

    private void showDatabaseUnavailable(Stage stage) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Database unavailable");
        alert.setHeaderText("Cannot reach the database");
        alert.setContentText(
                "Interview Coach could not connect to MySQL. Check that MySQL is running "
                        + "and that the local environment file is configured, then restart the app.");
        // Do not set the owner until the stage has a scene attached.
        // Setting owner here can cause NPE on older JavaFX versions when
        // the stage has no scene yet.
        VBox expandable = new VBox();
        alert.getDialogPane().setExpandableContent(expandable);
        alert.showAndWait();
        Platform.exit();
    }

    private void showUnexpectedError(Stage stage, Exception failure) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Unexpected error");
        alert.setHeaderText("The application could not start");
        alert.setContentText("An unexpected error occurred. See the application logs for details.");
        // Do not set owner until the stage has a scene; omit owner to avoid NPE.
        alert.showAndWait();
        log.error("Unexpected startup error", failure);
        Platform.exit();
    }

    @Override
    public void stop() {
        log.info("Application shutting down");
        getAppConfig().shutdown();
    }

    private AppConfig getAppConfig() {
        return AppConfig.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
