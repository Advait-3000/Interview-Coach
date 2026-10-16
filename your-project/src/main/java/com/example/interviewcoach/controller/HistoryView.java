
package com.example.interviewcoach.controller;

import com.example.interviewcoach.model.Attempt;
import com.example.interviewcoach.model.AuthenticatedUser;
import com.example.interviewcoach.service.PerformanceService;
import com.example.interviewcoach.util.Launcher;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;

public class HistoryView {

    private static final int PAGE_SIZE = 20;

    public void show(
            Stage stage,
            AuthenticatedUser user,
            PerformanceService performanceService,
            HomeController homeController
    ) {
        Label title = new Label("Interview History");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label(
                "Review your previously completed interviews."
        );

        TableView<Attempt> table = new TableView<>();

        TableColumn<Attempt, Long> idColumn =
                new TableColumn<>("Attempt ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        TableColumn<Attempt, String> typeColumn =
                new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getAttemptType().name()
                )
        );

        TableColumn<Attempt, String> difficultyColumn =
                new TableColumn<>("Difficulty");
        difficultyColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getDifficulty() == null
                                ? "—"
                                : cell.getValue().getDifficulty().name()
                )
        );

        TableColumn<Attempt, String> dateColumn =
                new TableColumn<>("Completed On");
        dateColumn.setCellValueFactory(cell -> {
            var date = cell.getValue().getSubmittedAt();
            String text = date == null
                    ? "—"
                    : date.format(
                            DateTimeFormatter.ofPattern(
                                    "dd MMM yyyy, hh:mm a"
                            )
                    );
            return new javafx.beans.property.SimpleStringProperty(text);
        });

        TableColumn<Attempt, Integer> totalColumn =
                new TableColumn<>("Questions");
        totalColumn.setCellValueFactory(
                new PropertyValueFactory<>("questionCount")
        );

        TableColumn<Attempt, Integer> correctColumn =
                new TableColumn<>("Correct");
        correctColumn.setCellValueFactory(
                new PropertyValueFactory<>("correctCount")
        );

        TableColumn<Attempt, String> scoreColumn =
                new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(cell -> {
            Double score = cell.getValue().getScorePercent();
            return new javafx.beans.property.SimpleStringProperty(
                    score == null
                            ? "—"
                            : String.format("%.1f%%", score)
            );
        });

        table.getColumns().addAll(
                idColumn,
                typeColumn,
                difficultyColumn,
                dateColumn,
                totalColumn,
                correctColumn,
                scoreColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        Label status = new Label();

        try {
            var attempts = performanceService.getHistory(
                    user.id(), 0, PAGE_SIZE
            );

            long total = performanceService.getHistoryCount(user.id());

            table.setItems(FXCollections.observableArrayList(attempts));

            status.setText(
                    total == 0
                            ? "You haven't completed any interviews yet."
                            : "Showing " + attempts.size()
                              + " of " + total + " completed interviews."
            );
        } catch (Exception ex) {
            status.setText(
                    "Unable to load history: " + ex.getMessage()
            );
        }

        Button backButton = new Button("Back to Dashboard");
        backButton.setOnAction(event ->
                Launcher.navigateToDashboard(user, stage)
        );

        VBox header = new VBox(8, title, subtitle);
        header.setPadding(new Insets(24));

        VBox center = new VBox(14, status, table);
        center.setPadding(new Insets(24));
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        HBox footer = new HBox(backButton);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setPadding(new Insets(0, 24, 24, 24));

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(center);
        root.setBottom(footer);
        root.setStyle("-fx-background-color: #F5F7FB;");

        stage.setTitle("Interview Coach - History");
        stage.setScene(new Scene(root, 1100, 700));
        stage.show();
    }
}
