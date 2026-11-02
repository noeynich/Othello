package othello;

import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * The ComputerPlayer class implements an AI-controlled Othello player.
 * The AI uses a provided algorithm to evaluate possible moves and
 * choose the one that maximizes its score.
 * Intelligence levels (1–3) determine the search depth.
 */
public class ComputerPlayer implements Player {
    private Color color;
    private int intel;
    private Referee referee;

    /**
     * Constructs a ComputerPlayer.
     * @param intel AI intelligence level (1–3); determines minimax depth
     * @param color the player's color (black or white)
     * @param referee reference to the Referee managing the game
     */
    public ComputerPlayer(int intel, Color color, Referee referee) {
        this.color = color;
        this.intel = intel;
        this.referee = referee;
    }

    @Override
    public Color getColor() { return this.color; }

    @Override
    public int getMode() { return this.intel; }

    @Override
    public boolean isClickable() { return false; }

    /**
     * Evaluates a board position for the given player by summing positional
     * weights for their pieces and subtracting weights for opponent pieces.
     */
    public int evaluateBoard(Board board, Player currPlayer) {
        OthelloSquare[][] squares = board.getSquares();
        Color color = currPlayer.getColor();
        int value = 0;
        for (int row = 1; row < Constants.ROWS-1; row++) {
            for (int col = 1; col < Constants.COLS - 1; col++) {
                OthelloPiece piece = squares[row][col].getPiece();
                if (piece != null) {
                    if (piece.getColor() == color) {
                        value += Constants.WEIGHTS[row][col];
                    } else {
                        value -= Constants.WEIGHTS[row][col];
                    }
                }
            }
        }
        return value;
    }

    /**
     * Performs a negamax-style minimax search to find the best move for the
     * current player at a given search depth.
     */
    public Move getBestMove(Board board, int intel, Player currPlayer) {
        Referee referee = board.getReferee();
        int index = 0;
        if (currPlayer.getColor() == Constants.WHITE_COLOR) {
            index = 1;
            referee.setCurrPlayer(board.getWhitePlayer());
            referee.setNextPlayer(board.getBlackPlayer());
        } else {
            referee.setCurrPlayer(board.getBlackPlayer());
            referee.setNextPlayer(board.getWhitePlayer());
        }


        if (referee.gameOver(board)) {
            int currPieces = board.countPieces()[index];
            int oppPieces = board.countPieces()[1-index];
            if (currPieces > oppPieces) { return new Move(5, 5, 10000); }
            else if (currPieces == oppPieces) { return new Move(5, 5, 0); }
            else { return new Move(5, 5, -10000); }
        }

        ArrayList<OthelloSquare> validSquaresArr = board.validSquares(currPlayer.getColor());
        if (validSquaresArr.size() == 0) {
            if (intel == 1) {
                return new Move(5, 5, -10000);
            } else {
                Move move = this.getBestMove(board, intel - 1, referee.getNextPlayer());
                move.setValue(move.getValue() * -1);
                return move;
            }
        }

        int bestValue = -100000;
        int bestRow = 0;
        int bestCol = 0;
        for (OthelloSquare square: validSquaresArr) {
            Board copiedBoard = new Board(board);
            int row = square.getRow();
            int col = square.getCol();
            OthelloSquare[][] copiedBoardSquares = copiedBoard.getSquares();

            copiedBoardSquares[row][col].addPiece(currPlayer.getColor());
            copiedBoard.flipPieces(copiedBoardSquares[row][col].getPiece(), currPlayer.getColor());

            int value;
            if (intel == 1) {
                value = this.evaluateBoard(copiedBoard, currPlayer);
            } else {
                value = -1 * this.getBestMove(copiedBoard, intel - 1, referee.getNextPlayer()).getValue();
            }
            bestValue = Math.max(bestValue, value);
            if (bestValue == value) {
                bestRow = row;
                bestCol = col;
            }
        }
        return new Move(bestRow, bestCol, bestValue);
    }

    @Override
    public void activate(Board board) {
        Board copiedBoard = new Board(board);
        Move move = this.getBestMove(copiedBoard, this.intel, this);

        this.referee.placePiece(move.getRow(), move.getCol());
    }

}
