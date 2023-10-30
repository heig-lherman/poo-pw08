package chess.assets;

import chess.PieceType;
import chess.PlayerColor;
import chess.views.console.ConsoleView;

public class ConsoleAssets {

    public static void loadAssets(ConsoleView view) {
        view.registerResource(PieceType.ROOK, PlayerColor.BLACK, ConsoleView.createResource("R", PlayerColor.BLACK));
        view.registerResource(PieceType.ROOK, PlayerColor.WHITE, ConsoleView.createResource("R", PlayerColor.WHITE));

        view.registerResource(PieceType.PAWN, PlayerColor.BLACK, ConsoleView.createResource("P", PlayerColor.BLACK));
        view.registerResource(PieceType.PAWN, PlayerColor.WHITE, ConsoleView.createResource("P", PlayerColor.WHITE));

        view.registerResource(PieceType.KNIGHT, PlayerColor.BLACK, ConsoleView.createResource("N", PlayerColor.BLACK));
        view.registerResource(PieceType.KNIGHT, PlayerColor.WHITE, ConsoleView.createResource("N", PlayerColor.WHITE));

        view.registerResource(PieceType.BISHOP, PlayerColor.BLACK, ConsoleView.createResource("B", PlayerColor.BLACK));
        view.registerResource(PieceType.BISHOP, PlayerColor.WHITE, ConsoleView.createResource("B", PlayerColor.WHITE));

        view.registerResource(PieceType.QUEEN, PlayerColor.BLACK, ConsoleView.createResource("Q", PlayerColor.BLACK));
        view.registerResource(PieceType.QUEEN, PlayerColor.WHITE, ConsoleView.createResource("Q", PlayerColor.WHITE));

        view.registerResource(PieceType.KING, PlayerColor.BLACK, ConsoleView.createResource("K", PlayerColor.BLACK));
        view.registerResource(PieceType.KING, PlayerColor.WHITE, ConsoleView.createResource("K", PlayerColor.WHITE));
    }
}
