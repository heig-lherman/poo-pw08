package engine.piece;

import chess.PieceType;
import chess.PlayerColor;
import engine.util.Assertions;
import engine.util.Board;
import engine.util.Direction;
import engine.util.Point;

import java.util.*;

/**
 * Class representing a King in a chess game.
 * 
 * @version 1.0
 */
public final class King extends SpecialFirstMovePiece implements CastlingPiece {

  private static final PieceType PIECE_TYPE = PieceType.KING;
  private static final int MAX_LENGTH_MOVE = 1;
  private static final int LENGTH_CASTLING = 2;
  private static final Direction[] POSSIBLE_MOVES = {
      Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST,
      Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.SOUTH_EAST, Direction.SOUTH_WEST
  };

  private final List<ChessPiece> inCheckBy = new LinkedList<>();
  private final List<CastlingPiece> castlingPieces = new LinkedList<>();

  /**
   * Creates a new King with the given color and position.
   *
   * @param color the color of the King
   * @param posX  the x position of the King
   * @param posY  the y position of the King
   */
  public King(PlayerColor color, int posX, int posY) {
    super(color, posX, posY, PIECE_TYPE, POSSIBLE_MOVES);
  }

  /**
   * Constructor for a King with the given color and position and the castling
   * pieces
   * that can be used for castling.
   *
   * @param color          the color of the King
   * @param posX           the x position of the King
   * @param posY           the y position of the King
   * @param castlingPieces the castling pieces that can be used for castling
   */
  public King(PlayerColor color, int posX, int posY, CastlingPiece... castlingPieces) {
    this(color, posX, posY);
    this.castlingPieces.addAll(Arrays.asList(castlingPieces));
  }

  /**
   * Method used to check if the move is a castling move. Only check if the length
   * of the move
   * is equal to required length.
   *
   * @param to the destination point
   * @return true if the move is a castling move, false otherwise
   */
  public boolean isCastling(Point to) {
    return (to.x() == getX() + LENGTH_CASTLING || to.x() == getX() - LENGTH_CASTLING);
  }

  /**
   * Method used to check if the castling is possible. This piece should be able
   * to do a
   * castling move and the castling piece should be able to do it too.
   *
   * @param board the board of the game
   * @param to    the destination point
   * @return true if the castling is possible, false otherwise
   */
  private boolean castlingCanBeDone(Board board, Point to) {
    ChessPiece p = board.getPiece(getCastlingPiecePos(to));
    Direction moveDirection = availableMove(to);
    boolean cannotCastle = null == p || null == moveDirection
        || p.availableMove(to) != moveDirection.opposite()
        || !isSameColor(p)
        || !canCastle()
        || !pathIsClear(board, availableMove(to), getPos(), p.getPos())
        || pathIsInCheck(board, to);

    if (cannotCastle) {
      return false;
    }

    CastlingPiece cPiece = pieceIsPossibleCastlingPiece(p);
    return null != cPiece && cPiece.canCastle();
  }

  /**
   * Method used to get the castling piece position. The castling can be done only
   * if the
   * castling piece is in the same row of the initial position of the king and in
   * the first
   * or last column of the board.
   *
   * @param to the destination point
   * @return the castling piece position
   */
  public Point getCastlingPiecePos(Point to) {
    int castlingWithX = getX() - to.x() > 0 ? 0 : Board.BOARD_SIZE - 1;
    return new Point(castlingWithX, getY());
  }

  /**
   * Method used to check if the piece is a possible castling piece.
   *
   * @param piece the piece to check
   * @return the castling piece if the piece is a castling piece with which
   *         this piece can do a castling, null otherwise
   */
  private CastlingPiece pieceIsPossibleCastlingPiece(ChessPiece piece) {
    for (CastlingPiece castlingPiece : castlingPieces) {
      if (castlingPiece.equals(piece)) {
        return castlingPiece;
      }
    }
    return null;
  }

  /**
   * Method to have the list of pieces that are putting this king in check.
   *
   * @return the list of pieces that are putting this piece in check. An empty
   *         list
   *         if the piece is not in check.
   */
  public List<ChessPiece> getInCheckBy() {
    return Collections.unmodifiableList(inCheckBy);
  }

  /**
   * Method used to add a piece to the list of pieces that are putting this piece
   * in check.
   *
   * @param piece the piece to add
   */
  public void addInCheckBy(ChessPiece piece) {
    Assertions.assertTrue(
        !isSameColor(piece),
        "The piece should be of the opposite color");
    inCheckBy.add(piece);
  }

  /**
   * Method used to check if the piece is in check.
   *
   * @return true if the piece is in check, false otherwise
   */
  public boolean isInCheck() {
    return inCheckBy.size() != 0;
  }

  /**
   * Method used to remove all the pieces that are putting this piece in check.
   */
  public void clearInCheckBy() {
    inCheckBy.clear();
  }

  /**
   * Check if the piece can reach a given position in the given board from its
   * current position
   * with a given move. The move must be a possible move of the piece to have a
   * chance to reach
   * the destination point. This method also check if the destination point is not
   * in check.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @param move  the move to use
   * @return true if the piece can reach the destination point, false otherwise
   */
  @Override
  protected boolean canReach(Board board, Point dest, Direction move) {
    return super.canReach(board, dest, move) && !destIsInCheck(board, dest);
  }

  /**
   * Check if the piece can move the given position in the given board. This
   * method check if the
   * position is occupied by a piece or not. Can only move if the destination is
   * not occupied by
   * a piece of the same color. This method also check the move is a castling move
   * and if the
   * castling is possible.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if the piece can move to the destination point, false otherwise
   */
  @Override
  public boolean canMoveTo(Board board, Point dest) {
    return super.canMoveTo(board, dest)
        || (Board.isInBoard(dest) && isCastling(dest) && castlingCanBeDone(board, dest));
  }

  /**
   * Method used to get the maximum length of the move of the king.
   *
   * @return the maximum length of the move of the king
   */
  @Override
  protected int possibleLengthDistance() {
    return MAX_LENGTH_MOVE;
  }

  /**
   * Method used to check if the piece can do a castling.
   *
   * @return true if the piece can do a castling, false otherwise
   */
  @Override
  public boolean canCastle() {
    return !hasMoved() && !isInCheck();
  }

  /**
   * Method used to check if a piece could capture another piece if there was a
   * piece at the
   * destination point. This method is useful to check if the position is attacked
   * by a piece
   * or not. Normally, this method calls the method canReach with the same
   * parameters. However,
   * canReach check if the destination point is not in check. This method should
   * not check if
   * the destination point is in check.
   *
   * @param board the board of the game
   * @param dest  the destination point
   * @return true if the piece could capture a piece if there was a piece at
   *         the destination point, false otherwise
   */
  @Override
  public boolean isAttacking(Board board, Point dest) {
    return super.canReach(board, dest, availableMove(dest));
  }

  /**
   * Method used to check if the destination point is in check.
   *
   * @param board the board of the game
   * @param to    the destination point
   * @return true if the destination point is in check, false otherwise
   */
  private boolean destIsInCheck(Board board, Point to) {
    List<ChessPiece> enemies = board.getAllPieces(getPlayerColor().opposite());
    Direction movingDirection = Direction.offSetOf(getPos(), to);
    for (ChessPiece e : enemies) {
      Direction attackingDirection = e.availableMove(to);
      if (e.isAttacking(board, to) || isInCheck() && inCheckBy.contains(e)
          && null != attackingDirection && attackingDirection.equals(movingDirection)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Method used to check if there is a piece attacking one of the position in the
   * path
   * of the move.
   *
   * @param board the board of the game
   * @param to    the destination point
   * @return true if there is a piece attacking one of the position in the path of
   *         the move,
   *         false otherwise
   */
  private boolean pathIsInCheck(Board board, Point to) {
    Direction movingDirection = Direction.offSetOf(getPos(), to);
    Point currentPos = getPos();

    do {
      currentPos = currentPos.withAdded(movingDirection);
      if (destIsInCheck(board, currentPos)) {
        return true;
      }
    } while (!currentPos.equals(to));

    return false;
  }
}
