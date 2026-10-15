package com.example.interviewcoach.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;

import java.util.Objects;

public final class IconLoader {

    private IconLoader() {
    }

    public static ImageView circle(String color) {
        javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas(12, 12);
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(javafx.scene.paint.Color.web(color));
        gc.fillOval(0, 0, 12, 12);
        Image image = canvas.snapshot(null, null);
        ImageView iv = new ImageView(image);
        iv.setFitWidth(12);
        iv.setFitHeight(12);
        iv.setPreserveRatio(true);
        return iv;
    }

    public static Image missingImage() {
        return new Image(Objects.requireNonNull(
                IconLoader.class.getResourceAsStream("/images/missing.png")),
                16, 16, true, true);
    }
}
