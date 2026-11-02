package othello;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * The Board class represents the visual and logical Othello board.
 * It manages the creation of squares, initial piece placement, move validation,
 * piece flipping, highlighting valid moves, and board reset operations.
 */
public class Board {

    private Pane pane;
    private OthelloSquare[][] squares;
    private Referee referee;
    private Player blackPlayer;
    private Player whitePlayer;
    /**
     * Constructs a new Board with an empty square grid,
     * creates the border and squares, sets up the Referee,
     * and places the initial four Othello pieces.
     */
    public Board() {
        this.pane = new Pane();

        this.squares = new OthelloSquare[Constants.ROWS][Constants.COLS];

        this.referee = new Referee(this); // add latest

        this.createBoard(); // draws border
        this.placeInitialPieces();
    }
    /**
     * Copy constructor — creates a simulated duplicate of an existing Board.
     * Used primarily by ComputerPlayer during minimax evaluation.
     */
    public Board(Board board) {
        this.pane = new Pane();
        this.squares = new OthelloSquare[Constants.ROWS][Constants.COLS];
        this.blackPlayer = board.blackPlayer;
        this.whitePlayer = board.whitePlayer;
        this.referee = new Referee(board);

        OthelloSquare[][] oldSquares = board.getSquares();
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS-1; col++) {
                OthelloSquare oldSq = oldSquares[row][col];
                OthelloSquare newSq = new OthelloSquare(
                    oldSq.getRow(), oldSq.getCol(), this.pane, this.referee);
                if (!oldSq.isEmpty()) {
                    OthelloPiece oldPiece = oldSq.getPiece();
                    newSq.addPiece(oldPiece.getColor());
                }
                this.squares[row][col] = newSq;
            }
        }
    }

    /** Sets the black player associated with this board. */
    public void setBlackPlayer(Player player) {
        this.blackPlayer = player;
    }

    /** Sets the white player associated with this board. */
    public void setWhitePlayer(Player player) {
        this.whitePlayer = player;
    }

    /** @return the Pane that visually displays the board */
    public Pane getPane() {
        return this.pane;
    }

    /** @return 2D array of board squares */
    public OthelloSquare[][] getSquares() { return this.squares; }

    /** @return the Referee managing gameplay */
    public Referee getReferee() {return this.referee; }

    /** Creates and draws the game board with borders */
    public void createBoard() {
        this.makeGraphicalBoard();
        this.pane.setPrefSize(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT);
        this.pane.setStyle(Constants.BOARD_STYLE);
    }

    /**
     * Creates all squares and border rectangles, populating the Pane.
     * Playable squares become OthelloSquare objects tied to the Referee.
     */
    private void makeGraphicalBoard() {
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                if (isBorder(row, col)) { // Green boarder
                    Rectangle boarder = new Rectangle(
                            col * Constants.SQUARE_WIDTH,
                            row * Constants.SQUARE_WIDTH,
                            Constants.SQUARE_WIDTH,
                            Constants.SQUARE_WIDTH);

                    boarder.setFill(Constants.BOARDER_COLOR);
                    boarder.setStroke(Constants.BOARDER_STROKE);
                    boarder.setStrokeWidth(Constants.STROKE_WIDTH); // thin grid line

                    this.pane.getChildren().add(boarder);
                }
                else {
                    this.squares[row][col] = new OthelloSquare(
                            row, col, this.pane, this.referee);
                    this.squares[row][col].addGraphically();
                }
            }
        }
    }

    /**
     * Determines whether a given row/column location is part of the green border.
     */
    private boolean isBorder(int row, int col) {
        return row == 0 || col == 0 ||
                row == Constants.ROWS - 1 ||
                col == Constants.COLS - 1;
    }

    /**
     * Places the four starting Othello pieces in the center of the board.
     */
    private void placeInitialPieces() {
        int mid = Constants.ROWS / 2;

        this.squares[mid - 1][mid - 1].addPiece(Constants.WHITE_COLOR);
        this.squares[mid][mid].addPiece(Constants.WHITE_COLOR);

        this.squares[mid - 1][mid].addPiece(Constants.BLACK_COLOR);
        this.squares[mid][mid - 1].addPiece(Constants.BLACK_COLOR);
    }

    /**
     * Recursively checks a single direction from a starting square to determine
     * whether placing a piece here would flip at least one opponent piece.
     */
    public boolean checkOneDirection(int row, int col, int rowDir,
                                     int colDir, Color playerColor,
                                     boolean seenOpponent) {
        OthelloSquare square = this.squares[row][col];
        if (this.isBorder(row, col) || square.isEmpty()) {
            return false;
        }
        if (square.getPiece().getColor() != playerColor) {
            return this.checkOneDirection(row + rowDir,
                    col + colDir, rowDir, colDir,
                    playerColor, true);
        }
        if (square.getPiece().getColor() == playerColor && seenOpponent) {
            return true;
        }
        return false;
    }

    /**
     * Determines whether the player may place a piece at (row, col),
     * and returns all directions in which flipping would occur.
     */
    public ArrayList<int[]> isValid(int row, int col, Color playerColor) {
        ArrayList<int[]> validDirArr = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int rowDir = Constants.DIRECTIONS[i][0];
            int colDir = Constants.DIRECTIONS[i][1];
            if (this.checkOneDirection(row + rowDir,
                    col + colDir, rowDir, colDir,
                    playerColor, false)) {
                int[] dir = {rowDir, colDir};
                validDirArr.add(dir);
            }
        }
        return validDirArr;
    }

    /**
     * Highlights all squares where the current player may legally play.
     */
    public void highlightValidSquares(Color playerColor) {
        for (OthelloSquare square: this.validSquares(playerColor)) {
            square.highlightSquare();
        }
    }

    /**
     * Computes all squares where a player may legally place a piece.
     */
    public ArrayList<OthelloSquare> validSquares(Color playerColor) {
        ArrayList<OthelloSquare> validSquareArr = new ArrayList<>();
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS-1; col++) {
                OthelloSquare square = this.squares[row][col];
                if (square.isEmpty()) {
                    if (this.isValid(row, col,
                            playerColor).size() != 0) {
                        validSquareArr.add(square);
                    }
                }
            }
        }
        return validSquareArr;
    }

    /**
     * Resets the visual background color of all squares to normal
     * (removes highlights).
     */
    public void resetBoardColor() {
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS - 1; col++) {
                OthelloSquare square = this.squares[row][col];
                square.resetSquareColor();
            }
        }
    }

    /**
     * Flips all opponent pieces in all valid flipping directions
     * after a piece is placed.
     */
    public void flipPieces(OthelloPiece piece, Color playerColor) {
        int row = piece.getRow();
        int col = piece.getCol();
        for (int[] dir: this.isValid(row, col, playerColor)) {
            this.flipOneDirection(row + dir[0],
                    col + dir[1], dir[0], dir[1], playerColor);
        }
    }

    /**
     * Recursively flips opponent pieces in one direction until reaching
     * the player's own piece.
     */
    public void flipOneDirection(int row, int col,
                                 int rowDir, int colDir,
                                 Color playerColor) {
        OthelloPiece piece = this.squares[row][col].getPiece();
        if (piece.getColor() !=playerColor) {
            piece.flipColor(playerColor);
            flipOneDirection(row + rowDir, col + colDir,
                    rowDir, colDir, playerColor);
            this.referee.updateScore(playerColor, "flip");
        }
    }

    /**
     * Return true if all playable squares contain a piece (board full)
     */
    public boolean isFull() {
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS - 1; col++) {
                if (this.squares[row][col].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Counts pieces for both sides.
     */
    public int[] countPieces() {
        OthelloSquare[][] squares = this.getSquares();
        int blackPieces = 0;
        int whitePieces = 0;
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS-1; col++) {
                OthelloPiece piece = squares[row][col].getPiece();
                if (piece != null) {
                    if (piece.getColor() == Constants.BLACK_COLOR) {
                        blackPieces += 1;
                    } else {
                        whitePieces += 1;
                    }
                }
            }
        }
        return new int[] {blackPieces, whitePieces};
    }

    /** @return the white player object */
    public Player getWhitePlayer() { return this.whitePlayer; }

    /** @return the black player object */
    public Player getBlackPlayer() { return this.blackPlayer; }

    /**
     * Resets the board to the initial state
     */
    public void reset() {
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS - 1; col++) {
                this.squares[row][col].removePiece();
            }
        }
        this.resetBoardColor();
        this.placeInitialPieces();
    }
}
