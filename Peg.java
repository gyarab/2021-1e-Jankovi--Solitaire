package com.example.predelanirocnikovky;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.example.predelanirocnikovky.constants.tileSize;
import static com.example.predelanirocnikovky.constants.viewSize;

public class Peg extends StackPane {
    private double mouseX, mouseY;
    private double oldX, oldY;
    public double getOldX() {
        return oldX;
    }
    public double getOldY() {
        return oldY;
    }

    public Peg(int x, int y) {

        move(x, y);

        Circle circle = new Circle(x, y, viewSize);
        circle.setFill(Color.valueOf("#c40003"));
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3);

        getChildren().addAll(circle);

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void move(int x, int y) {
        oldX = x * tileSize + 0.25 * tileSize;
        oldY = y * tileSize + 0.25 * tileSize;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }
}