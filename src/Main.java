import chess.ChessController;
import chess.ChessView;
import chess.views.console.ConsoleView;
import chess.views.gui.GUIView;
import engine.GameManager;

public final class Main {

    public static void main(String[] args) {
        boolean gui = false;
        if (0 < args.length) {
            gui = args[0].equals("-g");
        }

        // ChessController controller = new GameManagerTest();
        ChessController controller = new GameManager();
        ChessView view = gui ? new GUIView(controller) : new ConsoleView(controller);
        controller.start(view);
    }
}
