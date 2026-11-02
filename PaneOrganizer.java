package othello;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Organizes the main layout of the Othello game GUI using a BorderPane.
 * This class is responsible for creating the main layout container,
 * adding the game board to the left, Adding the game controls to the right,
 * Preparing a pane for the game graphics in the center
 */
public class PaneOrganizer {
    private BorderPane root;

    /**
     * Constructs thePaneOrganizer and initializes the layout.
     */
    public PaneOrganizer() {
        this.root = new BorderPane();

        Pane gamePane = new Pane();
        this.root.setCenter(gamePane);

        SetupGame othello = new SetupGame();

        Controls ctrl = new Controls(othello);
        this.root.setRight(ctrl.getPane());

        Board board = othello.getBoard();
        this.root.setLeft(board.getPane());
    }

    public Pane getRoot() {
        return root;
    }
}
