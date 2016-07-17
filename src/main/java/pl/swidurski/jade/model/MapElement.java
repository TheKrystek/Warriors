package pl.swidurski.jade.model;

import javafx.scene.Node;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MapElement {

    @Getter
    @Setter
    int x, y;

    @Getter
    @Setter
    ElementType type;

    @Setter
    @Getter
    Node node;

    MapEntry parent;

    public MapElement(int x, int y, ElementType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.node = buildNode();
    }

    private Node buildNode() {
        if (type == ElementType.PATH || type == ElementType.WALL)
            return new Text();

        return new Text(String.valueOf(type.getCharacter()));
    }

    public void setParent(MapEntry mapEntry) {
        if (parent != null) {
            parent.remove(this);
        }
        parent = mapEntry;
    }
}
