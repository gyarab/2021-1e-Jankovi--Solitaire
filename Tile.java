package com.example.rocnikovka_official;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private Peg peg;

    public boolean hasPeg() {
        return peg != null;
    }
    public Peg getPeg () {
        return peg;
    }

    public void setPeg(Peg peg) {
        this.peg = peg;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(HelloApplication.tileSize);
        setHeight(HelloApplication.tileSize);

        relocate(x * HelloApplication.tileSize, y * HelloApplication.tileSize);

        setFill(light ? Color.valueOf("#D3D3D3") : Color.valueOf("#A9A9A9"));
    }
}
