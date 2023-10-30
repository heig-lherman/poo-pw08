package test;

import chess.PieceType;
import chess.PlayerColor;
import engine.piece.*;
import engine.util.*;

import static test.TestFramework.*;

public class ChessPieceTest {

    public static void main(String[] args) {
        // Create a board and rearrange in a position for the tests
        Board board = new Board();
        board.removePiece(board.getPiece(0, 1));
        board.removePiece(board.getPiece(1, 1));
        board.removePiece(board.getPiece(0, 6));
        board.removePiece(board.getPiece(5, 7));
        moveChessPiece(board.getPiece(6, 0), board, new Point(5, 2));
        moveChessPiece(board.getPiece(0, 0), board, new Point(0, 2));
        moveChessPiece(board.getPiece(2, 0), board, new Point(1, 1));
        moveChessPiece(board.getPiece(0, 7), board, new Point(0, 5));

        // Test the pawn moves
        ChessPiece whitePawn = board.getPiece(4, 1);
        ChessPiece blackPawn = board.getPiece(5, 6);
        ChessPiece blockedPawn = board.getPiece(5, 1);
        assertTrue(whitePawn instanceof Pawn);
        assertTrue(blackPawn instanceof Pawn);
        assertTrue(blockedPawn instanceof Pawn);

        // Pawn can move 2 on the first move
        assertTrue(whitePawn.canMoveTo(board, new Point(4, 3)));
        moveChessPiece(whitePawn, board, new Point(4, 3));
        // Pawn can move 2 only for the first move
        assertFalse(whitePawn.canMoveTo(board, new Point(4, 5)));
        // Pawn can not go backward
        assertFalse(whitePawn.canMoveTo(board, new Point(4, 2)));
        // Pawn can not go horizontally
        assertFalse(whitePawn.canMoveTo(board, new Point(5, 3)));
        // Pawn can not go diagonally
        assertFalse(whitePawn.canMoveTo(board, new Point(6, 4)));
        // Pawn can only move 1
        assertTrue(whitePawn.canMoveTo(board, new Point(4, 4)));
        moveChessPiece(whitePawn, board, new Point(4, 4));
        moveChessPiece(blackPawn, board, new Point(5, 4));
        assertEquals(blackPawn, board.getPiece(board.getLastMoveTo()));
        assertTrue(((Pawn) whitePawn).isEnPassant(board, new Point(5, 5)));
        // Pawn eat "En passant"
        assertTrue(whitePawn.canMoveTo(board, new Point(5, 5)));
        board.removePiece(blackPawn);
        moveChessPiece(whitePawn, board, new Point(5, 5));
        // Pawn can eat diagonally
        assertTrue(whitePawn.canMoveTo(board, new Point(6, 6)));
        moveChessPiece(whitePawn, board, new Point(6, 6));

        assertEquals(blockedPawn.getPlayerColor(), board.getPiece(5, 2).getPlayerColor());
        // Pawn is blocked by an allied piece
        assertFalse(blockedPawn.canMoveTo(board, new Point(5, 2)));
        // Pawn can only promote at the other end of the board
        assertFalse(((Pawn) blockedPawn).canPromote());
        // Move the pawn in a position allowing promotion
        moveChessPiece(blockedPawn, board, new Point(0, 7));
        assertTrue(((Pawn) blockedPawn).canPromote());

        // Test the Rook
        ChessPiece leftRook = board.getPiece(0, 2);
        ChessPiece rightRook = board.getPiece(7, 0);
        assertTrue(leftRook instanceof Rook);
        assertTrue(rightRook instanceof Rook);

        // Rook can go vertically (down)
        assertTrue(leftRook.canMoveTo(board, new Point(0, 0)));
        // Rook can go vertically (up)
        assertTrue(leftRook.canMoveTo(board, new Point(0, 3)));
        // Rook can go horizontally (right)
        assertTrue(leftRook.canMoveTo(board, new Point(2, 2)));
        // Rook can go horizontally (left)
        assertTrue(rightRook.canMoveTo(board, new Point(6, 0)));

        assertEquals(null, board.getPiece(3, 3));
        // Rook can not go diagonally
        assertFalse(leftRook.canMoveTo(board, new Point(3, 3)));
        assertEquals(rightRook.getPlayerColor(), board.getPiece(7, 1).getPlayerColor());
        // Rook is blocked by an allied piece
        assertFalse(rightRook.canMoveTo(board, new Point(7, 4)));

        moveChessPiece(leftRook, board, new Point(0, 3));

        // Test the Bishop
        ChessPiece leftBishop = board.getPiece(1, 1);
        ChessPiece rightBishop = board.getPiece(5, 0);
        assertTrue(leftBishop instanceof Bishop);
        assertTrue(rightBishop instanceof Bishop);

        // Bishop can go diagonally
        assertTrue(leftBishop.canMoveTo(board, new Point(0, 0)));
        // Bishop can go diagonally
        assertTrue(leftBishop.canMoveTo(board, new Point(2, 0)));
        // Bishop can go diagonally
        assertTrue(leftBishop.canMoveTo(board, new Point(3, 3)));
        // Bishop can go diagonally
        assertTrue(rightBishop.canMoveTo(board, new Point(4, 1)));

        assertEquals(null, board.getPiece(1, 2));
        // Bishop can not go vertically
        assertFalse(leftBishop.canMoveTo(board, new Point(1, 2)));
        assertEquals(null, board.getPiece(0, 1));
        // Bishop can not go horizontally
        assertFalse(leftBishop.canMoveTo(board, new Point(0, 1)));
        assertEquals(rightBishop.getPlayerColor(), board.getPiece(7, 1).getPlayerColor());
        // Bishop is blocked by an allied piece
        assertFalse(rightBishop.canMoveTo(board, new Point(7, 4)));

        // Test the Knight
        ChessPiece leftKnight = board.getPiece(1, 0);
        ChessPiece rightKnight = board.getPiece(5, 2);
        assertTrue(leftKnight instanceof Knight);
        assertTrue(rightKnight instanceof Knight);

        // Knight can go in a L-shape
        assertTrue(leftKnight.canMoveTo(board, new Point(2, 2)));
        assertTrue(leftKnight.canMoveTo(board, new Point(0, 2)));
        assertTrue(rightKnight.canMoveTo(board, new Point(3, 3)));
        assertTrue(rightKnight.canMoveTo(board, new Point(7, 3)));
        moveChessPiece(rightKnight, board, new Point(3, 3));
        assertTrue(rightKnight.canMoveTo(board, new Point(1, 2)));
        assertTrue(rightKnight.canMoveTo(board, new Point(5, 2)));

        assertEquals(null, board.getPiece(0, 1));
        // Knight can not go diagonally
        assertFalse(leftKnight.canMoveTo(board, new Point(0, 1)));
        assertEquals(null, board.getPiece(1, 3));
        // Knight can not go vertically
        assertFalse(rightKnight.canMoveTo(board, new Point(1, 3)));
        assertEquals(null, board.getPiece(5, 3));
        // Knight can not go horizontally
        assertFalse(rightKnight.canMoveTo(board, new Point(5, 3)));

        // Test the Queen
        ChessPiece whiteQueen = board.getPiece(3, 0);
        assertTrue(whiteQueen instanceof Queen);

        // Queen can move diagonally
        assertTrue(whiteQueen.canMoveTo(board, new Point(4, 1)));
        moveChessPiece(whiteQueen, board, new Point(4, 1));
        // Queen can move vertically
        assertTrue(whiteQueen.canMoveTo(board, new Point(4, 2)));
        moveChessPiece(whiteQueen, board, new Point(4, 2));
        assertTrue(whiteQueen.canMoveTo(board, new Point(2, 2)));

        assertEquals(whiteQueen.getPlayerColor(), board.getPiece(3, 3).getPlayerColor());
        // Queen is blocked by an allied piece
        assertFalse(whiteQueen.canMoveTo(board, new Point(2, 4)));

        // Test the King
        ChessPiece whiteKing = board.getPiece(4, 0);
        ChessPiece blackKing = board.getPiece(4, 7);
        assertTrue(whiteKing instanceof King);
        assertTrue(blackKing instanceof King);
        assertEquals(board.getKing(PlayerColor.WHITE), whiteKing);
        assertEquals(board.getKing(PlayerColor.BLACK), blackKing);

        // King can move vertically
        assertTrue(whiteKing.canMoveTo(board, new Point(4, 1)));
        moveChessPiece(whiteKing, board, new Point(4, 1));
        // King can move diagonally
        assertTrue(whiteKing.canMoveTo(board, new Point(5, 2)));
        moveChessPiece(whiteKing, board, new Point(5, 2));
        // King can only move 1
        assertFalse(whiteKing.canMoveTo(board, new Point(7, 2)));
        assertEquals(whiteKing.getPlayerColor(), board.getPiece(4, 2).getPlayerColor());
        // King is blocked by an allied piece
        assertFalse(whiteKing.canMoveTo(board, new Point(4, 2)));
        moveChessPiece(whiteKing, board, new Point(6, 3));
        // King can move horizontally
        assertTrue(whiteKing.canMoveTo(board, new Point(6, 2)));

        assertTrue(((King) blackKing).canCastle());
        assertTrue(((King) blackKing).isCastling(new Point(2, 7)));
        // King can not castle if the Rook moved
        assertFalse(blackKing.canMoveTo(board, new Point(2, 7)));
        assertTrue(((King) blackKing).isCastling(new Point(6, 7)));
        // King can not castle if blocked by allied piece
        assertFalse(blackKing.canMoveTo(board, new Point(6, 7)));
        board.removePiece(board.getPiece(6, 7));
        // King can not castle if one pos is check
        assertFalse(blackKing.canMoveTo(board, new Point(6, 7)));
        board.removePiece(whitePawn);
        assertTrue(blackKing.canMoveTo(board, new Point(6, 7)));

        System.out.println("All tests passed!");
    }

    private static void moveChessPiece(ChessPiece piece, Board board, Point newPos) {
        board.setLastMove(piece.getPos(), newPos);
        board.removePiece(piece);
        piece.move(newPos);
        board.addPiece(piece);
    }
}
