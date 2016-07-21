package pl.swidurski.jade.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MapAgent;
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.MapElement;
import pl.swidurski.jade.model.MapState;
import pl.swidurski.jade.model.State;

/**
 * Created by Krystek on 2016-07-17.
 */
public class InformAboutMapStateBehaviour extends Behaviour {
    private MapAgent agent;
    private MessageTemplate mt = MessageTemplate.MatchConversationId(Const.INFORM_ABOUT_POSITION);

    public InformAboutMapStateBehaviour(MapAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                State state = State.loadFromString(reply.getContent());
                String sender = reply.getSender().getLocalName();
                if (state.getHp() > 0) {
                    agent.getStates().put(sender, state);
                    agent.move(sender);
                } else {
                    agent.getModel().remove(state);
                    agent.getWarriors().remove(reply.getSender());
                    agent.getStates().remove(reply.getSender());
                    int points = state.getPoints();

                    if (points > 0) {
                        MapElement t = new MapElement(state.getPosX(), state.getPosY(), ElementType.TREASURE);
                        t.setPoints(points);
                        agent.getModel().add(state.getPosX(), state.getPosY(), t);
                    }
                }

                sendMapInfo(reply.getSender());
            }
        } else {
            block();
        }
    }

    public void sendMapInfo(AID receiver) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(receiver);
        msg.setConversationId(Const.INFORM_ABOUT_MAP);
        msg.setContent(getMapState(receiver));
        agent.send(msg);
    }

    private String getMapState(AID receiver) {
        State state = agent.getStates().get(receiver.getLocalName());
        return MapState.toString(agent.getInfo(state.getPosX(), state.getPosY()));
    }

    @Override
    public boolean done() {
        return false;
    }
}
