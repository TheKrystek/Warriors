package pl.swidurski.jade.agents;

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
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.MapState;
import pl.swidurski.jade.model.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krystek on 2016-07-16.
 */
public class WarriorAgent extends FighterAgent {
    @Getter
    @Setter
    private List<MapState> visited = new ArrayList<>();

    WarriorGUI gui;

    @Override
    protected void setup() {
        super.setup();
        launchGUI();
        setMode(AgentMode.LOOK_FOR_TREASURE);
        setInternalState(new State(ElementType.WARRIOR));
        getInternalState().setAgent(getAID().getLocalName());
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

    @Override
    public void update() {
        gui.update();
    }
}
