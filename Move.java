package othello;

/**
 * Represents a potential or executed move in the Othello game.
 */
public class Move {
    private int row;
    private int col;
    private int value;

    /**
     * Constructs a Move object with a specific board location and value.
     */
    public Move(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    /**
     * Returns the heuristic value of the move.
     */
    public int getValue() { return this.value; }

    /**
     * Updates the heuristic value of the move.
     */
    public void setValue(int x) {
        this.value = x;
    }

    /**
     * Returns the row where the move will be applied.
     */
    public int getRow() { return this.row; }

    /**
     * Returns the column where the move will be applied.
     */
    public int getCol() { return this.col; }
}
