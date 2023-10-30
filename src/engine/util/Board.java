package engine.util;

import chess.PlayerColor;
import engine.piece.*;

import java.util.*;

/**
 * Class representing a Chess Board in a chess game.
 * 
 * @version 1.0
 */
public class Board {

  public static final int BOARD_SIZE = 8;

  private final ChessPiece[][] pieces = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
  private final List<PromotablePiece> promotablePieces = new LinkedList<>();
  private final King[] kings = new King[PlayerColor.values().length];
  private final Point[] lastMove = new Point[2];

  /**
   * Constructor for a new Board in the initial state.
   */
  public Board() {
    fillBoard();
  }

  /**
   * Method used to reset the board to the initial state.
   */
  public void reset() {
    for (ChessPiece[] row : pieces) {
      Arrays.fill(row, null);
    }

    Arrays.fill(kings, null);
    Arrays.fill(lastMove, null);
    promotablePieces.clear();
    fillBoard();
  }

  /**
   * Method used to get the piece at the given position.
   * 
   * @param pos the position of the piece
   * @return the piece at the given position
   */
  public ChessPiece getPiece(Point pos) {
    return getPiece(pos.x(), pos.y());
  }

  /**
   * Method used to get a piece at a given position. If there is no piece at the
   * given position,
   * it returns null.
   *
   * @param x The x coordinate of the piece.
   * @param y The y coordinate of the piece.
   * @return The piece at the given position, or null if there is no piece.
   * @throws IllegalArgumentException If the given coordinates are not valid.
   */
  public ChessPiece getPiece(int x, int y) {
    Assertions.assertTrue(isInBoard(x, y), "Position is out of board");
    return this.pieces[x][y];
  }

  /**
   * Method used to set the last move done on the board.
   *
   * @param from The position from which the piece was moved.
   * @param to   The position to which the piece was moved.
   * @throws IllegalArgumentException If one of the given positions is not valid.
   */
  public void setLastMove(Point from, Point to) {
    Assertions.assertTrue(
        isInBoard(from) && isInBoard(to),
        "Position is out of board");
    this.lastMove[0] = from;
    this.lastMove[1] = to;
  }

  /**
   * Get the position from which the last move was done.
   *
   * @return The position from which the last move was done. Null if no move was
   *         done.
   */
  public Point getLastMoveFrom() {
    return this.lastMove[0];
  }

  /**
   * Get the position to which the last move was done.
   *
   * @return The position to which the last move was done. Null if no move was
   *         done.
   */
  public Point getLastMoveTo() {
    return this.lastMove[1];
  }

  /**
   * Method used to check if a position is occupied by a piece.
   *
   * @param p The position to check.
   * @return True if the position is occupied by a piece, false otherwise.
   */
  public boolean isOccupied(Point p) {
    return isInBoard(p) && null != getPiece(p);
  }

  /**
   * Method used to check if a position is in the board.
   *
   * @param p The position to check.
   * @return True if the position is in the board, false otherwise.
   */
  public static boolean isInBoard(Point p) {
    return p.x() >= 0 && p.x() < Board.BOARD_SIZE && p.y() >= 0 && p.y() < Board.BOARD_SIZE;
  }

  /**
   * Method used to check if a position is in the board.
   *
   * @param x The x coordinate of the position to check.
   * @param y The y coordinate of the position to check.
   * @return True if the position is in the board, false otherwise.
   */
  public static boolean isInBoard(int x, int y) {
    return isInBoard(new Point(x, y));
  }

  /**
   * Method used to add a piece to the board. If the position is already occupied,
   * it will be
   * replaced.
   *
   * @param piece The piece to add.
   * @throws IllegalArgumentException If the given piece is null or if the
   *                                  position is out
   *                                  of the board.
   */
  public void addPiece(ChessPiece piece) {
    Assertions.assertNotNull(piece, "Piece cannot be null");
    Assertions.assertTrue(isInBoard(piece.getPos()), "Piece not valid");
    this.pieces[piece.getX()][piece.getY()] = piece;
  }

  /**
   * Method used to remove a piece from the board.
   *
   * @param piece The piece to remove.
   * @throws IllegalArgumentException If the piece is not in the board.
   */
  public void removePiece(ChessPiece piece) {
    Assertions.assertTrue(
        getPiece(piece.getPos()).equals(piece),
        "Piece is not on the board");

    this.pieces[piece.getX()][piece.getY()] = null;
  }

  /**
   * Method used to get the king of a given color.
   *
   * @param color The color of the king to get.
   * @return The king of the given color.
   */
  public King getKing(PlayerColor color) {
    for (King king : kings) {
      if (king.getPlayerColor() == color) {
        return king;
      }
    }

    return null;
  }

  /**
   * Method used to fill the board with the initial pieces.
   */
  private void fillBoard() {
    for (PlayerColor color : PlayerColor.values()) {
      int line = PlayerColor.WHITE == color ? 0 : BOARD_SIZE - 1;
      int pawnLine = PlayerColor.WHITE == color ? line + 1 : line - 1;
      Rook r1 = new Rook(color, 0, line);
      Rook r2 = new Rook(color, 7, line);
      King k = new King(color, 4, line, r1, r2);
      addPiece(r1);
      addPiece(r2);
      addPiece(k);
      addPiece(new Knight(color, 1, line));
      addPiece(new Bishop(color, 2, line));
      addPiece(new Queen(color, 3, line));
      addPiece(new Bishop(color, 5, line));
      addPiece(new Knight(color, 6, line));

      for (int j = 0; j < Board.BOARD_SIZE; j++) {
        Pawn p = new Pawn(color, j, pawnLine);
        addPiece(p);
        promotablePieces.add(p);
      }

      kings[color.ordinal()] = k;
    }
  }

  /**
   * Method used to get all the pieces of a given color.
   *
   * @param playerTurn The color of the pieces to get.
   * @return A list of all the pieces of the given color.
   */
  public List<ChessPiece> getAllPieces(PlayerColor playerTurn) {
    ArrayList<ChessPiece> playerPieces = new ArrayList<>();
    for (ChessPiece[] row : pieces) {
      for (ChessPiece piece : row) {
        if (null != piece && piece.getPlayerColor() == playerTurn) {
          playerPieces.add(piece);
        }
      }
    }

    return playerPieces;
  }

  /**
   * Returns the list of all the pieces that can be promoted.
   * 
   * @return the list of all the pieces that can be promoted.
   */
  public List<PromotablePiece> getPromotablePieces() {
    return Collections.unmodifiableList(promotablePieces);
  }

  /**
   * Method used to get all the pieces of all the colors.
   *
   * @return A list of all the pieces of all the colors.
   */
  public List<List<ChessPiece>> getAllPieces() {
    List<List<ChessPiece>> allPieces = new ArrayList<>();
    for (PlayerColor color : PlayerColor.values()) {
      allPieces.add(getAllPieces(color));
    }
    return allPieces;
  }
}
