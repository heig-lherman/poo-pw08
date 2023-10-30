package chess.views;

import chess.ChessController;
import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;

import java.util.HashMap;

public abstract class BaseView<Resource> implements ChessView {
    private final HashMap<PieceType, HashMap<PlayerColor, DrawableResource<Resource>>> resources = new HashMap<>();

    protected final ChessController controller;

    public BaseView(ChessController controller) {
        this.controller = controller;
    }

    public final void registerResource(PieceType type, PlayerColor color, DrawableResource<Resource> resource) {
        HashMap<PlayerColor, DrawableResource<Resource>> pieceResources = resources.get(type);
        if (pieceResources == null) {
            pieceResources = new HashMap<>();
        }
        pieceResources.put(color, resource);
        resources.put(type, pieceResources);
    }

    protected final Resource loadResourceFor(PieceType type, PlayerColor color, Resource def) {
        Resource icon = def;
        if (type != null && color != null) {
            HashMap<PlayerColor, DrawableResource<Resource>> pieceResources = resources.get(type);
            if (pieceResources != null) {
                DrawableResource<Resource> resource = pieceResources.get(color);
                if (resource != null) {
                    icon = resource.getResource();
                }
            }
        }
        return icon;
    }
}
