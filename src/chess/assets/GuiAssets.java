package chess.assets;

import chess.PieceType;
import chess.PlayerColor;
import chess.views.gui.GUIView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GuiAssets {
    public static void loadAssets(GUIView view) {
        try {
            view.registerResource(PieceType.ROOK, PlayerColor.BLACK, GUIView.createResource(assetsImage("rook_black.png")));
            view.registerResource(PieceType.ROOK, PlayerColor.WHITE, GUIView.createResource(assetsImage("rook_white.png")));

            view.registerResource(PieceType.PAWN, PlayerColor.BLACK, GUIView.createResource(assetsImage("pawn_black.png")));
            view.registerResource(PieceType.PAWN, PlayerColor.WHITE, GUIView.createResource(assetsImage("pawn_white.png")));

            view.registerResource(PieceType.KNIGHT, PlayerColor.BLACK, GUIView.createResource(assetsImage("knight_black.png")));
            view.registerResource(PieceType.KNIGHT, PlayerColor.WHITE, GUIView.createResource(assetsImage("knight_white.png")));

            view.registerResource(PieceType.BISHOP, PlayerColor.BLACK, GUIView.createResource(assetsImage("bishop_black.png")));
            view.registerResource(PieceType.BISHOP, PlayerColor.WHITE, GUIView.createResource(assetsImage("bishop_white.png")));

            view.registerResource(PieceType.QUEEN, PlayerColor.BLACK, GUIView.createResource(assetsImage("queen_black.png")));
            view.registerResource(PieceType.QUEEN, PlayerColor.WHITE, GUIView.createResource(assetsImage("queen_white.png")));

            view.registerResource(PieceType.KING, PlayerColor.BLACK, GUIView.createResource(assetsImage("king_black.png")));
            view.registerResource(PieceType.KING, PlayerColor.WHITE, GUIView.createResource(assetsImage("king_white.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage assetsImage(String imageName) throws IOException {
        return ImageIO.read(GuiAssets.class.getResource("images/" + imageName));
    }
}
