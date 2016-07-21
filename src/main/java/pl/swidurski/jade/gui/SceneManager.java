package pl.swidurski.jade.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.agents.MonsterAgent;
import pl.swidurski.jade.agents.WarriorAgent;

import java.io.IOException;

/**
 * Created by Krystek on 2016-07-16.
 */
public class SceneManager {

    @Getter
    @Setter
    FighterAgent fighterAgent;

    @Getter
    Stage stage;

    @Getter
    Controller controller;

    @Getter
    MapController mapController;

    @Getter
    WarriorController warriorController;

    @Getter
    MonsterController monsterController;


    public SceneManager(Stage primaryStage, String s) {
        this(primaryStage, s, 550, 550, true);
    }

    public SceneManager(Stage primaryStage, String s, int width, int height, boolean resizable) {
        this(primaryStage, s, width, height, resizable, null);
    }

    public SceneManager(Stage primaryStage, String s, int width, int height, boolean resizable, FighterAgent agent) {
        this.stage = primaryStage;
        setFighterAgent(agent);
        primaryStage.setScene(new Scene(load(s), width, height));
        primaryStage.setResizable(resizable);
        primaryStage.show();
    }

    public Parent load(String s) {
        return load(s, null);
    }

    public Parent load(String s, Object parameters) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getClassLoader().getResourceAsStream(s));
            controller = loader.getController();
            controller.setParameters(parameters);
            if (controller instanceof MapController)
                mapController = (MapController) controller;


            if (controller instanceof MonsterController) {
                monsterController = (MonsterController) controller;
                monsterController.setMonsterAgent((MonsterAgent) getFighterAgent());
            }

            if (controller instanceof WarriorController) {
                warriorController = (WarriorController) controller;
                warriorController.setWarriorAgent((WarriorAgent) getFighterAgent());
            }

            controller.setManager(this);
            controller.init();
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void open(String s, Object parameters) {
        stage.getScene().setRoot(load(s, parameters));
    }

}
