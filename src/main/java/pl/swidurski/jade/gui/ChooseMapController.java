package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import pl.swidurski.jade.Const;

public class ChooseMapController extends Controller {
    private static final String[] maps = new String[]{"base.map"};

    @FXML
    ComboBox<String> mapSelector;

    @Override
    public void init() {
        loadMaps();
    }

    private void loadMaps() {
        mapSelector.getItems().addAll(maps);
    }


    @FXML
    public void confirm() {
        String item = mapSelector.getSelectionModel().getSelectedItem();
        if (item == null)
            return;
        getManager().open("gui/Map.fxml", String.format("%s%s", Const.MAPS, item));
    }

}
