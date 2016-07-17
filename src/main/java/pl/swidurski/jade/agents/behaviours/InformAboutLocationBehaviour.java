package pl.swidurski.jade.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.WarriorAgent;
import pl.swidurski.jade.utils.AgentStateHelper;

/**
 * Created by Krystek on 2016-07-17.
 */
public class InformAboutLocationBehaviour extends Behaviour {
    private final WarriorAgent agent;
    private boolean finish = false;
    private MessageTemplate mt = MessageTemplate.MatchConversationId(Const.INFORM_ABOUT_MAP);
    int step = 0;

    public InformAboutLocationBehaviour(WarriorAgent agent) {
        this.agent = agent;
    }


    @Override
    public void action() {
        switch (step){
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
                System.out.println("RECEIVED: " + reply.getContent());
                finish = true;
            }
        } else {
            block();
        }
    }

    public void sendPositionInformation() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getMapAgent());
        msg.setContent(AgentStateHelper.saveToString(agent));
        msg.setConversationId(Const.INFORM_ABOUT_POSITION);
        agent.send(msg);
    }

    @Override
    public boolean done() {
        return finish;
    }
}
