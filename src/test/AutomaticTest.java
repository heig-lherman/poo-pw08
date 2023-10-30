package test;

import chess.ChessController;
import chess.ChessView;
import chess.views.console.ConsoleView;
import chess.views.gui.GUIView;
import engine.GameManager;
import engine.util.Point;

import java.util.ArrayList;
import java.util.List;

public class AutomaticTest {

    // https://www.chess.com/games/view/1223291
    // White wins by checkmate
    static final String[] CARLSEN_ERNST_2004 = new String[]{
            "e2e4", "c7c6", "d2d4", "d7d5", "b1c3", "d5e4", "c3e4", "c8f5", "e4g3", "f5g6", "h2h4",
            "h7h6", "g1f3", "b8d7", "h4h5", "g6h7", "f1d3", "h7d3", "d1d3", "e7e6", "c1f4", "g8f6",
            "e1c1", "f8e7", "g3e4", "d8a5", "c1b1", "e8g8", "e4f6", "d7f6", "f3e5", "a8d8", "d3e2",
            "c6c5", "e5g6", "f7g6", "e2e6", "g8h8", "h5g6", "f6g8", "f4h6", "g7h6", "h1h6", "g8h6",
            "e6e7", "h6f7", "g6f7", "h8g7", "d1d3", "d8d6", "d3g3", "d6g6", "e7e5", "g7f7", "e5f5",
            "g6f6", "f5d7"
    };

    // https://www.chess.com/game/live/66685869281
    // White wins by checkmate using en passant
    static final String[] CARLSEN_LUDVIQ_2023 = new String[]{
            "e2e4", "g7g6", "d2d4", "e7e6", "g1f3", "g8e7", "h2h4", "h7h6", "c1f4", "f8g7", "d1d2",
            "d7d5", "e4e5", "c7c5", "c2c3", "b8c6", "b1a3", "e8f8", "d4c5", "b7b6", "c5b6", "d8b6",
            "f1d3", "c8a6", "b2b4", "d5d4", "b4b5", "d4c3", "d2e3", "e7d5", "e3b6", "a7b6", "b5a6",
            "d5f4", "d3e4", "a8a6", "e4c6", "a6a3", "e1c1", "f8g8", "d1d8", "g8h7", "f3g5", "h6g5",
            "h4g5", "f4h5", "h1h5", "g6h5", "c6e4", "f7f5", "e5f6"
    };

    // https://www.chess.com/games/view/117100
    // Black wins by checkmate
    static final String[] BYRNE_FISCHER_1963 = new String[]{
            "d2d4", "g8f6", "c2c4", "g7g6", "g2g3", "c7c6", "f1g2", "d7d5", "c4d5", "c6d5", "b1c3",
            "f8g7", "e2e3", "e8g8", "g1e2", "b8c6", "e1g1", "b7b6", "b2b3", "c8a6", "c1a3", "f8e8",
            "d1d2", "e7e5", "d4e5", "c6e5", "f1d1", "e5d3", "d2c2", "d3f2", "g1f2", "f6g4", "f2g1",
            "g4e3", "c2d2", "e3g2", "g1g2", "d5d4", "e2d4", "a6b7", "g2f1", "d8d7", "d2f2", "d7h3",
            "f1g1", "e8e1", "d1e1", "g7d4", "f2d4", "h3g2"
    };

    // https://www.chessgames.com/perl/chessgame?gid=1848605
    // StaleMate between players
    static final String[] CARLSEN_KARJAKIN_2016 = new String[]{
            "e2e4", "e7e5", "g1f3", "b8c6", "f1c4", "f8c5", "e1g1", "g8f6", "d2d3", "e8g8", "a2a4",
            "a7a6", "c2c3", "d7d6", "f1e1", "c5a7", "h2h3", "c6e7", "d3d4", "e7g6", "b1d2", "c7c6",
            "c4f1", "a6a5", "d4e5", "d6e5", "d1c2", "c8e6", "d2c4", "d8c7", "b2b4", "a5b4", "c3b4",
            "b7b5", "c4e3", "b5a4", "a1a4", "a7e3", "c1e3", "a8a4", "c2a4", "f6e4", "e1c1", "e6d5",
            "b4b5", "c6b5", "a4e4", "c7c1", "e4d5", "c1c7", "d5b5", "f8b8", "b5d5", "b8d8", "d5b3",
            "d8b8", "b3a2", "h7h6", "a2d5", "c7e7", "d5e4", "e7f6", "g2g3", "b8c8", "f1d3", "f6c6",
            "e4f5", "c8e8", "d3e4", "c6e6", "f5h5", "g6e7", "h5e5", "e6e5", "f3e5", "e7g6", "e4g6",
            "e8e5", "g6d3", "f7f6", "g1g2", "g8h8", "g2f3", "e5d5", "d3g6", "d5a5", "f3e4", "a5b5",
            "h3h4", "b5e5", "e4d4", "e5a5", "d4c4", "a5e5", "e3d4", "e5a5", "d4c5", "h8g8", "c4d5",
            "a5b5", "d5d6", "b5a5", "c5e3", "a5e5", "e3f4", "e5a5", "g6d3", "a5a7", "d6e6", "a7b7",
            "e6f5", "b7d7", "d3c2", "d7b7", "f5g6", "b7b2", "c2f5", "b2f2", "f5e6", "g8h8", "f4d6",
            "f2e2", "e6g4", "e2e8", "g4f5", "h8g8", "f5c2", "e8e3", "c2b1", "g8h8", "g6f7", "e3b3",
            "b1e4", "b3e3", "e4f5", "e3c3", "g3g4", "c3c6", "d6f8", "c6c7", "f7g6", "h8g8", "f8b4",
            "c7b7", "b4d6", "g8h8", "d6f8", "h8g8", "f8a3", "g8h8", "f5e6", "b7b6", "g6f7", "b6b7",
            "a3e7", "h6h5", "g4h5", "f6f5", "e6f5", "b7e7", "f7e7", "h8g8", "f5d3", "g8h8", "e7f8",
            "g7g5", "h5g6"
    };

    // https://www.chessgames.com/perl/chessgame?gid=1272123
    // White wins by O-O-O#
    static final String[] KVICALA_NN_1869 = new String[]{
            "e2e4", "c7c5", "f1c4", "b8c6", "g1f3", "e7e6", "b1c3", "a7a6", "d2d4", "b7b5", "d4d5",
            "b5c4", "d5c6", "d7d6", "e4e5", "d6d5", "c1g5", "f7f6", "e5f6", "g8f6", "f3e5", "h7h6",
            "c6c7", "d8c7", "g5f6", "g7f6", "d1h5", "e8e7", "h5f7", "e7d6", "f7f6", "f8g7", "c3e4",
            "d5e4", "e1c1"
    };

    // https://www.chessgames.com/perl/chessgame?gid=1007138
    // Classic StaleMate
    static final String[] TARTAKOWER_RETI_1925 = new String[]{
            "c2c3", "c7c5", "d2d4", "e7e6", "e2e4", "g8f6", "e4e5", "f6d5", "g1f3", "d7d6", "e5d6",
            "f8d6", "d4c5", "d6c5", "f1b5", "c8d7", "b5d7", "b8d7", "e1g1", "e8g8", "b1d2", "d8c7",
            "d2e4", "c5e7", "d1e2", "d7c5", "e4c5", "c7c5", "c3c4", "d5b4", "a2a3", "b4c6", "b2b4",
            "c6d4", "f3d4", "c5d4", "c1b2", "d4b6", "c4c5", "b6c6", "e2g4", "g7g6", "g4d4", "f7f6",
            "f1e1", "a8d8", "d4c4", "d8d5", "e1e2", "e7d8", "a1e1", "f8e8", "h2h4", "g8f7", "c4f4",
            "d5f5", "f4e3", "d8c7", "e1d1", "f5d5", "d1d5", "c6d5", "e2d2", "d5f5", "g2g3", "e8d8",
            "d2d8", "c7d8", "c5c6", "f5d5", "c6b7", "d5b7", "b2d4", "a7a6", "e3h6", "f7g8", "h4h5",
            "b7f7", "h5g6", "h7g6", "h6e3", "f7d7", "e3e4", "g8f7", "d4c5", "d8c7", "e4h4", "d7d1",
            "g1h2", "d1h5", "h4h5", "g6h5", "h2h3", "f7g6", "a3a4", "c7b8", "h3h4", "e6e5", "b4b5",
            "a6b5", "a4b5", "f6f5", "b5b6", "f5f4", "g3f4", "e5f4", "c5e7", "b8e5", "e7d8", "e5c3",
            "f2f3", "c3e1", "h4h3", "e1b4", "d8h4", "b4c5", "h4d8", "c5b4", "h3g2", "g6f5", "g2f1",
            "f5e6", "d8c7", "b4d6", "c7d6", "e6d6", "f1g2", "d6c6", "g2h3", "c6b6", "h3h4", "b6c5",
            "h4h5", "c5d4", "h5g5", "d4e5", "g5g4", "e5e6", "g4f4", "e6f6", "f4e4", "f6e6", "f3f4",
            "e6f6", "f4f5", "f6e7", "e4e5", "e7f7", "f5f6", "f7f8", "e5e6", "f8e8", "f6f7", "e8f8",
            "e6f6"
    };

    // No GM game - from chess.com
    // Promotion to Queen must be done
    // [White "shy1akaanh"]
    // [Black "TheLovatsisAttack"]
    // Insufficient material
    static final String[] LAMBDA_GAME = new String[]{
            "d2d4", "d7d5", "b1c3", "g8f6", "g1f3", "b8c6", "c1g5", "e7e6", "e2e3", "h7h6", "g5h4",
            "f8d6", "f1b5", "c8d7", "d1d3", "g7g5", "h4g3", "d6g3", "h2g3", "a7a6", "b5a4", "d8e7",
            "a2a3", "e6e5", "d4e5", "c6e5", "a4d7", "f6d7", "d3d5", "e5f3", "g2f3", "c7c6", "d5d4",
            "e8c8", "e1c1", "c6c5", "d4c4", "e7f6", "c3d5", "f6f3", "d5b6", "d7b6", "c4c5", "f3c6",
            "c5f5", "c8c7", "f5f7", "c7b8", "f2f4", "h8f8", "f7e7", "b8a7", "f4g5", "h6g5", "e7g5",
            "f8g8", "g5f4", "d8e8", "h1h6", "c6c5", "d1e1", "b6c4", "b2b4", "c5b5", "f4d4", "a7a8",
            "d4c3", "g8g3", "a3a4", "b5g5", "h6h8", "e8h8", "c3h8", "a8a7", "h8d4", "b7b6", "d4c4",
            "g3e3", "c4c7", "a7a8", "e1e3", "g5e3", "c1b2", "e3d4", "c2c3", "d4d2", "b2b3", "d2d1",
            "b3a3", "d1a1", "a3b3", "a1b1", "b3a3", "b1a1", "a3b3", "a1d1", "b3a3", "d1d5", "c7b6",
            "d5d2", "b6a6", "a8b8", "a6b6", "b8c8", "b6c6", "c8d8", "a4a5", "d2c1", "a3b3", "c1b1",
            "b3c4", "b1f1", "c4c5", "f1f2", "c5b5", "f2f5", "c6c5", "f5d3", "b5b6", "d3g6", "c5c6",
            "g6g1", "c6c5", "g1g6", "b6b5", "g6d3", "c3c4", "d3d7", "c5c6", "d7f5", "c6d5", "f5d5",
            "c4d5", "d8d7", "b5b6", "d7d6", "a5a6", "d6d5", "a6a7", "d5c4", "b4b5", "c4b4", "a7a8q",
            "b4c4", "a8a4", "c4d5", "a4b3", "d5d4", "b6a5", "d4e5", "b5b6", "e5d6", "b6b7", "d6c7",
            "b3c3", "c7b7", "c3c6", "b7c6"
    };


    public static void main(String[] args) {
        ChessController controller = new GameManager();
        ChessView view = new GUIView(controller);
        controller.start(view);
        simulateGames(controller);
    }

    /**
     * Method used to simulate all the games found in this class. In order to perform all
     * the tests, when the game will ask for a new Game, select "Yes" in the GUI.
     *
     * @param controller the controller to use
     */
    public static void simulateGames(ChessController controller) {
        List<String[]> games = List.of(
                CARLSEN_ERNST_2004, CARLSEN_LUDVIQ_2023, BYRNE_FISCHER_1963,
                CARLSEN_KARJAKIN_2016, KVICALA_NN_1869, TARTAKOWER_RETI_1925,
                LAMBDA_GAME
        );
        for (String[] game : games) {
            List<List<Point>> moves = fromUciToPoint(game);
            for (List<Point> move : moves) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                controller.move(move.get(0).x(), move.get(0).y(), move.get(1).x(), move.get(1).y());
            }
        }
    }

    public static List<List<Point>> fromUciToPoint(String[] uci) {
        List<List<Point>> fromTo = new ArrayList<>();
        for (String s : uci) {
            List<Point> points = new ArrayList<>();
            points.add(new Point(s.charAt(0) - 'a', s.charAt(1) - '1'));
            points.add(new Point(s.charAt(2) - 'a', s.charAt(3) - '1'));
            fromTo.add(points);
        }
        return fromTo;
    }
}
