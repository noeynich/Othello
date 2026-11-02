package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents a single square on the Othello board.
 * Each square is responsible only for its own visual
 * representation and basic state.
 * All game logic (valid moves, turn-taking, flipping pieces)
 * is handled by the referee.
 */
public class OthelloSquare {

    private int row;
    private int col;
    private Pane pane; // needed so Square can add/remove pieces
    private Rectangle rect;
    private OthelloPiece piece; // null if empty
    private Referee referee;

    /**
     * Constructs an OthelloSquare with location, pane,and
     * referee logic. Initializes the JavaFX rectangle and
     * sets up the mouse-click handler.
     */
    public OthelloSquare(int row, int col,
                         Pane pane, Referee referee) {
        this.row = row;
        this.col = col;
        this.pane = pane;
        this.referee = referee;

        this.rect = new Rectangle(
                col * Constants.SQUARE_WIDTH,
                row * Constants.SQUARE_WIDTH,
                Constants.SQUARE_WIDTH,
                Constants.SQUARE_WIDTH
        );

        this.rect.setFill(Constants.BOARD_COLOR);
        this.rect.setStroke(Color.BLACK);

        this.setupClickHandler();
    }

    /**
     * Returns the column index of this square.
     */
    public int getCol() {return this.col;}

    /**
     * Returns the row index of this square.
     */
    public int getRow() {return this.row;}

    /**Create the OthelloPiece
     Position it at the center of the square
     Add the piece’s graphic to the pane
     Store the piece internally
     */
    public void addPiece(Color color) {
        this.piece = new OthelloPiece(
                color, this.row, this.col, Constants.PIECE_RADIUS);
        this.piece.addGraphically(this.pane);
        this.referee.updateScore(color, "add");
    }

    /**
     * Returns whether the square contains no piece.
     */
    public boolean isEmpty() {
        return this.piece == null;
    }

    /**
     * Sets up the mouse-click handler so that clicking the square notifies
     * the referee. The referee determines whether the move is legal and
     * performs appropriate game actions.
     */
    private void setupClickHandler() {
        this.rect.setOnMouseClicked(event -> {
            this.referee.handleClick(this);
        });
    }

    /**
     * Adds the square's visual rectangle to the board pane.
     * Called during board initialization.
     */
    public void addGraphically() {this.pane.getChildren().add(this.rect); }

    /**
     * Returns the piece currently occupying this square.
     */
    public OthelloPiece getPiece() {
        return this.piece;
    }

    /**
     * Changes the visual color of the square to the highlight color.
     * Used to display legal moves or selections.
     */
    public void highlightSquare() {this.rect.setFill(Constants.HIGHLIGHT_COLOR);}

    /**
     * Restores the square's background color to the default board color.
     * Used when removing highlights or resetting the board.
     */
    public void resetSquareColor() {this.rect.setFill(Constants.BOARD_COLOR);}

    /**
     * Removes the piece from this square—both logically and graphically.
     * Used during resetting the game.
     */
    public void removePiece() {
        if (!this.isEmpty()) {
            this.piece.removeGraphically(this.pane);
            this.piece = null;
        }
    }
}
