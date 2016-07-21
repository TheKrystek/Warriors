package pl.swidurski.jade.model;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MapEntry {
    @Getter
    private List<MapElement> elements = new ArrayList<>();

    @Getter
    @Setter
    FlowPane pane = new FlowPane(4, 4);

    public MapEntry(int x, int y, ElementType type) {
        if (type != ElementType.WALL && type != ElementType.PATH)
            add(new MapElement(x, y, ElementType.PATH));
        add(new MapElement(x, y, type));
        setBackground();
        setSize();
    }

    public MapEntry(MapElement element) {
        add(element);
    }


    private void setSize() {
        pane.setMaxSize(Const.SIZE.get(), Const.SIZE.get());
        pane.setMinSize(Const.SIZE.get(), Const.SIZE.get());
    }

    public MapElement add(MapElement element) {
        element.setParent(this);
        elements.add(element);
        Platform.runLater(() -> pane.getChildren().add(element.getNode()));
        return element;
    }

    public void remove(MapElement element) {
        elements.remove(element);
        Platform.runLater(() -> pane.getChildren().remove(element.getNode()));
        setBackground();
    }


    public void setBackground() {
        pane.setAlignment(Pos.CENTER);
        if (setBackgroundColor(firstOfType(ElementType.WALL)))
            return;
        if (setBackgroundColor(firstOfType(ElementType.TREASURE)))
            return;
        if (setBackgroundColor(firstOfType(ElementType.WARRIOR)))
            return;
        if (setBackgroundColor(firstOfType(ElementType.MONSTER)))
            return;
        if (setBackgroundColor(firstOfType(ElementType.POTION)))
            return;
        if (setBackgroundColor(firstOfType(ElementType.PATH)))
            return;
    }

    private boolean setBackgroundColor(Optional<MapElement> first) {
        if (first.isPresent()) {
            try {
                pane.setStyle("-fx-background-color: " + first.get().getType().getColor());
            } catch (Exception e) {}
            return true;
        }
        return false;
    }

    private Optional<MapElement> firstOfType(ElementType type) {
        return elements.stream().filter(p -> p.getType() == type).findFirst();

    }


    public boolean contains(MapElement element) {
        return elements.contains(element);
    }
}
