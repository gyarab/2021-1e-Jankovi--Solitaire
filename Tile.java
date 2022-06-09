package com.example.predelanirocnikovky;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Tile extends Rectangle {

    private Peg peg;

    public boolean hasPeg() {
        return peg != null;
    }

    public Peg getPeg() {
        return peg;
    }

    public void setPeg(Peg peg) {
        this.peg = peg;
    }

    public Tile(int light, int x, int y) {
        setWidth(constants.tileSize);
        setHeight(constants.tileSize);

        relocate(x * constants.tileSize, y * constants.tileSize);
        if (light <= 0){
            setFill(Color.valueOf("#000000"));
        }
        else{
            setFill((x + y) % 2 == 0 ? Color.valueOf("#eadabc") : Color.valueOf("#bf9053"));
        }
    }
}
