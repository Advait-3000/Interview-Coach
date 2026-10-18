
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
        Label tag = new Label("ACTIVITY LOG");
        tag.getStyleClass().add("brand-tag");

        Label title = new Label("Interview History");
        title.getStyleClass().add("label-title");

        Label subtitle = new Label(
                "Review your previously completed interview sessions and performance scores."
        );
        subtitle.getStyleClass().add("label-subtitle");

        TableView<Attempt> table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<Attempt, Long> idColumn =
                new TableColumn<>("ID");
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        idColumn.setPrefWidth(60);

        TableColumn<Attempt, String> typeColumn =
                new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getAttemptType() != null
                                ? cell.getValue().getAttemptType().name()
                                : "PRACTICE"
                )
        );
        typeColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("badge-neutral");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
        typeColumn.setPrefWidth(120);

        TableColumn<Attempt, String> difficultyColumn =
                new TableColumn<>("Difficulty");
        difficultyColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getDifficulty() == null
                                ? "—"
                                : cell.getValue().getDifficulty().name()
                )
        );
        difficultyColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || "—".equals(item)) {
                    setGraphic(null);
                    setText(empty ? null : "—");
                } else {
                    Label badge = new Label(item);
                    if ("EASY".equalsIgnoreCase(item)) {
                        badge.getStyleClass().add("badge-success");
                    } else if ("MEDIUM".equalsIgnoreCase(item)) {
                        badge.getStyleClass().add("badge-warning");
                    } else {
                        badge.getStyleClass().add("badge-error");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
        difficultyColumn.setPrefWidth(110);

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
        dateColumn.setPrefWidth(180);

        TableColumn<Attempt, Integer> totalColumn =
                new TableColumn<>("Questions");
        totalColumn.setCellValueFactory(
                new PropertyValueFactory<>("questionCount")
        );
        totalColumn.setPrefWidth(100);

        TableColumn<Attempt, Integer> correctColumn =
                new TableColumn<>("Correct");
        correctColumn.setCellValueFactory(
                new PropertyValueFactory<>("correctCount")
        );
        correctColumn.setPrefWidth(100);

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
        scoreColumn.setCellFactory(col -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || "—".equals(item)) {
                    setGraphic(null);
                    setText(empty ? null : "—");
                } else {
                    Label badge = new Label(item);
                    try {
                        double val = Double.parseDouble(item.replace("%", "").trim());
                        if (val >= 70.0) {
                            badge.getStyleClass().add("badge-success");
                        } else if (val >= 50.0) {
                            badge.getStyleClass().add("badge-warning");
                        } else {
                            badge.getStyleClass().add("badge-error");
                        }
                    } catch (Exception e) {
                        badge.getStyleClass().add("badge-neutral");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
        scoreColumn.setPrefWidth(120);

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
        status.getStyleClass().add("label-helper");

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

        Button backButton = new Button("← Back to Dashboard");
        backButton.getStyleClass().add("secondary-button");
        backButton.setPrefHeight(40);
        backButton.setOnAction(event ->
                Launcher.navigateToDashboard(user, stage)
        );

        // Header inside card
        VBox header = new VBox(6, tag, title, subtitle);

        VBox contentCard = new VBox(16, header, status, table, backButton);
        contentCard.setPadding(new Insets(32, 36, 32, 36));
        contentCard.getStyleClass().add("surface");
        contentCard.setMaxWidth(1060);
        VBox.setVgrow(table, javafx.scene.layout.Priority.ALWAYS);

        VBox outerWrapper = new VBox(contentCard);
        outerWrapper.setAlignment(Pos.TOP_CENTER);
        outerWrapper.setPadding(new Insets(28, 36, 28, 36));
        outerWrapper.setStyle("-fx-background-color: #F5F7FB;");

        BorderPane root = new BorderPane();
        root.setCenter(outerWrapper);
        root.setStyle("-fx-background-color: #F5F7FB;");

        Scene scene = new Scene(root, 1140, 750);
        scene.getStylesheets().add(
                java.util.Objects.requireNonNull(
                        getClass().getResource("/css/app.css")
                ).toExternalForm()
        );

        stage.setTitle("Interview Coach - Interview History");
        stage.setScene(scene);
        stage.show();
    }
}
