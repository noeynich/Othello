package othello;

import javafx.scene.paint.Color;

/**
 * Interface representing a player in the Othello game.
 * Both human and computer players implement this interface.
 * It defines basic player behavior and attributes.
 */
public interface Player {

    /**
     * Returns the color of this player’s pieces.
     * (hex code for BLACK or WHITE)
     */
    public Color getColor();

    /**
     * Determines whether this player can be clicked on the board.
     * Typically, human players return true, while AI players return false.
     */
    public boolean isClickable();

    /**
     * Returns the mode of this player.
     * 0 = HumanPlayer</li>
     * 1-3 = ComputerPlayer difficulty levels</li>
     */
    public int getMode();

    /**
     * Activates this player on the given board.
     * For human players, this might enable click handling.
     * For computer players, this might trigger AI move calculation.
     */
    public void activate(Board board);
}
