package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.gui.FighterAgent;
import pl.swidurski.jade.model.MapState;

import java.util.List;

/**
 * Created by Krystek on 2016-07-17.
 */
public class InformAboutLocationBehaviour extends Behaviour {
    private final FighterAgent agent;
    private boolean finish = false;
    private MessageTemplate mt = MessageTemplate.MatchConversationId(Const.INFORM_ABOUT_MAP);
    int step = 0;

    public InformAboutLocationBehaviour(FighterAgent agent) {
        this.agent = agent;
    }


    @Override
    public void action() {
        switch (step) {
            case 0:
                sendPositionInformation();
                step++;
                break;
            case 1:
                waitForMapState();
                break;
        }
    }

    private void waitForMapState() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                List<MapState> list = MapState.fromString(reply.getContent());
                agent.setMapState(list);
                if (agent.getInternalState().getHp() <= 0) {
                    agent.getInternalState().setPoints(0);
                    agent.update();
                }
                finish = true;
            }
        } else {
            block();
        }
    }

    public void sendPositionInformation() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getMapAgent());
        msg.setContent(agent.getInternalState().toString());
        msg.setConversationId(Const.INFORM_ABOUT_POSITION);
        agent.send(msg);
    }

    @Override
    public boolean done() {
        return finish;
    }
}
