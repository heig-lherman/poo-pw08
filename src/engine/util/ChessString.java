package engine.util;

import chess.PlayerColor;

/**
 * Class containing all the messages used in the chess game.
 * 
 * @version 1.0
 */
public final class ChessString {

  public static final String YES = "Yes";
  public static final String NO = "No";
  public static final String CHECK = "Check!";
  public static final String CHECKMATE = "Checkmate!";
  public static final String STALEMATE = "Stalemate!";
  public static final String INSUFFICIENT_MATERIAL = "Insufficient material!";
  public static final String PROMOTION = "Promote";
  public static final String PLAYER_MOVE = "to move";
  public static final String PLAYER_WINS = "wins";
  public static final String CHOOSE_PROMOTION = "To which piece do you want to promote?";
  public static final String PLAY_AGAIN_QUESTION = "Do you want to play again?";
  public static final String NEW_GAME = "New game";

  /**
   * Method used to announce the winner of the game.
   * 
   * @param color The color of the winner.
   * @return The message announcing the winner.
   */
  public static String playerWins(PlayerColor color) {
    return color.name() + " " + PLAYER_WINS;
  }

  /**
   * Method used to announce the player to move.
   * 
   * @param color The color of the player to move.
   * @return The message announcing the player to move.
   */
  public static String playerToMove(PlayerColor color) {
    return color.name() + " " + PLAYER_MOVE;
  }

}
