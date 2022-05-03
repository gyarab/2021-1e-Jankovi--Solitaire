package com.example.rocnikovka_official;

public class MoveResult {

    private MoveType type;

    public MoveType getType() {
        return type;
    }

    private Peg peg;

    public Peg getPeg() {
        return peg;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, Peg peg) {
        this.type = type;
        this.peg = peg;
    }
}
