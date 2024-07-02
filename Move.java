package com.company;

public class Move {
    private int fromI;
    private int fromJ;
    private int toI;
    private int toJ;

    public Move(int fromI, int fromJ, int toI, int toJ) {
        this.fromI = fromI;
        this.fromJ = fromJ;
        this.toI = toI;
        this.toJ = toJ;
    }

    public Move(Move move) {
        this(move.getFromI(), move.getFromJ(), move.getToI(), move.getToJ());
    }

    public int getFromI() {return fromI;}
    public int getFromJ() {return fromJ;}
    public int getToI() {return toI;}
    public int getToJ() {return toJ;}

    public static Move whiteCastleKingSide() {return new Move(7, 4, 7, 6);}
    public static Move whiteCastleQueenSide() {return new Move(7, 4, 7, 2);}
    public static Move blackCastleKingSide() {return new Move(0, 4, 0, 6);}
    public static Move blackCastleQueenSide() {return new Move(0, 4, 0, 2);}

    public static int castleMoveToIndex(Move move) {
        if (move.equal(whiteCastleKingSide())) return 0;
        if (move.equal(whiteCastleQueenSide())) return 1;
        if (move.equal(blackCastleKingSide())) return 2;
        if (move.equal(blackCastleQueenSide())) return 3;
        return -1;
    }

    public boolean equal(Move compareMove) {
        return  (this.fromI == compareMove.getFromI() && this.fromJ == compareMove.getFromJ() &&
                this.toI == compareMove.getToI() && this.toJ == compareMove.getToJ());
    }

    public void setMoveTo(Move move) {
        this.fromI = move.getFromI();
        this.fromJ = move.getFromJ();
        this.toI = move.getToI();
        this.toJ = move.getToJ();
    }

    public String toString() {
        return "From: " + fromI + ", " + fromJ + "     To: " + toI + ", " + toJ;
    }
}
