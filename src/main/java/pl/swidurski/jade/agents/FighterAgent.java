package pl.swidurski.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.agents.AgentMode;
import pl.swidurski.jade.model.MapState;
import pl.swidurski.jade.model.State;

import java.util.List;

/**
 * Created by Krystek on 2016-07-21.
 */
public abstract class FighterAgent extends Agent {
    @Getter
    @Setter
    AID mapAgent;

    @Getter
    @Setter
    private AgentMode mode;


    @Getter
    @Setter
    private State internalState;

    @Getter
    @Setter
    private List<MapState> mapState;


    public abstract void update();
}
