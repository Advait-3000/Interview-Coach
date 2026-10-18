package com.example.interviewcoach.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FxmlViewsTest {

    @BeforeAll
    static void initJavaFx() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
            assertTrue(latch.await(5, TimeUnit.SECONDS));
        } catch (IllegalStateException alreadyStarted) {
            // JavaFX runtime already initialized
        }
    }

    @Test
    void cssFileExistsAndIsAccessible() {
        URL css = getClass().getResource("/css/app.css");
        assertNotNull(css, "app.css must be available on the classpath");
    }

    @Test
    void loginViewFxmlLoadsSuccessfullyAsVBox() throws Exception {
        URL fxml = getClass().getResource("/fxml/LoginView.fxml");
        assertNotNull(fxml, "LoginView.fxml must exist");

        FXMLLoader loader = new FXMLLoader(fxml);
        VBox root = loader.load();
        assertNotNull(root, "Root node of LoginView.fxml must be a VBox");
        assertNotNull(loader.getController(), "LoginController should be bound");
    }

    @Test
    void registerViewFxmlLoadsSuccessfullyAsVBox() throws Exception {
        URL fxml = getClass().getResource("/fxml/RegisterView.fxml");
        assertNotNull(fxml, "RegisterView.fxml must exist");

        FXMLLoader loader = new FXMLLoader(fxml);
        VBox root = loader.load();
        assertNotNull(root, "Root node of RegisterView.fxml must be a VBox");
        assertNotNull(loader.getController(), "RegisterController should be bound");
    }

    @Test
    void homeViewFxmlLoadsSuccessfullyAsBorderPane() throws Exception {
        URL fxml = getClass().getResource("/fxml/HomeView.fxml");
        assertNotNull(fxml, "HomeView.fxml must exist");

        FXMLLoader loader = new FXMLLoader(fxml);
        BorderPane root = loader.load();
        assertNotNull(root, "Root node of HomeView.fxml must be a BorderPane");
        assertNotNull(loader.getController(), "HomeController should be bound");
    }

    @Test
    void studentDashboardViewFxmlLoadsSuccessfullyAsVBox() throws Exception {
        URL fxml = getClass().getResource("/fxml/StudentDashboardView.fxml");
        assertNotNull(fxml, "StudentDashboardView.fxml must exist");

        FXMLLoader loader = new FXMLLoader(fxml);
        VBox root = loader.load();
        assertNotNull(root, "Root node of StudentDashboardView.fxml must be a VBox");
        assertNotNull(loader.getController(), "StudentDashboardController should be bound");
    }
}
