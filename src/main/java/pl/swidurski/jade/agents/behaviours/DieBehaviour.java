package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.OneShotBehaviour;
import pl.swidurski.jade.agents.WarriorAgent;

/**
 * Created by Krystek on 2016-07-20.
 */
public class DieBehaviour extends OneShotBehaviour {

    private final WarriorAgent agent;

    public DieBehaviour(WarriorAgent agent){
        this.agent = agent;
    }

    @Override
    public void action() {

    }
}
