package pl.swidurski.jade.utils;

import pl.swidurski.jade.agents.WarriorAgent;
import pl.swidurski.jade.model.State;

/**
 * Created by Krystek on 2016-07-17.
 */
public class AgentStateHelper {

    public static String saveToString(WarriorAgent agent){
        State state = agent.getInternalState();
        return String.format("%s;%s;%s;%s;%s;%s",
                agent.getAID().getLocalName(),
                state.getPosX(),
                state.getPosY(),
                state.getHp(),
                state.getMaxHp(),
                state.getDamage());
    }

    public static State loadFromString(String string){
        String[] s = string.split(";");
        State state = new State();

        state.setPosX(Integer.parseInt(s[1]));
        state.setPosY(Integer.parseInt(s[2]));
        state.setHp(Integer.parseInt(s[3]));
        state.setMaxHp(Integer.parseInt(s[4]));
        state.setDamage(Integer.parseInt(s[5]));

        return state;
    }

    public static void loadFromString(WarriorAgent agent, String string){
        agent.setInternalState(loadFromString(string));
    }


}
