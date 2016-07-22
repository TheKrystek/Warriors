package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.helpers.PathHelper;
import pl.swidurski.jade.model.MapModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MapController extends Controller {
    @Getter
    @Setter
    MapModel mapModel;

    @FXML
    GridPane map;

    @Override
    public void init() {
        try {
            loadMap((String) getParameters());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String path) throws FileNotFoundException {
        InputStream stream = PathHelper.getStream(path);
        mapModel = MapModel.build(stream, map);
    }
}


