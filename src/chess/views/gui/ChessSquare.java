package chess.views.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

class ChessSquare extends JButton {

    //coordinates
    final int x;
    final int y;

    ChessSquare(int x, int y) {
        assert (x < 8 && x >= 0);
        assert (y < 8 && y >= 0);
        this.x = x;
        this.y = y;
        if ((x % 2 ^ y % 2) == 0)
            this.setBackground(Color.GRAY);
        else
            this.setBackground(Color.WHITE);
        this.setOpaque(true);
    }

    void select() {
        this.setBorder(new LineBorder(Color.GREEN, 5));
    }

    void deselect() {
        this.setBorder(null);
    }
}
