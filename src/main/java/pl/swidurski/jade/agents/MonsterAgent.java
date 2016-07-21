package pl.swidurski.jade.agents;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.behaviours.FightBehaviour;
import pl.swidurski.jade.agents.behaviours.WaitForHelloBehaviour;
import pl.swidurski.jade.gui.FighterAgent;
import pl.swidurski.jade.gui.MonsterGUI;
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.State;

/**
 * Created by Krystek on 2016-07-16.
 */
public class MonsterAgent extends FighterAgent {
    public MonsterGUI gui;

    @Override
    protected void setup() {
        super.setup();
        launchGUI();
        setMode(AgentMode.WAIT);
        setInternalState(new State(ElementType.MONSTER));
        getInternalState().setAgent(getAID().getLocalName());
        addBehaviour(new WaitForHelloBehaviour(this));
        addBehaviour(new FightBehaviour(this));
    }

    private void launchGUI() {
        gui = new MonsterGUI(this);
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
        sd.setType(Const.MONSTER_TYPE);
        sd.setName(Const.MONSTER_NAME);
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
