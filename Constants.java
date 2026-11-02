package othello;

import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Constants {

    public static final int WHITE = 0;
    public static final int BLACK = 1;
    public static final int CONTROLS_PANE_WIDTH = 250;

    public static final Color WHITE_COLOR = Color.WHITE;
    public static final Color BLACK_COLOR = Color.BLACK;

    public static final Color HIGHLIGHT_COLOR = Color.BLANCHEDALMOND;
    public static final int PIECE_RADIUS = 20;

    public static final int SCENE_WIDTH = 800;
    public static final int SCENE_HEIGHT = 500;
    public static final int BOARD_WIDTH = 500;
    public static final int BOARD_HEIGHT = 500;

    public static final int SQUARE_WIDTH = 50;
    public static final int COLS = 8 + 2;
    public static final int ROWS = 8 + 2;
    public static final Color BOARDER_COLOR = Color.GREEN;
    public static final Color BOARDER_STROKE = Color.BLACK;
    public static final double STROKE_WIDTH = 0.5;
    public static final String BOARD_STYLE =
            "-fx-background-color: black; -fx-border-color: " +
                    "white; -fx-border-width: 2px;";
    public static final Color BOARD_COLOR = Color.DARKGRAY;
    public static final Duration MOVE_DELAY = Duration.millis(500);

    public static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
    };

    public static final int[][] WEIGHTS = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 200, -70, 30, 25, 25, 30, -70, 200, 0},
            {0, -70, -100, -10, -10, -10, -10, -100, -70, 0},
            {0, 30, -10, 2, 2, 2, 2, -10, 30, 0},
            {0, 25, -10, 2, 2, 2, 2, -10, 25, 0},
            {0, 25, -10, 2, 2, 2, 2, -10, 25, 0},
            {0, 30, -10, 2, 2, 2, 2, -10, 30, 0},
            {0, -70, -100, -10, -10, -10, -10, -100, -70, 0},
            {0, 200, -70, 30, 25, 25, 30, -70, 200, 0}
    };


}
