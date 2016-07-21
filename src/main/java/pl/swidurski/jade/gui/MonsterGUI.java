package pl.swidurski.jade.gui;

import javafx.application.Platform;
import javafx.stage.Stage;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MonsterAgent;
import pl.swidurski.jade.agents.WarriorAgent;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MonsterGUI {
    private MonsterAgent agent;
    private Stage stage;
    private SceneManager sceneManager;


    public MonsterGUI(MonsterAgent agent) {
        this.agent = agent;
        Platform.runLater(() -> configure());
    }

    private void configure() {
        stage = new Stage();
        stage.setTitle(agent.getLocalName());
        sceneManager = new SceneManager(stage, Const.MONSTER_GUI, 320, 220, false, agent);
    }

    public void close() {
        if (stage != null)
            Platform.runLater(() -> stage.close());
    }

    public void update() {
        Platform.runLater(() -> sceneManager.getMonsterController().update());
    }
}
