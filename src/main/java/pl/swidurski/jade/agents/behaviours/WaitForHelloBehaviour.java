package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.agents.WarriorAgent;

/**
 * Created by Krystek on 2016-07-16.
 */
public class WaitForHelloBehaviour extends Behaviour {
    private final WarriorAgent agent;
    private MessageTemplate mt = MessageTemplate.MatchConversationId("hello");
    private boolean finish = false;

    public WaitForHelloBehaviour(WarriorAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                System.out.println("RECEIVED: " + reply.getContent());
                agent.setMapAgent(reply.getSender());
                agent.addBehaviour(new InformAboutLocationBehaviour(agent));
            }
            finish = true;
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return finish;
    }
}
