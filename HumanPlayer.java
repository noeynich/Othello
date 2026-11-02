package othello;

import javafx.scene.paint.Color;

/**
 * The HumanPlayer class represents a human-controlled player in the
 * Othello game. Unlike ComputerPlayer, HumanPlayer it responds to
 * mouse clicks on valid board squares.
 */
public class HumanPlayer implements Player {
    private Color color;

    /**
     * Constructs a HumanPlayer with a specified color.
     */
    public HumanPlayer(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() { return this.color; }

    @Override
    public int getMode() { return 0; }

    @Override
    public boolean isClickable() { return true; }

    @Override
    public void activate(Board board) {}
}
