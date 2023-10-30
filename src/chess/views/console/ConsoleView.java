package chess.views.console;

import chess.ChessController;
import chess.PieceType;
import chess.PlayerColor;
import chess.assets.ConsoleAssets;
import chess.views.BaseView;
import chess.views.DrawableResource;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ConsoleView extends BaseView<String> {

    private static class StringResource implements DrawableResource<String> {
        private String value;

        private StringResource(String value, PlayerColor color) {
            this.value = (color == PlayerColor.BLACK ? "\u001B[31m" : "") + value + "\u001B[30m";
        }

        @Override
        public String getResource() {
            return value;
        }
    }

    ;

    public static DrawableResource<String> createResource(String value, PlayerColor color) {
        return new StringResource(value, color);
    }

    private final static Scanner scanner = new Scanner(System.in);
    private final static Pattern movementPattern = Pattern.compile("[a-h][1-8][a-h][1-8]");
    private final static String EMPTY_CELL = " ";
    private final static String UNKNOWN_CELL = "?";

    private String checkMessage = "";
    private String[][] buffer;

    public ConsoleView(ChessController controller) {
        super(controller);
        ConsoleAssets.loadAssets(this);
        initialize();
        clearView();
    }

    @Override
    public void startView() {
        System.out.println("Chess game...");
        controller.newGame();
        while (true) { // TODO: ajouter un exit (comportement repris de la V1)
            printBoard();
            askMovement();
        }
    }

    @Override
    public void removePiece(int x, int y) {
        buffer[x][y] = EMPTY_CELL;
    }

    @Override
    public void putPiece(PieceType type, PlayerColor color, int x, int y) {
        buffer[x][y] = loadResourceFor(type, color, UNKNOWN_CELL);
    }

    @Override
    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    @Override
    public <T extends UserChoice> T askUser(String title, String question, T... possibilities) {
        T result = possibilities.length > 0 ? possibilities[0] : null;
        if (possibilities.length > 1) {
            int i = 0;
            for (T choice : possibilities) {
                System.out.println(i + ". " + choice.textValue());
                ++i;
            }

            int userChoice;
            do {
                userChoice = -1;
                System.out.println("Enter the desired number > ");

                try {
                    userChoice = Integer.parseInt(scanner.nextLine());
                    if (userChoice >= 0 && userChoice < possibilities.length)
                        result = possibilities[userChoice];
                    else
                        userChoice = -1;
                } catch (NumberFormatException e) { // nothing
                }

                if (userChoice < 0)
                    System.out.println("Error. Choose a value between 0 and " + (possibilities.length - 1));

            }
            while (userChoice < 0);
        }
        return result;
    }

    private void initialize() {
        buffer = new String[8][8];
    }

    private void clearView() {
        for (int i = 0; i < buffer.length; ++i) {
            for (int j = 0; j < buffer[i].length; ++j) {
                removePiece(i, j);
            }
        }
    }


    private void printBoard() {
        for (int y = 7; y >= 0; --y) {
            System.out.print(y + 1 + " |");
            for (int x = 0; x < 8; ++x) {
                System.out.print(buffer[x][y]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
        System.out.println("-------------------");
        System.out.println("   A B C D E F G H ");
    }

    private static int charCoordinateToIndex(char c) {
        assert (c >= 'a' && c < 'i');
        return c - 'a';
    }

    private static String askPattern(Pattern pattern, String text) {
        String in = null;
        while (in == null) {
            System.out.println(text);
            in = scanner.findInLine(pattern);
            scanner.nextLine();//clean buffer
        }
        return in;

    }

    private static int intCoordinateToIndex(char c) {
        assert (c >= '1' && c <= '9');
        return c - '1';
    }

    private void askMovement() {
        boolean ok = false;
        while (!ok) {
            String in = askPattern(movementPattern, "Next move?");
            System.out.println(in);
            ok = controller.move(charCoordinateToIndex(in.charAt(0)), intCoordinateToIndex(in.charAt(1)),
                    charCoordinateToIndex(in.charAt(2)), intCoordinateToIndex(in.charAt(3)));

            if (!ok) {
                System.out.println("Invalid move");
                printBoard();
            }
        }
    }
}
