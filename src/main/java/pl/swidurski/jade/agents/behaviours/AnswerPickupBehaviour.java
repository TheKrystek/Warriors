package pl.swidurski.jade.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MapAgent;
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.MapElement;

/**
 * Created by Krystek on 2016-07-17.
 */
public class AnswerPickupBehaviour extends Behaviour {
    private MapAgent agent;
    private MessageTemplate mt = MessageTemplate.MatchConversationId(Const.PICKUP);

    public AnswerPickupBehaviour(MapAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                System.out.println("RECEIVED: " + reply.getContent());
                String sender = reply.getSender().getLocalName();
                ElementType type = ElementType.getElement(reply.getContent());
                MapElement element = agent.getElements().get(sender);
                int val = agent.getModel().remove(element.getX(), element.getY(), type);
                send(reply.getSender(), val);
            }
        } else {
            block();
        }
    }

    public void send(AID receiver, int val) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiver);
        msg.setConversationId(Const.PICKUP);
        msg.setContent(String.valueOf(val));
        agent.send(msg);
    }

    @Override
    public boolean done() {
        return false;
    }
}
