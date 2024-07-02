package com.company;

public class PieceData {
    private static final int inf = 1000;
    private static final int[] costs = {1, 3, 3, 5, 9, inf};
    private static final int[] maxMoves = {1, 1, 7, 7, 7, 1};
    private static final String[] printNames = {"♙", "♘", "♗", "♖", "♕", "♔", "♟", "♞", "♝", "♜", "♛", "♚"};

    private static final int[] knightDi = {-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] knightDj = {-1, 1, -2, 2, -2, 2, -1, 1};
    private static final int[] bishopDi = {-1, -1, 1, 1};
    private static final int[] bishopDj = {-1, 1, -1, 1};
    private static final int[] rookDi = {-1, 0, 0, 1};
    private static final int[] rookDj = {0, -1, 1, 0};
    private static final int[] queenDi = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
    private static final int[] queenDj = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
    private static final int[] kingDi = {-1, -1, -1, 0, 0, 0, 1, 1, 1};
    private static final int[] kingDj = {-1, 0, 1, -1, 0, 1, -1, 0, 1};
    private static final int[][] di = {{}, knightDi, bishopDi, rookDi, queenDi, kingDi};
    private static final int[][] dj = {{}, knightDj, bishopDj, rookDj, queenDj, kingDj};

    public static int getCost(Piece piece) {
        return costs[piece.ordinal() % 6];
    }

    public static int getMaxMoves(Piece piece) {
        return maxMoves[piece.ordinal() % 6];
    }

    public static int[] getDi(Piece piece) {
        return di[piece.ordinal() % 6];
    }

    public static int[] getDj(Piece piece) {
        return dj[piece.ordinal() % 6];
    }

    public static String getPrintName(Piece piece) {
        return printNames[piece.ordinal()];
    }

}
