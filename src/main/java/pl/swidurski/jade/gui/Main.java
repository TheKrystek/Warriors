package pl.swidurski.jade.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new SceneManager(primaryStage, "gui/ChooseMap.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
