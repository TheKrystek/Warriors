package pl.swidurski.jade.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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

    @FXML
    TextField column;
    @FXML
    TextField row;


    MapElement warrior = new MapElement(0, 0, ElementType.WARRIOR);

    @FXML
    public void click() {
        int c = Integer.parseInt(column.getText());
        int r = Integer.parseInt(row.getText());

        mapModel.add(c, r, warrior);
    }


    @Override
    public void init() {
        loadMap((String) getParameters());
    }

    public void loadMap(String path) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        mapModel = MapModel.build(stream, map);
    }

    public FlowPane getNode(ElementType element) {
        FlowPane node = new FlowPane();

        if (element != ElementType.PATH && element != ElementType.WALL) {
            String text = String.valueOf(element.getCharacter());
            if (element == ElementType.TREASURE) {
                node.getChildren().add(new Text("M"));
            }
            node.getChildren().add(new Text(text));
        }

        return node;
    }


}
