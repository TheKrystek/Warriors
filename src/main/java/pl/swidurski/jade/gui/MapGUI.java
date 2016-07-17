package pl.swidurski.jade.gui;

import javafx.application.Platform;
import javafx.stage.Stage;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MapAgent;
import pl.swidurski.jade.model.MapModel;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MapGUI {
    private MapAgent agent;
    private Stage stage;
    private SceneManager sceneManager;


    public MapGUI(MapAgent agent) {
        this.agent = agent;
        Platform.runLater(() -> configure());
    }

    private void configure() {
        stage = new Stage();
        stage.setTitle(agent.getLocalName());
        sceneManager = new SceneManager(stage, Const.MAP_GUI);
    }

    public void close() {
        if (stage != null)
            Platform.runLater(() -> stage.close());
    }

    public MapModel getMapModel(){
        if (sceneManager.getMapController() == null)
            return null;
        return sceneManager.getMapController().getMapModel();
    }
}
