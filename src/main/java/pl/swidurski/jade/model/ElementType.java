package pl.swidurski.jade.model;

import lombok.Getter;

/**
 * Created by Krystek on 2016-07-16.
 */
public enum ElementType {

    WALL('#', "black"),
    PATH('_', "white"),
    POTION('P', "blue"),
    MONSTER('M', "brown"),
    TREASURE('T', "yellow"),
    WARRIOR('W', "red");

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
}
