package com.example.predelanirocnikovky;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.predelanirocnikovky.constants.*;

public class HelloApplication extends Application {
    private Stage gameStage = null;
    private Tile[][] board = new Tile[width][height];
    private Group tileGroup = new Group();
    private Group pegGroup = new Group();
    private int[][] gameLayout = null;

    private void placeTiles(int[][] gameLayout) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Tile tile = null;
                Peg peg = null;
                // 0 jsou nepoužitelné dlaždice, 2 jsou prázdné dlaždice
                if (gameLayout[y][x] == 0 || gameLayout[y][x] == 2) {
                    tile = new Tile(gameLayout[y][x], x, y);
                    // 1 jsou dlaždice s žetony
                } else if (gameLayout[y][x] == 1) {
                    tile = new Tile(gameLayout[y][x], x, y);
                    peg = pohyb(x, y);
                }
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
                if (peg != null) {
                    tile.setPeg(peg);
                    pegGroup.getChildren().add(peg);
                }
            }
        }
    }

    //funkce, která nastaví výchozí herní plochu
    private Parent setupGame(String gameVersion) {
        Pane root = new Pane();
        root.setPrefSize(width * tileSize, height * tileSize);
        root.getChildren().addAll(tileGroup, pegGroup);
        switch (gameVersion) {
            case "english" -> gameLayout = englishVersion;
            case "french" -> gameLayout = frenchVersion;
            default -> gameLayout = englishVersion;
        }
        placeTiles(gameLayout);
        return root;
    }
    // boolean co zkontroluje zda se nesnažíme přetáhnou žeton mimo hrací pole
    private boolean OB(int x, int y) {
        if (x < 0 || x > height || y < 0 || y > width){
            return true;
        }
        return gameLayout[y][x] == 0;
    }
    // boolean co kontroluje počet žetonů ve hře, pokud se rovná 1 vrací true
    private boolean gameFinished(){
        int pegsLeft = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (board[x][y].hasPeg()){
                    pegsLeft += 1;
                }
            }
        }
        return pegsLeft == 1;
    }
    // kontroluje jakýkoliv pohyb zda je legální
    private boolean legalMove(int x0, int y0, int x1, int y1) {

        // zkontroluje, zda hráč nepřetahuje žeton mimo hrací pole pomocí booleanu OB
        if (OB(x1, y1)){
            return false;
        }
        // zkontroluje, zda dlaždice, na kterou chceme žeton přetáhnout, už nemá žeton
        if (board[x1][y1].hasPeg()) {
            System.out.println("Dlaždice na souřadnicích " + x1 + " " + y1 + "je zabraná");
            return false;
        }
        // zkontroluje, zda hráč nepřetahuje žeton moc daleko
        if (Math.abs(x0 - x1) > 2 || Math.abs(y0 - y1) > 2) {
            System.out.println("Moc daleko");
            return false;
        }

        // Zkontroluje, zda hráč přetahuje žeton ve správném směru (horizontální, vertikální nebo diagonální)
        if (Math.abs(x0 - x1) == 1 || Math.abs(y0 - y1) == 1) {
            System.out.println("Špatný směr");
            return false;
        }
        // Kontroluje, zda hráč přeskakuje nějaký žeton
        System.out.println("Přeskakuje žeton = " + board[(x0 + x1) / 2][(y0 + y1) / 2].hasPeg());
        return board[(x0 + x1) / 2][(y0 + y1) / 2].hasPeg();
    }

    private int toBoard(double pixel) {
        return (int) (pixel + tileSize / 2) / tileSize;
    }
    //Metoda, která má na starost pohyb, využívá funkci legalMove
    private Peg pohyb(int x, int y) {
        Peg peg = new Peg(x, y);

        peg.setOnMouseReleased(e -> {
            int x1 = toBoard(peg.getLayoutX());
            int y1 = toBoard(peg.getLayoutY());

            int x0 = toBoard(peg.getOldX());
            int y0 = toBoard(peg.getOldY());

            boolean legalMove = legalMove(x0, y0, x1, y1);
            System.out.println("legalMove from " + x0 + " " + y0 + " to " + x1 + " " + y1);
            System.out.println("Move is " + legalMove);

            if (legalMove) {
                peg.move(x1, y1);

                board[x0][y0].setPeg(null);
                board[x1][y1].setPeg(peg);

                Peg eatenPeg = board[(x0 + x1) / 2][(y0 + y1) / 2].getPeg();
                board[(x0 + x1) / 2][(y0 + y1) / 2].setPeg(null);
                pegGroup.getChildren().remove(eatenPeg);
            }
            if (gameFinished()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException waitE) {
                    waitE.printStackTrace();
                }

                Label label = new Label("YOU WIN!");
                Font font = Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 25);
                label.setFont(font);
                label.setTextFill(Color.RED);
                label.setAlignment(Pos.BASELINE_CENTER);
                Group root = new Group();
                root.getChildren().add(label);
                Scene scene = new Scene(root, 150, 40, Color.WHITE);
                gameStage.setTitle("Game finished");
                gameStage.setScene(scene);
                gameStage.show();

            }

            else {
                peg.abortMove();
            }
        });

        return peg;
    }

    @Override
    public void start(Stage stage) throws IOException {
        String gameVersion = "english";
        this.gameStage = stage;
        Scene scene = new Scene(setupGame(gameVersion));
        gameStage.setTitle("Peg Solitaire");
        gameStage.setScene(scene);
        gameStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
