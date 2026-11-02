package othello;

import javafx.scene.paint.Color;

/**
 * SetupGame is responsible for initializing and managing the Othello game.
 * It creates the board, referee, and players, and handles game start and reset.
 */
public class SetupGame {

    private Board board;
    private Referee referee;
    private boolean started;

    /**
     * Constructs a new SetupGame instance.
     * Initializes a board, obtains the referee from the board,
     * and marks the game as not started.
     */
    public SetupGame(){
        this.board = new Board();
        this.referee = this.board.getReferee();
        this.started = false;
    }

    /**
     * Returns the game board.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Returns the referee.
     */
    public Referee getReferee() {
        return this.referee;
    }

    /**
     * Sets the players for the game based on selected modes.
     * Mode 0 = HumanPlayer, Mode 1-3 = ComputerPlayer levels.
     * If the game hasn't started, adds the players to the
     * referee and highlights valid moves.
     * If the game is already started, updates the existing
     * players in the referee.
     */
    public void setPlayers(int modeBlackPlayer, int modeWhitePlayer) {
        Player blackPlayer = this.createPlayer(modeBlackPlayer,
                Constants.BLACK_COLOR);
        Player whitePlayer = this.createPlayer(modeWhitePlayer,
                Constants.WHITE_COLOR);
        if (!this.started) {
            this.referee.addPlayer(blackPlayer);
            this.referee.addPlayer(whitePlayer);
            if (modeBlackPlayer == 0) {
                this.board.highlightValidSquares(this.referee.getPlayerColor());
            }
            this.started = true;
            this.referee.getCurrPlayer().activate(this.board);
        } else {
            this.referee.setPlayer(blackPlayer);
            this.referee.setPlayer(whitePlayer);
            this.referee.getCurrPlayer().activate(this.board);
        }
    }

    /**
     * Creates a Player instance based on mode and color.
     */
    private Player createPlayer(int mode, Color color) {
        Player player;
        switch (mode) {
            case 0:
                player = new HumanPlayer(color);
                break;
            case 1:
                player = new ComputerPlayer(1, color, this.referee);
                break;
            case 2:
                player = new ComputerPlayer(2, color, this.referee);
                break;
            default:
                player = new ComputerPlayer(3, color, this.referee);
                break;
        }
        return player;
    }

    /**
     * Resets the game to its initial state.
     * Resets the started flag, referee, and board.
     */
    public void reset() {
        this.started = false;
        this.referee.reset();
        this.board.reset();
    }
}
