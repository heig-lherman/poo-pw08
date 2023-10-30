package engine;

import chess.ChessController;
import chess.ChessView;
import chess.ChessView.UserChoice;
import chess.PlayerColor;
import engine.piece.*;
import engine.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the game engine used to control a chess game from the
 * beginning to the end.
 * 
 * @version 1.0
 */
public class GameManager implements ChessController {

  private Map<ChessPiece, List<Point>> mandatoryAdversaryMoves = new HashMap<>();
  protected final Board board = new Board();
  protected ChessView chessView;
  private int turn;

  /**
   * Constructor for a new GameManager in the initial state of a chess game.
   */
  public GameManager() {
  }

  @Override
  public void newGame() {
    board.reset();
    mandatoryAdversaryMoves.clear();
    turn = 0;

    if (null != chessView) {
      insertPiecesInView();
    }
  }

  @Override
  public void start(ChessView view) {
    Assertions.assertNotNull(view, "View must not be null");
    chessView = view;

    insertPiecesInView();
    chessView.startView();
  }

  @Override
  public boolean move(int fromX, int fromY, int toX, int toY) {
    Assertions.assertTrue(
        Board.isInBoard(fromX, fromY) && Board.isInBoard(toX, toY),
        "Invalid move");

    Point from = new Point(fromX, fromY);
    Point to = new Point(toX, toY);
    ChessPiece piece = board.getPiece(from);

    if (!movePreconditions(piece, to)) {
      return false;
    }

    boolean specialMove = executeSpecialMoves(piece, to);
    movePiece(piece, to);
    promoteIfNeeded(piece);
    if (!specialMove) {
      board.setLastMove(from, piece.getPos());
    }

    postMoveActions();
    return true;
  }

  /**
   * Check the preconditions of the desired move and return true if the move is
   * valid.
   *
   * @param piece The piece to move
   * @param to    The destination
   * @return True if the move is valid, false otherwise
   */
  private boolean movePreconditions(ChessPiece piece, Point to) {
    return null != piece
        && !piece.getPos().equals(to)
        && piece.getPlayerColor().equals(playerTurn())
        && piece.canMoveTo(board, to)
        && (!board.getKing(piece.getPlayerColor()).isInCheck()
            || mandatoryAdversaryMoves.get(piece).contains(to));
  }

  /**
   * Method used to remove a piece from the board and the chessView.
   *
   * @param p The piece to remove
   */
  protected void removePiece(ChessPiece p) {
    board.removePiece(p);
    chessView.removePiece(p.getX(), p.getY());
  }

  /**
   * Method used to insert a piece in the board and the chessView.
   *
   * @param p The piece to insert
   */
  protected void insertPiece(ChessPiece p) {
    board.addPiece(p);
    chessView.putPiece(p.getPieceType(), p.getPlayerColor(), p.getX(), p.getY());
  }

  /**
   * Method used to make actions after the move.
   */
  private void postMoveActions() {
    boolean check = checkIfAdversaryKingIsInCheck();
    boolean checkMate = !mandatoryAdversaryMoves.isEmpty()
        && mandatoryAdversaryMoves.values().stream().allMatch(List::isEmpty);
    boolean impossibleToCheckMate = isInsufficientMaterial();
    boolean stalemate = isStalemate();
    displayMessages(checkMate, check, stalemate, impossibleToCheckMate);

    if (!checkMate && !impossibleToCheckMate && !stalemate) {
      turn++;
    }

    postGameActions(checkMate, stalemate, impossibleToCheckMate);
  }

  /**
   * Method used to display messages after the move using the chessView.
   *
   * @param checkMate             indicates if the adversary king is in checkmate
   * @param check                 indicates if the adversary king is in check
   * @param stalemate             indicates if there is a stalemate
   * @param impossibleOfCheckMate indicates if there is an impossibility of
   *                              checkmate
   */
  private void displayMessages(
      boolean checkMate,
      boolean check,
      boolean stalemate,
      boolean impossibleOfCheckMate) {
    if (checkMate) {
      chessView.displayMessage(ChessString.CHECKMATE);
      return;
    }

    if (check) {
      chessView.displayMessage(ChessString.CHECK);
      return;
    }

    if (stalemate) {
      chessView.displayMessage(ChessString.STALEMATE);
      return;
    }

    if (impossibleOfCheckMate) {
      chessView.displayMessage(ChessString.INSUFFICIENT_MATERIAL);
      return;
    }

    chessView.displayMessage(ChessString.playerToMove(playerTurn().opposite()));
  }

  /**
   * Method used to make actions after a checkmate or a pat.
   *
   * @param checkMate             indicates if the adversary king is in checkmate
   * @param pat                   indicates if there is a pat
   * @param impossibleOfCheckMate indicates if there is an impossibility of
   *                              checkmate
   */
  private void postGameActions(boolean checkMate, boolean pat, boolean impossibleOfCheckMate) {
    if (!checkMate && !pat && !impossibleOfCheckMate) {
      return;
    }

    String header = checkMate
        ? ChessString.playerWins(playerTurn())
        : (pat ? ChessString.STALEMATE : ChessString.INSUFFICIENT_MATERIAL);
    String[] options = { ChessString.YES, ChessString.NO };
    UserChoice[] choices = new UserChoice[options.length];

    for (int i = 0; i < choices.length; i++) {
      int finalI = i;
      choices[finalI] = () -> options[finalI];
    }

    UserChoice choice = chessView.askUser(header, ChessString.PLAY_AGAIN_QUESTION, choices);
    if (null != choice && choice.equals(choices[0])) {
      clearView();
      newGame();
      chessView.displayMessage(ChessString.NEW_GAME);
    }
  }

  /**
   * Method used to clear the chessView. This method is only used when the game is
   * over. This
   * method is used instead of clearView() from GUIView or ConsoleView because
   * this method is
   * private.
   */
  private void clearView() {
    for (int i = 0; i < Board.BOARD_SIZE; i++) {
      for (int j = 0; j < Board.BOARD_SIZE; j++) {
        chessView.removePiece(i, j);
      }
    }
  }

  /**
   * Method used to check if there is a promotion and promote the piece if needed.
   *
   * @param piece The piece that moved
   */
  private void promoteIfNeeded(ChessPiece piece) {
    List<PromotablePiece> promotablePieces = board.getPromotablePieces();
    for (PromotablePiece promotablePiece : promotablePieces) {
      if (promotablePiece.equals(piece) && promotablePiece.canPromote()) {
        promote(piece);
      }
    }
  }

  /**
   * Check if the adversary king is in check. It can be a DiscoveredCheck or a
   * positional check.
   * Or both. The method returns true if the king is in check, false otherwise. If
   * the king is
   * in check, the method also updates the mandatoryAdversaryMoves map with the
   * possible moves
   * to avoid the check. If a promotion has been made with promoteIfNeeded(), the
   * method will
   * dynamically take the new piece in consideration.
   *
   * @return True if the king is in check, false otherwise
   */
  private boolean checkIfAdversaryKingIsInCheck() {
    ChessPiece movedPiece = board.getPiece(board.getLastMoveTo());
    King adversaryKing = board.getKing(movedPiece.getPlayerColor().opposite());
    if (isAPositionalCheck(adversaryKing, movedPiece)
        || isDiscoveredCheck(adversaryKing, board.getLastMoveFrom())) {
      mandatoryAdversaryMoves = getMovesToAvoidCheck(adversaryKing);
      return true;
    }

    return false;
  }

  /**
   * Check if the adversary king is in check because of the moved piece. If it is
   * the case, the
   * piece is added to the adversary king inCheckBy list.
   *
   * @param adversaryKing The adversary king
   * @param movedPiece    The piece that have been moved
   * @return True if the king is in check, false otherwise
   */
  private boolean isAPositionalCheck(King adversaryKing, ChessPiece movedPiece) {
    if (!movedPiece.isAttacking(board, adversaryKing.getPos())) {
      return false;
    }

    adversaryKing.addInCheckBy(movedPiece);
    return true;
  }

  /**
   * Check if the adversary king is in check because of a discovered check. If it
   * is the case,
   * the piece is added to the adversary king inCheckBy list.
   *
   * @param adversaryKing The adversary king
   * @param oldPos        The old position of the moved piece
   * @return True if the king is in check, false otherwise
   */
  private boolean isDiscoveredCheck(King adversaryKing, Point oldPos) {
    Direction attackDir = Direction.offSetOf(adversaryKing.getPos(), oldPos);
    if (null == attackDir) {
      return false;
    }

    Point attackerPos = oldPos;
    do {
      attackerPos = attackerPos.withAdded(attackDir);
    } while (Board.isInBoard(attackerPos) && !board.isOccupied(attackerPos));

    ChessPiece attacker = null;
    if (Board.isInBoard(attackerPos)) {
      attacker = board.getPiece(attackerPos);
    }

    if (null != attacker && !adversaryKing.isSameColor(attacker)
        && attacker.isAttacking(board, adversaryKing.getPos())) {
      adversaryKing.addInCheckBy(attacker);
      return true;
    }

    return false;
  }

  /**
   * Method used to create a map of pieces and the list of moves that can avoid
   * the check. The
   * method is used to find the mandatory moves of the adversary to avoid the
   * check.
   *
   * @param k The king that is in check
   * @return A map of pieces and the list of moves that can avoid the check
   */
  private HashMap<ChessPiece, List<Point>> getMovesToAvoidCheck(King k) {
    List<ChessPiece> allies = board.getAllPieces(k.getPlayerColor());
    HashMap<ChessPiece, List<Point>> possibleMoves = new HashMap<>();
    allies.forEach(a -> possibleMoves.put(a, new ArrayList<>()));
    displacementMoves(k, possibleMoves);
    blockingMoves(k, possibleMoves, allies);
    capturingMoves(k, possibleMoves, allies);
    return possibleMoves;
  }

  /**
   * Method used to find the possible moves that the king can do to avoid the
   * check.
   *
   * @param k             The king that is in check
   * @param possibleMoves The map of pieces and the list of moves that can avoid
   *                      the check
   */
  private void displacementMoves(King k, HashMap<ChessPiece, List<Point>> possibleMoves) {
    List<Direction> kingDirections = k.getDirections();
    for (Direction direction : kingDirections) {
      Point recheablePoint = k.getPos().withAdded(direction);
      if (k.canMoveTo(board, recheablePoint)) {
        possibleMoves.get(k).add(recheablePoint);
      }
    }
  }

  /**
   * Method used to check if there is a stalemate between the two players. A
   * stalemate is when
   * the adversary can't move any piece and the king is not in check. If there is
   * a piece that
   * can move, the method return false.
   *
   * @return True if there is a stalemate, false otherwise
   */
  private boolean isStalemate() {
    King adversaryKing = board.getKing(playerTurn().opposite());
    if (!adversaryKing.isInCheck() && !canMove(adversaryKing)) {
      List<ChessPiece> allies = board.getAllPieces(adversaryKing.getPlayerColor());
      for (ChessPiece ally : allies) {
        if (canMove(ally)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  /**
   * Method used to check if players cannot checkmate between each other.
   * This method follows the rules:
   * <a href="https://en.wikipedia.org/wiki/Draw_(chess)">Impossibility of
   * checkmate</a>
   *
   * @return True if player cannot checkmate between each other, false otherwise
   */
  private boolean isInsufficientMaterial() {
    List<List<ChessPiece>> allPieces = board.getAllPieces();
    for (PlayerColor color : PlayerColor.values()) {
      if (allPieces.get(color.ordinal()).size() == 1) {
        if (allPieces.get(color.opposite().ordinal()).size() == 1) {
          return true;
        }

        if (allPieces.get(color.opposite().ordinal()).size() > 1) {
          return false;
        }

        long numberOfBishops = allPieces.get(color.opposite().ordinal()).stream()
            .filter(piece -> piece instanceof Bishop)
            .count();
        long numberOfKnights = allPieces.get(color.opposite().ordinal()).stream()
            .filter(piece -> piece instanceof Knight)
            .count();
        return numberOfBishops == 1 || numberOfKnights == 1;
      }

      if (allPieces.get(color.ordinal()).size() == 2
          && allPieces.get(color.opposite().ordinal()).size() == 2) {
        Bishop b1 = (Bishop) allPieces.get(color.ordinal()).stream()
            .filter(piece -> piece instanceof Bishop)
            .findFirst().orElse(null);
        Bishop b2 = (Bishop) allPieces.get(color.opposite().ordinal()).stream()
            .filter(piece -> piece instanceof Bishop)
            .findFirst().orElse(null);
        return null != b1 && null != b2
            && (((b1.getX() + b1.getY()) % 2) == ((b2.getX() + b2.getY()) % 2));
      }
    }

    return false;
  }

  /**
   * Method used to check if a piece can make a move.
   *
   * @param piece The piece to check
   * @return True if the piece can make a move, false otherwise
   */
  private boolean canMove(ChessPiece piece) {
    List<Direction> pieceDirections = piece.getDirections();
    for (Direction direction : pieceDirections) {
      Point recheablePoint = piece.getPos().withAdded(direction);
      if (piece.canMoveTo(board, recheablePoint)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Method used to find the possible moves that the pieces can do to capture the
   * piece that is
   * checking the king.
   *
   * @param king          The king that is in check
   * @param possibleMoves The map of pieces and the list of moves that can avoid
   *                      the check
   * @param allies        The list of allies of the king
   */
  private void capturingMoves(
      King king,
      HashMap<ChessPiece, List<Point>> possibleMoves,
      List<ChessPiece> allies) {
    // Impossible to avoid double check capturing two pieces at the same time
    if (king.getInCheckBy().size() != 1) {
      return;
    }

    for (ChessPiece ally : allies) {
      if (ally.canMoveTo(board, king.getInCheckBy().get(0).getPos())) {
        possibleMoves.get(ally).add(king.getInCheckBy().get(0).getPos());
      }
    }
  }

  /**
   * Method used to find the possible moves that the pieces can do to block the
   * piece that is
   * checking the king.
   *
   * @param king          The king that is in check
   * @param possibleMoves The map of pieces and the list of moves that can avoid
   *                      the check
   * @param allies        The list of allies of the king
   */
  private void blockingMoves(
      King king,
      HashMap<ChessPiece, List<Point>> possibleMoves,
      List<ChessPiece> allies) {
    // Impossible to avoid double check blocking two pieces at the same time
    if (king.getInCheckBy().size() != 1) {
      return;
    }

    Direction attackDir = Direction.offSetOf(
        king.getPos(),
        king.getInCheckBy().get(0).getPos());
    for (ChessPiece ally : allies) {
      Point currentPos = king.getPos();
      do {
        currentPos = currentPos.withAdded(attackDir);
        if (!board.isOccupied(currentPos) && ally.canMoveTo(board, currentPos)) {
          possibleMoves.get(ally).add(currentPos);
        }
      } while (Board.isInBoard(currentPos) && !board.isOccupied(currentPos));
    }
  }

  /**
   * Update the chessView, the board and the piece position. If the king is in
   * check it will
   * clear the mandatory moves seen that the king can move to avoid the check.
   *
   * @param p  The piece to move
   * @param to The destination
   */
  private void movePiece(ChessPiece p, Point to) {
    removePiece(p);
    p.move(to);
    insertPiece(p);
    if (board.getKing(playerTurn()).isInCheck()) {
      mandatoryAdversaryMoves.clear();
      board.getKing(playerTurn()).clearInCheckBy();
    }
  }

  /**
   * Method used to promote a piece. The board and the chessView are updated.
   *
   * @param piece the piece to promote
   */
  private void promote(ChessPiece piece) {
    int posX = piece.getX(), posY = piece.getY();
    PlayerColor color = piece.getPlayerColor();
    ChessPiece[] options = {
        new Queen(color, posX, posY), new Rook(color, posX, posY),
        new Bishop(color, posX, posY), new Knight(color, posX, posY)
    };
    ChessPiece choice = chessView.askUser(
        ChessString.PROMOTION, ChessString.CHOOSE_PROMOTION, options);
    removePiece(piece);
    insertPiece(choice);
  }

  /**
   * Method used to insert the pieces of the board in the current view.
   */
  protected void insertPiecesInView() {
    for (PlayerColor color : PlayerColor.values()) {
      board.getAllPieces(color).forEach(piece -> chessView.putPiece(
          piece.getPieceType(),
          piece.getPlayerColor(),
          piece.getX(),
          piece.getY()));
    }
  }

  /**
   * Method used to execute special moves like castling or en passant if the move
   * is valid. If
   * the move is not valid it will return false.
   *
   * @param piece the moving piece
   * @param to    the destination
   * @return true if there is a special move, false otherwise
   */
  private boolean executeSpecialMoves(ChessPiece piece, Point to) {
    return castlingMove(piece, to) || enPassantMove(piece, to);
  }

  /**
   * Method used to do the castling move for the king and the rook involved in the
   * castling.
   * If the piece is not a king, the method does nothing and returns false.
   *
   * @param piece The moving piece
   * @param to    The destination of the piece
   * @return true if there is a castling move, false otherwise
   */
  private boolean castlingMove(ChessPiece piece, Point to) {
    King king = board.getKing(piece.getPlayerColor());
    if (!king.equals(piece) || !king.isCastling(to)) {
      return false;
    }

    int newCastlingWithPos = king.getX() - to.x() < 0 ? to.x() - 1 : to.x() + 1;
    ChessPiece castlingWithPiece = board.getPiece(king.getCastlingPiecePos(to));
    Point newCastlingPiecePos = new Point(newCastlingWithPos, king.getY());
    movePiece(castlingWithPiece, newCastlingPiecePos);
    board.setLastMove(castlingWithPiece.getPos(), newCastlingPiecePos);
    return true;
  }

  /**
   * Method used to do the en passant move for the pawn involved in the move. If
   * the piece is not
   * a pawn, the method does nothing and returns false.
   *
   * @param piece The moving piece
   * @param to    The destination of the piece
   * @return true if there is en passant move, false otherwise
   */
  private boolean enPassantMove(ChessPiece piece, Point to) {
    if (!(piece instanceof Pawn p) || !p.isEnPassant(board, to)) {
      return false;
    }

    Pawn adversaryPawn = (Pawn) board.getPiece(board.getLastMoveTo());
    removePiece(adversaryPawn);
    board.setLastMove(adversaryPawn.getPos(), to);
    return true;
  }

  /**
   * Method used to know the player that has to play.
   *
   * @return The player that has to play
   */
  protected PlayerColor playerTurn() {
    return turn % 2 == 0 ? PlayerColor.WHITE : PlayerColor.BLACK;
  }
}
