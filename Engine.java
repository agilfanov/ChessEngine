package com.company;

import java.util.ArrayList;

public class Engine {
    private final Board chessBoard;
    private final int depth;
    int takeDepth = 5;

    public Engine(Board chessBoard, int depth) {
        this.chessBoard = chessBoard;
        this.depth = depth;
        takeDepth = depth + 1;
    }

    public MoveValue bestMoveForBoard(int currentDepth) {
        ArrayList<Move> legalMoves = chessBoard.generateLegalMovesForCurrentColor();
        if (legalMoves.isEmpty()) {
            return noLegalMovesCase(takeDepth - currentDepth);
        }
        MoveValue bestMove = new MoveValue(null, 10000);
        if (chessBoard.isWhiteTurn()) bestMove.setEvaluation(-10000);

        for (Move possibleMove: legalMoves) {

            Piece fromPiece = chessBoard.getPiece(possibleMove.getFromI(), possibleMove.getFromJ());
            Piece toPiece = chessBoard.getPiece(possibleMove.getToI(), possibleMove.getToJ());

            simulateMoveForward(possibleMove);
            MoveValue possibleMoveValue = new MoveValue(new Move(possibleMove), 0);

            if ((currentDepth == depth && toPiece == null) || (currentDepth == takeDepth)) {
                possibleMoveValue.setEvaluation(Game.evaluateBoard(chessBoard));
            } else {
                possibleMoveValue.setEvaluation(bestMoveForBoard(currentDepth + 1).getEvaluation());
            }

            simulateMoveBack(possibleMove, fromPiece, toPiece);

            if (chessBoard.isWhiteTurn() && bestMove.getEvaluation() < possibleMoveValue.getEvaluation()) {
                bestMove.setMove(possibleMove);
                bestMove.setEvaluation(possibleMoveValue.getEvaluation());
            } else if (!chessBoard.isWhiteTurn() && bestMove.getEvaluation() > possibleMoveValue.getEvaluation()) {
                bestMove.setMove(possibleMove);
                bestMove.setEvaluation(possibleMoveValue.getEvaluation());
            }
        }
        return bestMove;
    }



    private MoveValue noLegalMovesCase(int depthLeft) {
        boolean ownKingInCheck = chessBoard.kingInCheck();
        if (ownKingInCheck) {
            int m = 1;
            if (chessBoard.isWhiteTurn()) m = -1;
            return new MoveValue(null, m * 1000 + depthLeft * m);
        } else {
            return new MoveValue(null, 0);
        }
    }

    private void simulateMoveForward(Move move) {
        chessBoard.makeMove(move);
        chessBoard.toggleWhiteTurn();
    }

    private void simulateMoveBack(Move move, Piece fromPiece, Piece toPiece) {
        chessBoard.toggleWhiteTurn();
        chessBoard.makeMoveBack(move, fromPiece, toPiece);
    }

}
