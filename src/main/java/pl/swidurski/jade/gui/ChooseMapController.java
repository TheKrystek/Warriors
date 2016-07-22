package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.helpers.PathHelper;

import java.io.File;
import java.io.FileNotFoundException;

public class ChooseMapController extends Controller {
    @FXML
    ComboBox<String> mapSelector;

    @Override
    public void init() {
        try {
            loadMaps();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadMaps() throws FileNotFoundException {
        File maps = PathHelper.getFile(Const.MAPS);
        File[] files = maps.listFiles();
        for (File file : files) {
            if (file.getName().endsWith(".map"))
                mapSelector.getItems().add(file.getName());
        }
    }

    @FXML
    public void confirm() {
        String item = mapSelector.getSelectionModel().getSelectedItem();
        if (item == null)
            return;
        getManager().open("gui/Map.fxml", String.format("%s%s", Const.MAPS, item));
    }

}
