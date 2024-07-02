package com.company;

public class MoveValue {
    private Move move;
    private double evaluation;

    public MoveValue(Move move, double evaluation) {
        this.move = move;
        this.evaluation = evaluation;
    }

    public Move getMove() {return this.move;}
    public double getEvaluation() {return this.evaluation;}
    public void setEvaluation(double evaluation) {this.evaluation = evaluation;}
    public void setMove(Move move) {
        if (this.move == null) {
            this.move = new Move(move);
        } else this.move.setMoveTo(move);
    }

}
