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
import pl.swidurski.jade.agents.behaviours.WaitForHelloBehaviour;
import pl.swidurski.jade.gui.WarriorGUI;
import pl.swidurski.jade.model.State;

/**
 * Created by Krystek on 2016-07-16.
 */
public class WarriorAgent extends Agent {

    @Getter
    @Setter
    AID mapAgent;

    @Getter
    @Setter
    private State internalState = new State();



    @Getter
    WarriorGUI gui;

    @Override
    protected void setup() {
        super.setup();
        launchGUI();
        addBehaviour(new WaitForHelloBehaviour(this));
    }


    private void launchGUI() {
        gui = new WarriorGUI(this);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
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
}
