package com.company;

import java.util.ArrayList;

public class Board {

    private final Piece[][] chessBoard;
    private boolean whiteTurn;
    private int whiteSum;
    private int blackSum;
    private int[] kingMoves = {0, 0};
    private int[] rookMoves = {0, 0, 0, 0};

    private boolean whiteCastled;
    private boolean blackCastled;

    private final static Pair[][] castlingPoints = {
            {new Pair(7, 4), new Pair(7, 5), new Pair(7, 6)},
            {new Pair(7, 4), new Pair(7, 3), new Pair(7, 2), new Pair(7, 1)},
            {new Pair(0, 4), new Pair(0, 5), new Pair(0, 6)},
            {new Pair(0, 4), new Pair(0, 3), new Pair(0, 2), new Pair(0, 1)}};


    public Board() {

        chessBoard = new Piece[8][8];
        whiteTurn = true;
        for (int j = 0; j < 8; j++) {
            chessBoard[1][j] = Piece.BPAWN;
            chessBoard[6][j] = Piece.WPAWN;
        }

        chessBoard[0][0] = Piece.BROOK;
        chessBoard[0][1] = Piece.BKNIGHT;
        chessBoard[0][2] = Piece.BBISHOP;
        chessBoard[0][3] = Piece.BQUEEN;
        chessBoard[0][4] = Piece.BKING;
        chessBoard[0][5] = Piece.BBISHOP;
        chessBoard[0][6] = Piece.BKNIGHT;
        chessBoard[0][7] = Piece.BROOK;
        chessBoard[7][0] = Piece.WROOK;
        chessBoard[7][1] = Piece.WKNIGHT;
        chessBoard[7][2] = Piece.WBISHOP;
        chessBoard[7][3] = Piece.WQUEEN;
        chessBoard[7][4] = Piece.WKING;
        chessBoard[7][5] = Piece.WBISHOP;
        chessBoard[7][6] = Piece.WKNIGHT;
        chessBoard[7][7] = Piece.WROOK;
        whiteSum = 39;
        blackSum = 39;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public int getWhiteSum() {
        return whiteSum;
    }

    public int getBlackSum() {
        return blackSum;
    }

    public void toggleWhiteTurn() {
        whiteTurn = !whiteTurn;
    }

    public Piece getPiece(int i, int j) {
        return chessBoard[i][j];
    }

    public boolean isWhiteCastled() {
        return whiteCastled;
    }

    public boolean isBlackCastled() {
        return blackCastled;
    }

    public void makeMove(Move move) {
        Piece toPiece = chessBoard[move.getToI()][move.getToJ()];
        Piece from = chessBoard[move.getFromI()][move.getFromJ()];
        if (from == null) return;
        if (from.ordinal() % 6 == 5 && Math.abs(move.getFromJ() - move.getToJ()) == 2) {
            makeCastlingMove(Move.castleMoveToIndex(move));
            return;
        }

        if (toPiece != null) {
            if (toPiece.ordinal() >= 6) {
                blackSum -= PieceData.getCost(toPiece);
            } else {
                whiteSum -= PieceData.getCost(toPiece);
            }
        }
        if (from == Piece.WKING) {
            kingMoves[0]++;
        } else if (from == Piece.WROOK && move.getFromJ() == 0) {
            rookMoves[1]++;
        } else if (from == Piece.WROOK && move.getFromJ() == 7) {
            rookMoves[0]++;
        }
        if (from == Piece.BKING) {
            kingMoves[1]++;
        } else if (from == Piece.BROOK && move.getFromJ() == 0) {
            rookMoves[3]++;
        } else if (from == Piece.BROOK && move.getFromJ() == 7) {
            rookMoves[2]++;
        }
        chessBoard[move.getToI()][move.getToJ()] = from;
        chessBoard[move.getFromI()][move.getFromJ()] = null;
    }

    public void makeMoveBack(Move move, Piece from, Piece to) {
        if (from == null) return;
        if (from.ordinal() % 6 == 5 && Math.abs(move.getFromJ() - move.getToJ()) == 2) {
            makeCastlingMoveBack(Move.castleMoveToIndex(move));
            return;
        }
        if (from == Piece.WKING) {
            kingMoves[0]--;
        } else if (from == Piece.WROOK && move.getFromJ() == 0) {
            rookMoves[1]--;
        } else if (from == Piece.WROOK && move.getFromJ() == 7) {
            rookMoves[0]--;
        }
        if (from == Piece.BKING) {
            kingMoves[1]--;
        } else if (from == Piece.BROOK && move.getFromJ() == 0) {
            rookMoves[3]--;
        } else if (from == Piece.BROOK && move.getFromJ() == 7) {
            rookMoves[2]--;
        }
        chessBoard[move.getFromI()][move.getFromJ()] = from;
        chessBoard[move.getToI()][move.getToJ()] = to;
        if (whiteTurn && to != null) blackSum += PieceData.getCost(to);
        if (!whiteTurn && to != null) whiteSum += PieceData.getCost(to);
    }

    private void makeCastlingMoveBack(int castleIndex) {

        if (castleIndex == 0) {
            kingMoves[0]--;
            rookMoves[0]--;
            chessBoard[7][6] = null;
            chessBoard[7][5] = null;
            chessBoard[7][4] = Piece.WKING;
            chessBoard[7][7] = Piece.WROOK;
            whiteCastled = false;
        } else if (castleIndex == 1) {
            kingMoves[0]--;
            rookMoves[1]--;
            chessBoard[7][2] = null;
            chessBoard[7][3] = null;
            chessBoard[7][4] = Piece.WKING;
            chessBoard[7][0] = Piece.WROOK;
            whiteCastled = false;
        } else if (castleIndex == 2) {
            kingMoves[1]--;
            rookMoves[2]--;
            chessBoard[0][6] = null;
            chessBoard[0][5] = null;
            chessBoard[0][4] = Piece.BKING;
            chessBoard[0][7] = Piece.BROOK;
            blackCastled = false;
        } else {
            kingMoves[1]--;
            rookMoves[3]--;
            chessBoard[0][2] = null;
            chessBoard[0][3] = null;
            chessBoard[0][4] = Piece.BKING;
            chessBoard[0][0] = Piece.BROOK;
            blackCastled = false;
        }
    }

    private void makeCastlingMove(int castleIndex) {
        if (castleIndex == 0) {
            chessBoard[7][6] = Piece.WKING;
            chessBoard[7][5] = Piece.WROOK;
            chessBoard[7][4] = null;
            chessBoard[7][7] = null;
            kingMoves[0]++;
            rookMoves[0]++;
            whiteCastled = true;
        } else if (castleIndex == 1) {
            chessBoard[7][2] = Piece.WKING;
            chessBoard[7][3] = Piece.WROOK;
            chessBoard[7][4] = null;
            chessBoard[7][0] = null;
            kingMoves[0]++;
            rookMoves[1]++;
            whiteCastled = true;
        } else if (castleIndex == 2) {
            chessBoard[0][6] = Piece.BKING;
            chessBoard[0][5] = Piece.BROOK;
            chessBoard[0][4] = null;
            chessBoard[0][7] = null;
            kingMoves[1]++;
            rookMoves[2]++;
            blackCastled = true;
        } else {
            chessBoard[0][2] = Piece.BKING;
            chessBoard[0][3] = Piece.BROOK;
            chessBoard[0][4] = null;
            chessBoard[0][0] = null;
            kingMoves[1]++;
            rookMoves[3]++;
            blackCastled = true;
        }
    }

    private boolean canCastle(int castleIndex) {
        if (kingMoves[castleIndex / 2] + rookMoves[castleIndex] != 0) return false;
        if (castleIndex == 0) {
            if (chessBoard[7][4] != Piece.WKING || chessBoard[7][7] != Piece.WROOK) return false;
        } else if (castleIndex == 1) {
            if (chessBoard[7][4] != Piece.WKING || chessBoard[7][0] != Piece.WROOK) return false;
        } else if (castleIndex == 2) {
            if (chessBoard[0][4] != Piece.BKING || chessBoard[0][7] != Piece.BROOK) return false;
        } else {
            if (chessBoard[0][4] != Piece.BKING || chessBoard[0][0] != Piece.BROOK) return false;
        }
        for (Pair point : castlingPoints[castleIndex]) {
            if (tileInCheck(point.a, point.b)) return false;
            if (chessBoard[point.a][point.b] != null && PieceData.getCost(chessBoard[point.a][point.b]) != 1000)
                return false;
        }
        return true;
    }


    private boolean movesInListCheckTile(int tileI, int tileJ, ArrayList<Move> movesList) {
        for (Move currentMove : movesList) {
            if (currentMove.getToI() == tileI && currentMove.getToJ() == tileJ) return true;
        }
        return false;
    }

    public void promotePawn(int iPosition, int jPosition, Piece promoteTo) {
        if (whiteTurn) {
            whiteSum += (PieceData.getCost(promoteTo) - 1);
        } else {
            blackSum += (PieceData.getCost(promoteTo) - 1);
        }
        chessBoard[iPosition][jPosition] = promoteTo;
    }

    public boolean kingInCheck() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (whiteTurn && chessBoard[i][j] == Piece.WKING || !whiteTurn && chessBoard[i][j] == Piece.BKING) {
                    return tileInCheck(i, j);
                }
            }
        }
        return false;
    }


    public boolean tileInCheck(int tileI, int tileJ) {
        ArrayList<Move> movesOfCurrentPiece = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] != null) {
                    movesOfCurrentPiece.clear();
                    if ((whiteTurn && chessBoard[i][j].ordinal() > 5) || (!whiteTurn && chessBoard[i][j].ordinal() < 6)) {
                        movesOfCurrentPiece = generateMovesForPiece(chessBoard[i][j], i, j, !whiteTurn);
                        if (movesInListCheckTile(tileI, tileJ, movesOfCurrentPiece)) return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean isLegalMove(Move move) {
        Piece fromPiece = chessBoard[move.getFromI()][move.getFromJ()];
        Piece toPiece = chessBoard[move.getToI()][move.getToJ()];
        if ((fromPiece != null && toPiece != null) && (fromPiece.ordinal() < 6 && toPiece.ordinal() < 6) ||
                ((fromPiece != null && toPiece != null) && (fromPiece.ordinal() > 5 && toPiece.ordinal() > 5)))
            return false;
        if (toPiece != null && toPiece.ordinal() % 6 == 5) return false;
        makeMove(move);
        boolean legal = kingInCheck();
        makeMoveBack(move, fromPiece, toPiece);

        return !legal;
    }


    private ArrayList<Move> generateMovesForPawn(int iPos, int jPos, boolean whitePawn) {
        ArrayList<Move> possibleMovesForPawn = new ArrayList<>();
        if (whitePawn) {
            boolean canGoForward = iPos > 0 && chessBoard[iPos - 1][jPos] == null;
            if (canGoForward)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos - 1, jPos));
            if (iPos == 6 && canGoForward && chessBoard[iPos - 2][jPos] == null)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos - 2, jPos));
            if (jPos > 0 && iPos > 0 && chessBoard[iPos - 1][jPos - 1] != null && chessBoard[iPos - 1][jPos - 1].ordinal() > 5)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos - 1, jPos - 1));
            if (jPos < 7 && iPos > 0 && chessBoard[iPos - 1][jPos + 1] != null && chessBoard[iPos - 1][jPos + 1].ordinal() > 5)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos - 1, jPos + 1));
        } else {
            boolean canGoForward = iPos < 7 && chessBoard[iPos + 1][jPos] == null;
            if (canGoForward)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos + 1, jPos));
            if (iPos == 1 && canGoForward && chessBoard[iPos + 2][jPos] == null)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos + 2, jPos));
            if (jPos > 0 && iPos > 0 && chessBoard[iPos + 1][jPos - 1] != null && chessBoard[iPos + 1][jPos - 1].ordinal() < 6)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos + 1, jPos - 1));
            if (jPos < 7 && iPos > 0 && chessBoard[iPos + 1][jPos + 1] != null && chessBoard[iPos + 1][jPos + 1].ordinal() < 6)
                possibleMovesForPawn.add(new Move(iPos, jPos, iPos + 1, jPos + 1));
        }
        return possibleMovesForPawn;
    }


    private ArrayList<Move> generateMovesForPiece(Piece piece, int iPos, int jPos, boolean whitePawn) {
        ArrayList<Move> possibleMovesForPiece = new ArrayList<>();
        if (PieceData.getCost(piece) == 1) {
            return generateMovesForPawn(iPos, jPos, whitePawn);
        }
        int maxMovesInDirection = PieceData.getMaxMoves(piece);
        int[] pieceDx = PieceData.getDi(piece);
        int[] pieceDy = PieceData.getDj(piece);
        for (int i = 0; i < pieceDx.length; i++) {
            for (int j = 1; j <= maxMovesInDirection; j++) {
                Move currentMove = new Move(iPos, jPos, iPos + j * pieceDx[i], jPos + j * pieceDy[i]);
                if (currentMove.getToI() > 7 || currentMove.getToI() < 0 || currentMove.getToJ() > 7 || currentMove.getToJ() < 0)
                    continue;
                possibleMovesForPiece.add(currentMove);
                if (chessBoard[iPos + j * pieceDx[i]][jPos + j * pieceDy[i]] != null) break;
            }
        }
        return possibleMovesForPiece;
    }

    private ArrayList<Move> generateLegalMovesForPiece(Piece piece, int iPos, int jPos) {
        ArrayList<Move> possibleMovesForPiece = generateMovesForPiece(piece, iPos, jPos, whiteTurn);
        ArrayList<Move> legalMovesForPiece = new ArrayList<>();

        for (Move currentMove : possibleMovesForPiece) {
            if (isLegalMove(currentMove)) {
                legalMovesForPiece.add(currentMove);
            }
        }
        return legalMovesForPiece;
    }


    public ArrayList<Move> generateLegalMovesForCurrentColor() {
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] == null) continue;
                if ((whiteTurn && chessBoard[i][j].ordinal() <= 5) || (!whiteTurn && chessBoard[i][j].ordinal() > 5)) {
                    legalMoves.addAll(generateLegalMovesForPiece(chessBoard[i][j], i, j));
                }
            }
        }
        if (whiteTurn) {
            if (canCastle(0)) legalMoves.add(Move.whiteCastleKingSide());
            if (canCastle(1)) legalMoves.add(Move.whiteCastleQueenSide());
        } else {
            if (canCastle(2)) legalMoves.add(Move.blackCastleKingSide());
            if (canCastle(3)) legalMoves.add(Move.blackCastleQueenSide());
        }
        return legalMoves;
    }

    public String[] boardToString() {
        String[] board = new String[12];
        board[0] = "   a b c d e f g h";
        board[1] = "   ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯";
        for (int i = 0; i < 8; i++) {
            String currentRow = "" + (8 - i) + " |";
            for (int j = 0; j < 8; j++) {
                if (!(chessBoard[i][j] == null)) {
                    currentRow += (PieceData.getPrintName(chessBoard[i][j]) + " ");
                } else {
                    if ((j + i) % 2 == 0) {
                        currentRow += (Color.ANSI_GREEN + "□ " + Color.ANSI_RESET);
                    } else {
                        currentRow += (Color.ANSI_GREEN + "■ " + Color.ANSI_RESET);
                    }
                }
            }
            currentRow += "| " + (8 - i);
            board[i + 2] = currentRow;
        }
        board[10] = "   ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯ ⎯⎯";
        board[11] = "   a b c d e f g h";
        return board;
    }


}
