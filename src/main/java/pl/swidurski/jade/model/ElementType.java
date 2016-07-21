package pl.swidurski.jade.model;

import lombok.Getter;

/**
 * Created by Krystek on 2016-07-16.
 */
public enum ElementType {

    WALL('#', "#2c3e50"),
    PATH('_', "#ecf0f1"),
    POTION('P', "#9b59b6"),
    MONSTER('M', "#d35400"),
    TREASURE('T', "#f1c40f"),
    WARRIOR('W', "#c0392b"), ;

    @Getter
    private final String color;

    @Getter
    char character;

    ElementType(char c, String color) {
        this.character = c;
        this.color = color;
    }


    public static ElementType getElement(char c) {
        switch (c) {
            case '#':
                return WALL;
            case '_':
                return PATH;
            case 'P':
                return POTION;
            case 'M':
                return MONSTER;
            case 'T':
                return TREASURE;
            default:
                return PATH;
        }
    }

    public static ElementType getElement(String s) {
        switch (s) {
            case "WALL":
                return WALL;
            case "PATH":
                return PATH;
            case "POTION":
                return POTION;
            case "MONSTER":
                return MONSTER;
            case "TREASURE":
                return TREASURE;
            case "WARRIOR":
                return WARRIOR;
            default:
                return PATH;
        }
    }
}
