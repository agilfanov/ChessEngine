package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private final Board chessBoard;
    private final Engine computerEngine;

    public Game(int engineDepth) {
        this.chessBoard = new Board();
        this.computerEngine = new Engine(chessBoard, engineDepth);
    }

    public void playTurnComputerBlack() {
        makePlayerMove();

        int state = whiteTurnState(chessBoard);
        if (state == 0) {
            System.out.println(Color.ANSI_RED + "White Won" + Color.ANSI_RESET);
            return;
        } else if (state == 1) {
            System.out.println("Tie");
            return;
        }
        printChessBoard();
        makeComputerMove();
        state = whiteTurnState(chessBoard);
        if (state == 0) {
            System.out.println(Color.ANSI_RED + "Black Won" + Color.ANSI_RESET);
        } else if (state == 1) {
            System.out.println("Tie");
        }

    }

    public void playTurnComputerWhite() {
        makeComputerMove();

        int state = whiteTurnState(chessBoard);
        if (state == 0) {
            System.out.println(Color.ANSI_RED + "White Won" + Color.ANSI_RESET);
            return;
        } else if (state == 1) {
            System.out.println("Tie");
            return;
        }
        printChessBoard();
        makePlayerMove();

        state = whiteTurnState(chessBoard);
        if (state == 0) {
            System.out.println(Color.ANSI_RED + "Black Won" + Color.ANSI_RESET);
        } else if (state == 1) {
            System.out.println("Tie");
        }
    }

    private void makePlayerMove() {
        ArrayList<Move> possibleMoves = chessBoard.generateLegalMovesForCurrentColor();
        System.out.println(Color.ANSI_BLUE + "Enter move:" + Color.ANSI_RESET);
        Move playerMove = readMove();
        for (Move move : possibleMoves) {
            if (playerMove.equal(move)) {
                chessBoard.makeMove(playerMove);
                if (checkPawnPromotion(playerMove)) {
                    System.out.println(chessBoard.getPiece(move.getToI(), move.getToJ()).ordinal());
                    executePawnPromotion(playerMove.getToI(), playerMove.getToJ(), true);
                }
                chessBoard.toggleWhiteTurn();
                return;
            }
        }
        System.out.println(Color.ANSI_BLUE + "Not a legal move" + Color.ANSI_RESET);
        makePlayerMove();
    }

    private void makeComputerMove() {
        MoveValue comp = computerEngine.bestMoveForBoard(1);
        System.out.println(Color.ANSI_RED + "_______________________________" + Color.ANSI_RESET);
        Move computerMove = comp.getMove();
        chessBoard.makeMove(computerMove);
        if (checkPawnPromotion(computerMove)) {
            executePawnPromotion(computerMove.getToI(), computerMove.getToJ(), false);
        }
        chessBoard.toggleWhiteTurn();
    }

    public static double evaluateBoard(Board board) {
        double evaluation = (board.getWhiteSum() - board.getBlackSum());
        for (int i = 2; i < 6; i++) {
            for (int j = 2; j < 6; j++) {
                if (board.getPiece(i, j) != null) {
                    double dist = (10 - Math.abs(3.5 - i)) * 0.005 + (10 - Math.abs(3.5 - j)) * 0.005;
                    if (board.getPiece(i, j).ordinal() > 5) {
                        evaluation -= dist;
                    } else {
                        evaluation += dist;
                    }
                }
            }
        }
        if (board.isWhiteCastled()) evaluation += 0.6;
        if (board.isBlackCastled()) evaluation -= 0.6;
        return evaluation;
    }

    private void executePawnPromotion(int iPosition, int jPosition, boolean playerTurn) {
        String promoteTo = "Q";
        if (playerTurn) {
            Scanner input = new Scanner(System.in);
            System.out.println("What piece would you like to promote to? Q, R, B, K");
            promoteTo = input.nextLine();
        }
        Piece promotionPiece = null;
        if (chessBoard.isWhiteTurn()) {
            if (promoteTo.equals("Q")) promotionPiece = Piece.WQUEEN;
            if (promoteTo.equals("R")) promotionPiece = Piece.WROOK;
            if (promoteTo.equals("B")) promotionPiece = Piece.WBISHOP;
            if (promoteTo.equals("K")) promotionPiece = Piece.WKNIGHT;
            chessBoard.promotePawn(iPosition, jPosition, promotionPiece);
        } else {
            if (promoteTo.equals("Q")) promotionPiece = Piece.BQUEEN;
            if (promoteTo.equals("R")) promotionPiece = Piece.BROOK;
            if (promoteTo.equals("B")) promotionPiece = Piece.BBISHOP;
            if (promoteTo.equals("K")) promotionPiece = Piece.BKNIGHT;
            chessBoard.promotePawn(iPosition, jPosition, promotionPiece);
        }
    }

    private boolean checkPawnPromotion(Move move) {
        return ((chessBoard.isWhiteTurn() && move.getToI() == 0 && chessBoard.getPiece(move.getToI(), move.getToJ()).ordinal() == 0) ||
                (!chessBoard.isWhiteTurn() && move.getToI() == 7 && chessBoard.getPiece(move.getToI(), move.getToJ()).ordinal() == 6));
    }

    private Move readMove() {
        Scanner input = new Scanner(System.in);
        String moveString = input.nextLine();
        if (moveString.length() < 5) {
            return new Move(0, 0, 4, 1);
        }
        return new Move(8 - (moveString.charAt(1) - '0'), moveString.charAt(0) - 'a', 8 - (moveString.charAt(4) - '0'), moveString.charAt(3) - 'a');
    }

    private static int whiteTurnState(Board board) {
        boolean whiteTurnKingInCheck = board.kingInCheck();
        ArrayList<Move> legalMoves = board.generateLegalMovesForCurrentColor();
        if (legalMoves.size() == 0 && whiteTurnKingInCheck) return 0;
        else if (legalMoves.size() == 0) return 1;
        board.toggleWhiteTurn();
        ArrayList<Move> legalMovesForOther = board.generateLegalMovesForCurrentColor();
        boolean otherKingInCheck = board.kingInCheck();
        board.toggleWhiteTurn();
        if (legalMovesForOther.size() == 0 && otherKingInCheck) return 2;
        else if (legalMovesForOther.size() == 0) return 1;
        return 3;
    }

    private static double round(double x) {
        x *= 10000;
        x = (int) x;
        x /= 10000;
        return x;
    }

    public void printChessBoard() {
        String[] stringChessBoard = chessBoard.boardToString();
        for (String s : stringChessBoard) {
            System.out.println(s);
        }
    }
}
