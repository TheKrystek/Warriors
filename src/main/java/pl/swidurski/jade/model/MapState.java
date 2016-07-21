package pl.swidurski.jade.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-07-17.
 */
public class MapState {

    public MapState(String string) {
        String[] s = string.split(",");
        x = Integer.parseInt(s[0]);
        y = Integer.parseInt(s[1]);
        type = ElementType.getElement(s[2]);
        if (type == ElementType.WARRIOR || type == ElementType.MONSTER)
            agent = s[3];
    }

    public MapState(MapElement element) {
        x = element.getX();
        y = element.getY();
        agent = element.getAgent();
        type = element.getType();
    }

    @Getter
    @Setter
    int x, y;
    @Getter
    @Setter
    String agent;
    @Getter
    @Setter
    ElementType type;

    @Override
    public String toString() {
        String result = String.format("%s,%s,%s", x, y, type);
        if (type == ElementType.WARRIOR || type == ElementType.MONSTER)
            return String.format("%s,%s", result, agent);
        return result;
    }

    public static String toString(List<MapState> list) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            s.append(list.get(i).toString());
            if (i < list.size() - 1)
                s.append("|");
        }
        return s.toString();
    }

    public static List<MapState> fromString(String string) {
        List<MapState> result = new ArrayList<>();
        String[] split = string.split("\\|");
        for (String s : split)
            result.add(new MapState(s));
        return result;
    }
}
