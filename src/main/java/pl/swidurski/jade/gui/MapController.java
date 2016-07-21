package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.MapElement;
import pl.swidurski.jade.model.MapModel;

import java.io.InputStream;

public class MapController extends Controller {
    @Getter
    @Setter
    MapModel mapModel;

    @FXML
    GridPane map;

    @Override
    public void init() {
        loadMap((String) getParameters());
    }

    public void loadMap(String path) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        mapModel = MapModel.build(stream, map);
    }
}
