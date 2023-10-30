package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.util.Direction;
import engine.util.Point;
import engine.util.Board;

/**
 * Class representing a Pawn in a chess game.
 * 
 * @version 1.0
 */
public final class Pawn extends SpecialFirstMovePiece implements PromotablePiece {

  private static final PieceType PIECE_TYPE = PieceType.PAWN;
  private static final int BEGIN_LENGTH_MOVE = 2;
  private static final int NORMAL_LENGTH_MOVE = 1;
  private static final Direction[] NORTH_POSSIBLE_MOVES = {
      Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST
  };
  private static final Direction[] SOUTH_POSSIBLE_MOVES = {
      Direction.SOUTH, Direction.SOUTH_EAST, Direction.SOUTH_WEST
  };

  /**
   * Creates a new Pawn with the given color and position.
   *
   * @param color the color of the Pawn
   * @param posX  the x position of the Pawn
   * @param posY  the y position of the Pawn
   */
  public Pawn(PlayerColor color, int posX, int posY) {
    super(color, posX, posY, PIECE_TYPE, getPossibleMoves(color));
  }

  /**
   * Method used to determine the possible moves of the Pawn. North moves are
   * possible if the Pawn
   * is white, south moves are possible if the Pawn is black.
   *
   * @param color the color of the Pawn
   * @return the possible moves of the Pawn
   */
  private static Direction[] getPossibleMoves(PlayerColor color) {
    return color == PlayerColor.WHITE ? NORTH_POSSIBLE_MOVES : SOUTH_POSSIBLE_MOVES;
  }

  @Override
  protected boolean isAttacking(Board board, Point dest, Direction move) {
    return Direction.exists(move)
        && move.isDiagonal()
        && Direction.moveDistance(getPos(), dest) == NORMAL_LENGTH_MOVE;
  }

  @Override
  protected boolean canReach(Board board, Point dest, Direction move) {
    if (!Direction.exists(move)
        || move.isDiagonal()
            && Direction.moveDistance(getPos(), dest) != NORMAL_LENGTH_MOVE) {
      return false;
    }

    return super.canReach(board, dest, move)
        && (move.isDiagonal() == board.isOccupied(dest) || isEnPassant(board, dest, move));
  }

  /**
   * Method used to check if the move is an en passant move. The move should be
   * diagonal and the
   * last move of the opponent should be a double move of a Pawn and the
   * destination of the move
   * should be the same if the opponent's Pawn had moved one step forward.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @param move  the direction of the move
   * @return true if it's en passant, false otherwise
   */
  private boolean isEnPassant(Board board, Point dest, Direction move) {
    Point lastMoveTo = board.getLastMoveTo();
    if (null == lastMoveTo) {
      return false;
    }
    ChessPiece pieceMovedBefore = board.getPiece(lastMoveTo);
    return move.isDiagonal() && pieceMovedBefore instanceof Pawn p
        && Direction.moveDistance(board.getLastMoveFrom(), lastMoveTo) == BEGIN_LENGTH_MOVE
        && isNextTo(p) && dest.x() == lastMoveTo.x();
  }

  /**
   * Method used to check if the move is an en passant move. The move should be
   * diagonal and the
   * last move of the opponent should be a double move of a Pawn and the
   * destination of the move
   * should be the same if the opponent's Pawn had moved one step forward.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if it's en passant, false otherwise
   */
  public boolean isEnPassant(Board board, Point dest) {
    return isEnPassant(board, dest, availableMove(dest));
  }

  /**
   * Method to check if the piece is next to the given piece in the x-axis and in
   * the same y-axis.
   *
   * @param piece the piece to check
   * @return true if the piece is next to the given piece, false otherwise
   */
  private boolean isNextTo(ChessPiece piece) {
    return Math.abs(piece.getX() - getX()) == 1 && piece.getY() == getY();
  }

  @Override
  protected int possibleLengthDistance() {
    return hasMoved() ? NORMAL_LENGTH_MOVE : BEGIN_LENGTH_MOVE;
  }

  @Override
  public boolean canPromote() {
    return getY() == (getPossibleMoves(getPlayerColor()) == NORTH_POSSIBLE_MOVES
        ? Board.BOARD_SIZE - 1
        : 0);
  }
}
