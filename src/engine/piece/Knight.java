package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.util.Direction;

/**
 * Class representing a Knight in a chess game.
 * 
 * @version 1.0
 */
public final class Knight extends ChessPiece {

  private static final PieceType PIECE_TYPE = PieceType.KNIGHT;
  private static final int MAX_LENGTH_MOVE = 1;
  private static final Direction[] POSSIBLE_MOVES = {
      Direction.L_EAST_NORTH, Direction.L_NORTH_EAST,
      Direction.L_EAST_SOUTH, Direction.L_SOUTH_EAST,
      Direction.L_WEST_SOUTH, Direction.L_SOUTH_WEST,
      Direction.L_WEST_NORTH, Direction.L_NORTH_WEST
  };

  /**
   * Creates a new Knight with the given color and position.
   *
   * @param color the color of the Knight
   * @param posX  the x position of the Knight
   * @param posY  the y position of the Knight
   */
  public Knight(PlayerColor color, int posX, int posY) {
    super(color, posX, posY, PIECE_TYPE, POSSIBLE_MOVES);
  }

  /**
   * Returns the maximum length of a move of the Knight.
   *
   * @return the maximum length of a move of the Knight
   */
  @Override
  protected int possibleLengthDistance() {
    return MAX_LENGTH_MOVE;
  }
}
