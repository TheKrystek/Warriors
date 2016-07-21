package pl.swidurski.jade.model;

import javafx.scene.layout.GridPane;
import lombok.Getter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MapModel {
    private final int rows;
    private final int cols;
    private final GridPane grid;

    @Getter
    Map<String, MapEntry> map = new HashMap<>();


    public MapModel(int cols, int rows, GridPane pane) {
        this.cols = cols;
        this.rows = rows;
        this.grid = pane;
    }

    public MapElement add(int x, int y, ElementType type) {
        String key = getKey(x, y);
        MapEntry entry = new MapEntry(x, y, type);
        map.put(key, entry);
        grid.add(entry.getPane(), x, y);
        return null;
    }


    public void add(int x, int y, MapElement element) {
        String key = getKey(x, y);
        element.setX(x);
        element.setY(y);
        if (map.containsKey(key)) {
            MapEntry entry = map.get(key);
            if (entry.contains(element))
                return;
            entry.add(element);
            entry.setBackground();
        } else {
            MapEntry entry = new MapEntry(element);
            map.put(key, entry);
            grid.add(entry.getPane(), x, y);
        }
    }


    public int remove(int x, int y, ElementType element) {
        String key = getKey(x, y);
        if (map.containsKey(key)) {
            MapEntry entry = map.get(key);
            int sum = 0;
            List<MapElement> elements = entry.getElements();
            for (int i = 0; i < elements.size(); i++) {
                if (elements.get(i).getType() == element) {
                    sum += elements.get(i).getPoints();
                    entry.remove(elements.get(i));
                }
            }
            entry.setBackground();
            return sum;
        }
        return 0;
    }


    public void removeWarrior(State state) {
        String key = getKey(state.getPosX(), state.getPosY());
        if (map.containsKey(key)) {
            MapEntry entry = map.get(key);
            for (int i = 0; i < entry.getElements().size(); i++) {
                if (entry.getElements().get(i).getType() == ElementType.WARRIOR && entry.getElements().get(i).getAgent().equals(state.getAgent())) {
                    entry.remove(entry.getElements().get(i));
                }
            }
            entry.setBackground();
        }
    }

    public static String getKey(int x, int y) {
        return String.format("%s.%s", x, y);
    }

    public static MapModel build(InputStream stream, GridPane pane) {
        Scanner s = new Scanner(stream);
        int cols = s.nextInt();
        int rows = s.nextInt();

        MapModel model = new MapModel(cols, rows, pane);
        s.nextLine();
        for (int i = 0; i < rows; i++) {
            String line = s.nextLine();
            System.out.println(line);
            for (int j = 0; j < cols; j++) {
                model.add(j, i, ElementType.getElement(line.charAt(j)));
            }
        }
        return model;
    }

}
