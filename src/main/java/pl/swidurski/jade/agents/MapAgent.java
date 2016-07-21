package pl.swidurski.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.agents.behaviours.*;
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
    @Setter
    @Getter
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
        addBehaviour(new InformAboutMapStateBehaviour(this));
        addBehaviour(new AnswerPickupBehaviour(this));
        addBehaviour(new SuperviseFightBehaviour(this));
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
        MapElement element = new MapElement(state.getPosX(), state.getPosY(), state.getType());
        element.setAgent(agent);
        elements.put(agent, element);
        return element;
    }

    public void move(String agent) {
        State state = states.get(agent);
        MapElement element;
        if (!elements.containsKey(agent)) {
            element = createElement(agent);
            if (element.getType() == ElementType.MONSTER)
                getModel().add(state.getPosX(), state.getPosY(), new MapElement(state.getPosX(), state.getPosY(), ElementType.TREASURE));
        } else
            element = elements.get(agent);
        getModel().add(state.getPosX(), state.getPosY(), element);
    }

    private List<MapState> getMapInfo(int x, int y) {
        List<MapState> result = new ArrayList<>();
        String key = model.getKey(x, y);
        MapEntry entry = model.getMap().get(key);
        for (MapElement element : entry.getElements()) {
            ElementType type = element.getType();
            if (type == ElementType.WALL)
                continue;
            result.add(new MapState(element));
        }
        return result;
    }

    public List<MapState> getInfo(int x, int y) {
        List<MapState> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                result.addAll(getMapInfo(x + i, y + j));
        return result;
    }

    public AID getAIDByName(String name) {
        for (AID aid : warriors) {
            if (aid.getLocalName().equals(name))
                return aid;
        }
        return null;
    }
}


