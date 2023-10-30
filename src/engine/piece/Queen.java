package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.util.Board;
import engine.util.Direction;

/**
 * Class representing a Queen in a chess game.
 * 
 * @version 1.0
 */
public final class Queen extends ChessPiece {

  private static final PieceType PIECE_TYPE = PieceType.QUEEN;
  private static final int MAX_LENGTH_MOVE = Board.BOARD_SIZE - 1;
  private static final Direction[] POSSIBLE_MOVES = {
      Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST,
      Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_EAST, Direction.SOUTH_WEST
  };

  public Queen(PlayerColor color, int posX, int posY) {
    super(color, posX, posY, PIECE_TYPE, POSSIBLE_MOVES);
  }

  @Override
  protected int possibleLengthDistance() {
    return MAX_LENGTH_MOVE;
  }

}
