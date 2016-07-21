package pl.swidurski.jade.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import lombok.Getter;
import lombok.Setter;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.behaviours.FightBehaviour;
import pl.swidurski.jade.agents.behaviours.WaitForHelloBehaviour;
import pl.swidurski.jade.gui.WarriorGUI;
import pl.swidurski.jade.model.MapState;
import pl.swidurski.jade.model.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-07-16.
 */
public class WarriorAgent extends Agent {
    @Getter
    @Setter
    AID mapAgent;

    @Getter
    @Setter
    private AgentMode mode = AgentMode.LOOK_FOR_TREASURE;


    @Getter
    @Setter
    private State internalState;

    @Getter
    @Setter
    private List<MapState> mapState;

    @Getter
    @Setter
    private List<MapState> visited = new ArrayList<>();

    @Getter
    WarriorGUI gui;


    public void addHp(int hp) {
        int currHp = getInternalState().getHp();
        getInternalState().setHp(currHp + hp);
        getGui().update();
    }

    @Override
    protected void setup() {
        super.setup();
        launchGUI();
        internalState = new State();
        internalState.setAgent(getAID().getLocalName());
        addBehaviour(new WaitForHelloBehaviour(this));
        addBehaviour(new FightBehaviour(this));
    }


    private void launchGUI() {
        gui = new WarriorGUI(this);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        gui.close();
    }

    public void registerInYellowPages() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(Const.WARRIOR_TYPE);
        sd.setName(Const.WARRIOR_NAME);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public void makeDecision(List<MapState> list) {
        this.mapState = list;
    }


}
