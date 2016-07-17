package pl.swidurski.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.agents.behaviours.FindWarriorsBehaviour;
import pl.swidurski.jade.agents.behaviours.GetMapModelBehaviour;
import pl.swidurski.jade.agents.behaviours.InformAboutMapState;
import pl.swidurski.jade.gui.MapGUI;
import pl.swidurski.jade.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MapAgent extends Agent {
    @Getter
    MapGUI gui;

    @Setter
    @Getter
    MapModel model;

    @Setter
    @Getter
    List<AID> warriors = new ArrayList<>();

    @Setter
    @Getter
    Map<String, State> states = new HashMap<>();
    Map<String, MapElement> elements = new HashMap<>();


    @Override
    protected void setup() {
        super.setup();
        launchGUI();
        addBehaviours();
    }

    private void addBehaviours() {
        addBehaviour(new GetMapModelBehaviour(this));
        addBehaviour(new FindWarriorsBehaviour(this));
        addBehaviour(new InformAboutMapState(this));
    }

    private void launchGUI() {
        gui = new MapGUI(this);
    }


    @Override
    protected void takeDown() {
        super.takeDown();
        gui.close();
    }


    private MapElement createElement(String agent) {
        State state = states.get(agent);
        MapElement element = new MapElement(state.getPosX(), state.getPosY(), ElementType.WARRIOR);
        elements.put(agent, element);
        return element;
    }

    public void move(String agent){
        State state = states.get(agent);
        MapElement element;
        if (!elements.containsKey(agent))
            element = createElement(agent);
        else
            element = elements.get(agent);
        getModel().add(state.getPosX(), state.getPosY(), element);
    }

    public void getMapInfo(int x, int y){
        String key = model.getKey(x, y);
        MapEntry entry = model.getMap().get(key);
        for (MapElement element : entry.getElements()) {
            System.out.println(key + " " + element.getType());
        }
    }


}


