package othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
  * This is the main class where the Othello game will start.
  * The main method of this application calls the App constructor.
  * The main method of this application calls launch,
  * which eventually calls the start method below.
  */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // Create top-level object, set up the scene, and show the stage here.
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(),
                Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("I Hate Othello");
        stage.show();
    }

    public static void main(String[] argv) {
        launch(argv);
    }
}
