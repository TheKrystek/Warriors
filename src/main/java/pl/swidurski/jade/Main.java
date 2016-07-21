package pl.swidurski.jade;

import jade.Boot;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by Krystek on 2016-07-16.
 */
public class Main extends Application {
    static String[] args;

    public static void main(String[] args) {
        Main.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Boot.main(args);
    }
}
