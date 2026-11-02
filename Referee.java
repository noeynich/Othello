package othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * Referee controls the flow of the Othello game.
 * It manages turns, handles clicks, updates scores, flips pieces,
 * determines the winner, and interacts with the GUI.
 */
public class Referee {

    private Board board;
    private Player currPlayer;
    private Player nextPlayer;
    private int blackScore;
    private int whiteScore;
    private boolean canClick;
    private Label statusLabel;
    private Timeline timeline;

    /**
     * Constructs a Referee for a given board.
     * Initialize scores, status label, and timeline for AI moves.
     */
    public Referee(Board board) {
        this.board = board;
        this.canClick = false;
        this.blackScore = 0;
        this.whiteScore = 0;

        this.statusLabel = new Label("");
        this.timeline = new Timeline(
                new KeyFrame(Constants.MOVE_DELAY, e -> moveTimeline())
        );
        this.timeline.setCycleCount(1);
    }

    /**
     * Called by the timeline to activate the current player.
     * Stops the timeline, lets the player make a move, then restarts it.
     */
    private void moveTimeline() {
        this.timeline.stop();
        this.currPlayer.activate(this.board);
        this.timeline.play();
    }

    /**
     * Handles a click on a square by a human player.
     * Delegates to placePiece if clicking is allowed.
     */
    public void handleClick(OthelloSquare square) {
        if (canClick) {
            int row = square.getRow();
            int col = square.getCol();
            this.placePiece(row, col);
        }
    }

    /**
     * Places a piece at the given row and column if valid.
     * Flips opponent pieces, updates score, switches turns,
     * and checks for game over.
     */
    public void placePiece(int row, int col) {
        OthelloSquare square = this.board.getSquares()[row][col];
        // Ignore if square has piece already
        if (!square.isEmpty() ||
             this.board.isValid(row, col, this.getPlayerColor()).size() == 0) {
            return;
        }

        // Place piece
        square.addPiece(this.getPlayerColor());

        // Flip pieces around this square
        this.board.flipPieces(square.getPiece(), this.getPlayerColor());

        if (!this.gameOver(this.board)) {
            // Switch turn
            switchTurn();
            this.updateStatusLabel();
        } else {
            this.board.resetBoardColor();
            this.statusLabel.setText("White: " + this.whiteScore + "Black: "
                                    + this.blackScore + "\n\n" + "Game Ended!"
                                    + "\n" + this.getWinner());
        }
    }

    /**
     * Switches turns between players.
     * Highlights valid squares for human players.
     * Plays AI moves using timeline if applicable.
     */
    public void switchTurn() {
        this.board.resetBoardColor();

        if (board.validSquares(nextPlayer.getColor()).size() != 0) {
            Player temp = this.currPlayer;
            this.currPlayer = this.nextPlayer;
            this.nextPlayer = temp;
            this.canClick = this.currPlayer.isClickable();
        }
        if (this.currPlayer.getMode() == 0) {
            this.board.highlightValidSquares(this.getPlayerColor());
        }
        timeline.play();
    }

    /**
     * Getters for current players
     */
    public Player getCurrPlayer() { return this.currPlayer; }

    /**
     * Getters for next players
     */
    public Player getNextPlayer() { return this.nextPlayer; }

    /**
     * Getters for current players colors
     */
    public Color getPlayerColor() {return this.currPlayer.getColor();}

    /**
     * Adds a player to the game.
     * Assigns black or white and sets clickable status for humans.
     */
    public void addPlayer(Player player) {
        if (this.currPlayer == null) {
            this.currPlayer = player;
            this.board.setBlackPlayer(player);
            this.canClick = this.currPlayer.isClickable();
        } else if (this.nextPlayer == null) {
            this.nextPlayer = player;
            this.board.setWhitePlayer(player);
        }
    }

    /**
     * Sets a player, updating the correct slot and board reference.
     */
    public void setPlayer(Player player) {
        if (this.currPlayer.getColor() == player.getColor()) {
            this.currPlayer = player;
            this.canClick = this.currPlayer.isClickable();
        } else {
            this.nextPlayer = player;
        }

        if (player.getColor() == Constants.BLACK_COLOR) {
            this.board.setBlackPlayer(player);
        } else {
            this.board.setWhitePlayer(player);
        }
    }

    /**
     * Sets the current player
     */
    public void setCurrPlayer(Player player) {
        this.currPlayer = player;
    }

    /**
     * Sets the next player
     */
    public void setNextPlayer(Player player) {
        this.nextPlayer = player;
    }

    /**
     * Determines if the game is over.
     * Game is over if both players have no valid moves or the board is full.
     */
    public boolean gameOver(Board board) {
        Player currPlayer = this.currPlayer;
        Player nextPlayer = this.nextPlayer;
        if (board.validSquares(currPlayer.getColor()).size() == 0
                && board.validSquares(nextPlayer.getColor()).size() == 0) {
            return true;
        } else if (board.isFull()) {
            return true;
        }
        return false;
    }

    /**
     * Updates the score when a piece is added or flipped.
     */
    public void updateScore(Color color, String type) {
        if (type == "flip") {
            if (color == Constants.BLACK_COLOR) {
                this.blackScore += 1;
                this.whiteScore -= 1;
            } else {
                this.whiteScore += 1;
                this.blackScore -= 1;
            }
        } else if (type == "add") {
            if (color == Constants.BLACK_COLOR) {
                this.blackScore += 1;
            } else {
                this.whiteScore += 1;
            }
        }
    }

    /**
     * Updates the GUI status label with current scores and player's turn.
     */
    public Node updateStatusLabel() {
        this.statusLabel.setText("White: " + this.whiteScore
                                + "Black: " + this.blackScore + "\n\n"
                                + this.getPlayerName() + "'s Turn");
        return this.statusLabel;
    }

    /**
     * Gets the name of the current player for display.
     */
    private String getPlayerName() {
        if (this.currPlayer != null) {
            if (this.getPlayerColor() == Constants.BLACK_COLOR) {
                return "Black";
            } else {
                return "White";
            }
        } else {
            return "Black";
        }
    }

    /**
     * Resets the referee for a new game.
     * Clears players, scores, clickable status, and stops AI timeline.
     */
    public void reset() {
        this.currPlayer = null;
        this.nextPlayer = null;
        this.blackScore = 0;
        this.whiteScore = 0;
        this.canClick = false;
        this.timeline.stop();
    }

    /**
     * Determines the winner of the game based on scores.
     */
    public String getWinner() {
        if (this.blackScore > this.whiteScore) {
            return "Winner is Black!";
        } else if (this.blackScore == this.whiteScore) {
            return "It's a Tie!";
        } else {
            return "Winner is White!";
        }
    }
}
