package chess.views.gui;

import chess.ChessController;
import chess.PieceType;
import chess.PlayerColor;
import chess.assets.GuiAssets;
import chess.views.BaseView;
import chess.views.DrawableResource;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class GUIView extends BaseView<ImageIcon> {

    private static class PieceResource implements DrawableResource<ImageIcon> {

        private ImageIcon icon;

        PieceResource(BufferedImage bufferedImage) {
            icon = new ImageIcon(bufferedImage);
        }

        @Override
        public ImageIcon getResource() {
            return icon;
        }
    }

    private static class SwingChoiceWrapper<T extends UserChoice> {
        private T object;

        SwingChoiceWrapper(T object) {
            this.object = object;
        }

        public T userChoice() {
            return object;
        }

        @Override
        public String toString() {
            return object.textValue();
        }
    }

    public static DrawableResource<ImageIcon> createResource(BufferedImage image) throws IOException {
        return new PieceResource(image);
    }

    //idea:https://stackoverflow.com/questions/21142686/making-a-robust-resizable-swing-chess-gui
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private ChessSquare[][] chessBoardSquares = new ChessSquare[8][8];
    private final JLabel headerLabel = new JLabel("Welcome to the HEIG-VD Chess game!");
    private static final String COLS = "ABCDEFGH";
    private final JLabel messageLabel = new JLabel("");

    private ChessSquare lastPressed = null;

    private final static ImageIcon EMPTY_ICON = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
    private final static ImageIcon UNKNOWN_ICON;

    static { // Damier pour absence de texture au cas où
        BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        final int squareSize = 8;
        g.setColor(Color.MAGENTA);
        for (int stripeX = 0; stripeX < img.getWidth(); stripeX += squareSize) {
            for (int y = 0, row = 0; y < img.getHeight(); y += squareSize / 2, ++row) {
                int x = (row % 2 == 0) ? stripeX : (stripeX + squareSize / 2);
                g.fillRect(x, y, squareSize / 2, squareSize / 2);
            }
        }
        UNKNOWN_ICON = new ImageIcon(img);
    }

    public GUIView(ChessController controller) {
        super(controller);
        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        GuiAssets.loadAssets(this);
        initializeGui();
        clearView();
    }

    @Override
    public void startView() {
        Runnable r = () -> {
            JFrame f = new JFrame("HEIG-VD Chess");
            f.add(gui);
            // Ensures JVM closes after frame(s) closed and
            // all non-daemon threads are finished
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);

            // ensures the frame is the minimum size it needs to be
            // in order display the components within it
            f.pack();
            // ensures the minimum size is enforced.
            f.setMinimumSize(f.getSize());
            f.setVisible(true);
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }


    @Override
    public void removePiece(int x, int y) {
        chessBoardSquares[x][y].setIcon(EMPTY_ICON);
    }

    @Override
    public void putPiece(PieceType type, PlayerColor color, int x, int y) {
        chessBoardSquares[x][y].setIcon(loadResourceFor(type, color, UNKNOWN_ICON));
    }

    @Override
    public void displayMessage(String msg) {
        messageLabel.setText(msg);
    }

    @Override
    public <T extends UserChoice> T askUser(String title, String question, T... possibilities) {
        T result = possibilities.length > 0 ? possibilities[0] : null;

        if (possibilities.length > 1) {
            SwingChoiceWrapper<T>[] selectionValues = Arrays.stream(possibilities).map(SwingChoiceWrapper::new).toArray(SwingChoiceWrapper[]::new);

            Object chosen = JOptionPane.showInputDialog(null,
                    question, title, JOptionPane.QUESTION_MESSAGE, null, selectionValues, result);

            result = chosen != null ? ((SwingChoiceWrapper<T>) chosen).userChoice() : null;
        }
        return result;
    }


    private void clearView() {
        messageLabel.setText("");
        for (int i = 0; i < chessBoardSquares.length; ++i) {
            for (int j = 0; j < chessBoardSquares[i].length; ++j) {
                removePiece(i, j);
            }
        }
    }

    private void move(ChessSquare from, ChessSquare to) {
        messageLabel.setText("");
        controller.move(from.x, from.y, to.x, to.y);
    }


    private void squareAction(ChessSquare b) {
        //Nothing selected yet
        if (lastPressed == null) {
            lastPressed = b;
            b.select();
        }
        //Smth was already selected
        else {
            move(lastPressed, b);
            lastPressed.deselect();
            lastPressed = null;
        }
    }

    private void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New game") {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearView();
                controller.newGame();
            }
        };
        tools.add(newGameAction);
        tools.addSeparator();
        tools.add(headerLabel);
        tools.addSeparator();
        tools.addSeparator();
        tools.add(messageLabel);
        messageLabel.setForeground(Color.RED);

        JPanel chessBoard;
        chessBoard = new JPanel(new GridLayout(0, 9)) {

            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int) d.getWidth(), (int) d.getHeight());
                } else if (
                        c.getWidth() > d.getWidth() &&
                                c.getHeight() > d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                int s = (Math.min(w, h));
                return new Dimension(s, s);
            }
        };
        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(8, 8, 8, 8),
                new LineBorder(Color.BLACK)
        ));
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        // create the chess board squares
        for (int i = 0; i < chessBoardSquares.length; i++) {
            for (int j = 0; j < chessBoardSquares[i].length; j++) {
                ChessSquare b = new ChessSquare(i, j);
                //Action on each button
                b.addActionListener(e -> squareAction(b));
                chessBoardSquares[i][j] = b;
            }
        }

        /*
         * fill the chess board
         */
        chessBoard.add(new JLabel(""));
        // fill the top row
        for (int i = 0; i < 8; i++) {
            chessBoard.add(
                    new JLabel(COLS.substring(i, i + 1),
                            SwingConstants.CENTER));
        }
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) {
                    chessBoard.add(new JLabel("" + (i + 1),
                            SwingConstants.CENTER));
                }
                chessBoard.add(chessBoardSquares[j][i]);
            }
        }
    }
}
