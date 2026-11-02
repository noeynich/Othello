package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * Represents a single Othello disk placed on the board.
 * Pieces do not move between squares; instead, they may have their color
 * flipped during gameplay, correspondingly.
 */
public class OthelloPiece {
    private Color color;
    private Circle disk;
    private int row;
    private int col;

    /**
     * Constructs an OthelloPiece with a specified color, position,
     * and circle radius for display.
     */
    public OthelloPiece(Color color, int row, int col, double radius) {
        this.color = color;
        this.row = row;
        this.col = col;

        double centerX = col * Constants.SQUARE_WIDTH +
                Constants.SQUARE_WIDTH / 2.0;
        double centerY = row * Constants.SQUARE_WIDTH +
                Constants.SQUARE_WIDTH / 2.0;
        this.disk = new Circle(centerX, centerY, radius);
        this.disk.setFill(color);
    }

    /**
     * Returns the current color of the piece.
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Flips the piece to a new color and updates the JavaFX display.
     * Used by the game logic when pieces are captured.
     */
    public void flipColor(Color color) {
        this.disk.setFill(color);
        this.color = color;
    }

    /**
     * Returns the row index of the piece.
     */
    public int getRow() { return this.row; }

    /**
     * Returns the column index of the piece.
     */
    public int getCol() { return this.col; }


    /**
     * Adds the piece's visual representation to the board's pane.
     */
    public void addGraphically(Pane pane) { pane.getChildren().add(this.disk); }

    /**
     * Removes the piece's visual representation to the board's pane.
     */
    public void removeGraphically(Pane pane) {
        pane.getChildren().remove(this.disk); }
}