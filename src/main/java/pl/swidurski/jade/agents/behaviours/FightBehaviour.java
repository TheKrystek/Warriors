package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.WarriorAgent;
import pl.swidurski.jade.gui.FighterAgent;

/**
 * Created by Krystek on 2016-07-20.
 */
public class FightBehaviour extends Behaviour {
    private final FighterAgent agent;
    private final MessageTemplate mt = MessageTemplate.MatchConversationId(Const.ATTACK);

    public FightBehaviour(FighterAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                int dmg = Integer.parseInt(reply.getContent());
                agent.getInternalState().addHp(-dmg);
                agent.update();
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
