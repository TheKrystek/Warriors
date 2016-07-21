package pl.swidurski.jade;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Krystek on 2016-07-16.
 */
public class Const {
    public static final String MAPS = "maps/";

    public static final SimpleIntegerProperty SIZE = new SimpleIntegerProperty(50);
    
    
    public static final String WARRIOR_TYPE = "warrior";
    public static final String MONSTER_TYPE = "monster";
    public static final String WARRIOR_NAME = "JADE-WARRIORS";
    public static final String MONSTER_NAME = "JADE-MONSTER";
    public static final String MAP_GUI = "gui/ChooseMap.fxml";
    public static final String WARRIOR_GUI = "gui/Warrior.fxml";
    public static final String INFORM_ABOUT_POSITION = "inform-about-position";
    public static final String INFORM_ABOUT_MAP = "inform-about-map";
    public static final String PICKUP = "pickup";
    public static final String ATTACK = "attack";
}
