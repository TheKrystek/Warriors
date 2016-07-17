package pl.swidurski.jade.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MapAgent;
import pl.swidurski.jade.model.State;
import pl.swidurski.jade.utils.AgentStateHelper;

/**
 * Created by Krystek on 2016-07-17.
 */
public class InformAboutMapState extends Behaviour {
    private MapAgent agent;
    private MessageTemplate mt = MessageTemplate.MatchConversationId(Const.INFORM_ABOUT_POSITION);

    public InformAboutMapState(MapAgent agent){
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                System.out.println("RECEIVED: " + reply.getContent());

                State state = AgentStateHelper.loadFromString(reply.getContent());

                String sender = reply.getSender().getLocalName();
                agent.getStates().put(sender, state);
                agent.move(sender);
                sendMapInfo(reply.getSender());
            }
        } else {
            block();
        }
    }

    public void sendMapInfo(AID agent) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent);
        msg.setContent("XXX");
        msg.setConversationId(Const.INFORM_ABOUT_MAP);

        String sender = agent.getLocalName();
        State state = this.agent.getStates().get(sender);

        this.agent.getInfo(state.getPosX(), state.getPosY());
        this.agent.send(msg);
    }

    @Override
    public boolean done() {
        return false;
    }
}
