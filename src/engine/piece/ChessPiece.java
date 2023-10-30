package engine.piece;

import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import engine.util.Assertions;
import engine.util.Board;
import engine.util.Direction;
import engine.util.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class that representing a chess piece.
 * 
 * @version 1.0
 */
public abstract class ChessPiece implements ChessView.UserChoice {

  private final PieceType pieceType;
  private final PlayerColor color;
  private final Direction[] possibleMoves;
  private Point pos;

  protected ChessPiece(
      PlayerColor color,
      int posX, int posY,
      PieceType pieceType,
      Direction... possibleMoves) {
    Assertions.assertNotNull(color, "The color of the piece cannot be null");
    Assertions.assertNotNull(possibleMoves, "Directions cannot be null");
    Assertions.assertNotNull(pieceType, "PieceType cannot be null");
    Assertions.assertTrue(Board.isInBoard(posX, posY), "Position is out of board");
    for (Direction direction : possibleMoves) {
      Assertions.assertTrue(Direction.exists(direction), "Direction must exist");
    }

    this.color = color;
    this.pos = new Point(posX, posY);
    this.pieceType = pieceType;
    this.possibleMoves = Arrays.copyOf(possibleMoves, possibleMoves.length);
  }

  @Override
  public String textValue() {
    return getPieceType().name();
  }

  /**
   * Check if the piece has a possible move to the given position in the given
   * board from its
   * current position.
   *
   * @param dest the destination point
   * @return the move that the piece can do to reach the destination point, null
   *         if the piece has
   *         no possible move
   */
  protected Direction availableMove(Point dest) {
    return availableMoveFrom(this, pos, dest);
  }

  /**
   * Check if the piece has a possible move to the given position in the given
   * board from the
   * given position.
   *
   * @param piece the piece to check
   * @param from  the starting point
   * @param dest  the destination point
   * @return the move that the piece can do to reach the destination point, null
   *         if the piece has
   *         no possible move
   */
  protected static Direction availableMoveFrom(ChessPiece piece, Point from, Point dest) {
    Direction move = Direction.offSetOf(from, dest);
    for (Direction possibleMove : piece.possibleMoves) {
      if (possibleMove.equals(move)) {
        return move;
      }
    }

    return null;
  }

  /**
   * Check if the piece can reach a given position in the given board from its
   * current position
   * with a given move. The move must be a possible move of the piece to have a
   * chance to reach
   * the destination point. This method does not check if the position is occupied
   * by a piece
   * or not.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @param move  the move to use
   * @return true if the piece can reach the destination point, false otherwise
   */
  protected boolean canReach(Board board, Point dest, Direction move) {
    return pieceCanReachFrom(board, this, getPos(), dest, move);
  }

  /**
   * Static method used to check if a piece can reach a destination point from a
   * different
   * position than the current one with a specified move.
   * This method does not check if the position is occupied by a piece or not.
   * Useful to check if
   * the king is in check after a move. It's recommended to use the method
   * availableMoveFromTo in
   * order to pass a valid move for the piece if it exists.
   *
   * @param board the board of the game
   * @param piece the piece to check
   * @param from  the starting point
   * @param dest  the destination point
   * @param move  the move to check
   * @return true if the piece can reach the destination point, false otherwise
   */
  protected static boolean pieceCanReachFrom(
      Board board,
      ChessPiece piece,
      Point from,
      Point dest,
      Direction move) {
    return Direction.exists(move)
        && haveEnoughMoveLengthFrom(piece, from, dest)
        && pathIsClear(board, move, from, dest);
  }

  /**
   * Method used to check if a piece could capture another piece if there was a
   * piece at the
   * destination point. This method is useful to check if the position is attacked
   * by a piece or
   * not. Normally, this method calls the method canReach with the same
   * parameters. However, some
   * pieces can capture with different moves from those used to move.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @param move  the move to use
   * @return true if the piece could capture the piece in the destination point,
   *         false otherwise
   */
  protected boolean isAttacking(Board board, Point dest, Direction move) {
    return canReach(board, dest, move);
  }

  /**
   * Public method used to check if a piece is attacking a piece at a given
   * position in the given
   * board from its current position. The main reason of the existence of this
   * method is to give
   * public access to the method isAttacking without the need to calculate the
   * move to use.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if the piece could capture the piece in the destination point,
   *         false otherwise
   */
  public boolean isAttacking(Board board, Point dest) {
    Assertions.assertNotNull(board, "The board cannot be null");
    Assertions.assertTrue(Board.isInBoard(dest), "Position is out of board");
    return isAttacking(board, dest, availableMove(dest));
  }

  /**
   * Check if the piece can move the given position in the given board. This
   * method check if the
   * position is occupied by a piece or not. Can only move if the destination is
   * not occupied by
   * a piece of the same color.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if the piece can reach the destination point, false otherwise
   */
  public boolean canMoveTo(Board board, Point dest) {
    Assertions.assertNotNull(board, "The board cannot be null");
    return Board.isInBoard(dest) && canReach(board, dest, availableMove(dest))
        && (!board.isOccupied(dest) || !isSameColor(board.getPiece(dest)))
        && !moveImpliesKingInCheck(board, dest);
  }

  /**
   * Method used to get all the directions that the piece can move to.
   *
   * @return the list of all the directions that the piece can move to.
   */
  public final List<Direction> getDirections() {
    return Arrays.asList(possibleMoves);
  }

  /**
   * Method used to check if the move of the piece would put his king in check. If
   * that is the
   * case, the move is not allowed. Can be simplified looking for the enemy
   * through the direction
   * of the king to this piece instead of all enemies.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if the move would put the king in check, false otherwise
   */
  private boolean moveImpliesKingInCheck(Board board, Point dest) {
    Point kingPos = board.getKing(getPlayerColor()).getPos();
    Direction defendingFromMove = Direction.offSetOf(getPos(), kingPos);
    Direction newDefendingMove = Direction.offSetOf(dest, kingPos);
    List<ChessPiece> enemies = board.getAllPieces(getPlayerColor().opposite());
    for (ChessPiece enemy : enemies) {
      // If the enemy is the piece that could be captured by the move,
      // we don't need to check if it can reach the king
      if (enemy.equals(board.getPiece(dest))) {
        continue;
      }

      Direction possibleMoveAttack = enemy.availableMove(kingPos);
      boolean isPinned = isPinned(
          board,
          kingPos,
          enemy,
          possibleMoveAttack,
          defendingFromMove,
          newDefendingMove);

      if (isPinned) {
        return true;
      }
    }

    return false;
  }

  /**
   * Check if a piece is pinned by another piece. A piece is pinned if it can only
   * move in a
   * specific direction to defend the king. If the piece is pinned, it cannot move
   * in any other
   * direction.
   *
   * @param board                 the board of the game
   * @param kingPos               the position of the king
   * @param enemy                 the enemy that could attack the king
   * @param attackingDirection    the move that the enemy could do to attack the
   *                              king
   * @param defendingDirection    the move against which the king is defended
   * @param newDefendingDirection the new move against which the king is defended
   *                              once the piece is moved
   * @return true if the king is attacked by the enemy with a skewer attack, false
   *         otherwise
   */
  private boolean isPinned(
      Board board,
      Point kingPos,
      ChessPiece enemy,
      Direction attackingDirection,
      Direction defendingDirection,
      Direction newDefendingDirection) {
    return null != attackingDirection
        && enemy.possibleLengthDistance() > 1
        && attackingDirection.equals(defendingDirection)
        && !attackingDirection.equals(newDefendingDirection)
        && enemy.isAttacking(board, pos)
        // Simulate the enemy move from the position of the piece to the king position
        && pieceCanReachFrom(
            board, enemy, pos, kingPos,
            availableMoveFrom(enemy, pos, kingPos));
  }

  /**
   * Check if there is no piece between the starting point and the destination
   * point. This method
   * does not check if the starting point and the destination point are occupied
   * by a piece.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if the path is clear, false otherwise
   */
  protected static boolean pathIsClear(
      Board board,
      Direction direction,
      Point from,
      Point dest) {
    Point current = from.withAdded(direction);
    while (!current.equals(dest)) {
      if (board.isOccupied(current)) {
        return false;
      }

      current = current.withAdded(direction);
    }

    return true;
  }

  /**
   * Static method used to check if the piece has enough move length to reach the
   * destination
   * point from the given position.
   *
   * @param piece the piece to check
   * @param from  the starting point
   * @param to    the destination point
   * @return true if the piece has enough move length, false otherwise
   */
  private static boolean haveEnoughMoveLengthFrom(ChessPiece piece, Point from, Point to) {
    int lengthMove = Direction.moveDistance(from, to);
    return lengthMove <= piece.possibleLengthDistance();
  }

  /**
   * Method used to update the position of the piece
   *
   * @param p the new position
   */
  public void move(Point p) {
    Assertions.assertTrue(Board.isInBoard(p), "Position is out of board");
    pos = p;
  }

  /**
   * Method used to check if the piece is the same color as the given piece
   *
   * @param piece the piece to check
   * @return true if the piece is the same color, false otherwise
   */
  public final boolean isSameColor(ChessPiece piece) {
    return null != piece && color.equals(piece.color);
  }

  /**
   * Method used to get the maximum length of the move of the piece.
   *
   * @return the maximum length of the move of the piece
   */
  protected abstract int possibleLengthDistance();

  /**
   * Method used to have the type of the piece.
   *
   * @return the type of the piece
   */
  public final PieceType getPieceType() {
    return pieceType;
  }

  /**
   * Method used to have the color of the piece.
   *
   * @return the color of the piece
   */
  public final PlayerColor getPlayerColor() {
    return color;
  }

  /**
   * Method used to have the position of the piece.
   *
   * @return the position of the piece
   */
  public final Point getPos() {
    return pos;
  }

  /**
   * Method used to get the position x of the piece.
   *
   * @return the position x of the piece
   */
  public final int getX() {
    return pos.x();
  }

  /**
   * Method used to get the position y of the piece.
   *
   * @return the position y of the piece
   */
  public final int getY() {
    return pos.y();
  }

  @Override
  public String toString() {
    return textValue() + ":" + getPlayerColor();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ChessPiece that = (ChessPiece) o;
    if (pieceType != that.pieceType)
      return false;
    if (color != that.color)
      return false;
    return Objects.equals(pos, that.pos);
  }

  @Override
  public int hashCode() {
    int result = pieceType != null ? pieceType.hashCode() : 0;
    result = 31 * result + (color != null ? color.hashCode() : 0);
    result = 31 * result + (pos != null ? pos.hashCode() : 0);
    return result;
  }
}
