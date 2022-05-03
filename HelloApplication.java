package com.example.rocnikovka_official;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static  final  int tileSize = 100;
    public static  final  int width = 7;
    public static  final  int height = 7;

    private Tile[][] board = new Tile[width][height];

    private Group tileGroup = new Group();
    private Group pegGroup = new Group();

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pegGroup);

        for (int y = 0; y < height; y++) {
             for (int x = 0; x < width; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board [x][y] = tile;

                tileGroup.getChildren().add(tile);

                Peg peg = null;

                if (x > 1 && y == 0 && x < 5) {
                    peg = makePiece(PieceType.RED, x, y);
                }
                 if (x > 1 && y == 1 && x < 5) {
                     peg = makePiece(PieceType.RED, x, y);
                 }
                 if (y == 2) {
                     peg = makePiece(PieceType.RED, x, y);
                 }
                 if (y == 3 && x != 3) {
                     peg = makePiece(PieceType.RED, x, y);
                 }
                 if (y == 4) {
                     peg = makePiece(PieceType.RED, x, y);
                 }
                 if (x > 1 && y == 5 && x < 5) {
                     peg = makePiece(PieceType.RED, x, y);
                 }
                 if (x > 1 && y == 6 && x < 5) {
                     peg = makePiece(PieceType.RED, x, y);
                 }


                if (peg != null) {
                    tile.setPeg(peg);
                    pegGroup.getChildren().add(peg);
                }

            }
        }
        return root;
    }

    private MoveResult tryMove(Peg peg, int newX, int newY) {

        int mouseX =  toBoard(peg.getMouseX());
        int mouseY =  toBoard(peg.getMouseY());

        int layoutX = toBoard(peg.getLayoutX());
        int layoutY = toBoard(peg.getLayoutY());

        int x0 = toBoard(peg.getOldX());
        int y0 = toBoard(peg.getOldY());

        int x2 = x0 - 2;
        int x3 = x0 + 2;
        int y2 = y0 - 2;
        int y3 = y0 + 2;




        if (board[newX][newY].hasPeg()) {
            return new MoveResult(MoveType.NONE);
        }
        if (Math.abs(newX) < 2 && Math.abs(newY) < 2) {
            return new MoveResult(MoveType.NONE);
        }
        if (Math.abs(newX) > 4 && Math.abs(newY) < 2) {
            return new MoveResult(MoveType.NONE);
        }
        if (Math.abs(newX) > 4 && Math.abs(newY) > 4) {
            return new MoveResult(MoveType.NONE);
        }
        if (Math.abs(newX) < 2 && Math.abs(newY) > 4) {
            return new MoveResult(MoveType.NONE);
        }




        if (Math.abs(newX - x0) == 2 || Math.abs(newY - y0) == 2) {

            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;

            if (board[x1][y1].hasPeg()) {
                return new MoveResult(MoveType.KILL, board[x1][y1].getPeg());
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    private int toBoard(double pixel) {
        return (int)(pixel + tileSize / 2) / tileSize;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
        stage.setTitle("Peg Solitaire");
        stage.setScene(scene);
        stage.show();
    }

    private Peg makePiece(PieceType type, int x, int y) {
        Peg peg = new Peg(type, x, y);

        peg.setOnMouseReleased(e -> {
            int newX = toBoard(peg.getLayoutX());
            int newY = toBoard(peg.getLayoutY());

            MoveResult result = tryMove(peg, newX, newY);

            int x0 = toBoard(peg.getOldX());
            int y0 = toBoard(peg.getOldY());

            switch (result.getType()) {

                case NONE:
                    peg.abortMove();
                    break;
                case KILL:
                    peg.move(newX, newY);
                    board[x0][y0].setPeg(null);
                    board[newX][newY].setPeg(peg);

                    Peg otherPeg = result.getPeg();
                    board[toBoard(otherPeg.getOldX())][toBoard(otherPeg.getOldY())].setPeg(null);
                    pegGroup.getChildren().remove(otherPeg);

                    break;
            }

        });

        return peg;
    }

    public static void main(String[] args) {launch();}
}