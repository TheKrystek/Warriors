package pl.swidurski.jade.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.WarriorAgent;
import pl.swidurski.jade.model.ElementType;

/**
 * Created by Krystek on 2016-07-20.
 */
public class PickupBehaviour extends Behaviour {

    private final ElementType type;
    private final WarriorAgent agent;
    private MessageTemplate mt = MessageTemplate.MatchConversationId(Const.PICKUP);

    int step = 0;
    private boolean finish = false;

    public PickupBehaviour(WarriorAgent agent, ElementType type) {
        this.agent = agent;
        this.type = type;
    }

    @Override
    public void action() {
        if (step == 0) {
            sendMessage(agent.getMapAgent());
            step++;
        }
        else {
            ACLMessage reply = agent.receive(mt);
            if (reply != null) {
                if (reply.getPerformative() == ACLMessage.INFORM) {
                    int value = Integer.parseInt(reply.getContent());
                    if (type == ElementType.TREASURE) {
                        int point = agent.getInternalState().getPoints() + value;
                        agent.getInternalState().setPoints(point);
                    }
                    if (type == ElementType.POTION) {
                        agent.getInternalState().addHp(value);
                    }
                    agent.update();
                    finish = true;
                }
            } else {
                block();
            }
        }
    }

    @Override
    public boolean done() {
        return finish;
    }


    public void sendMessage(AID receiver) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiver);
        msg.setConversationId(Const.PICKUP);
        msg.setContent(type.toString());
        agent.send(msg);
    }
}
