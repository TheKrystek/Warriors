package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.agents.MonsterAgent;
import pl.swidurski.jade.agents.WarriorAgent;
import pl.swidurski.jade.agents.FighterAgent;

/**
 * Created by Krystek on 2016-07-16.
 */
public class WaitForHelloBehaviour extends Behaviour {
    private final FighterAgent agent;
    private MessageTemplate mt = MessageTemplate.MatchConversationId("hello");
    private boolean finish = false;

    public WaitForHelloBehaviour(FighterAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                agent.setMapAgent(reply.getSender());
                assignBehaviour();
            }
            finish = true;
        } else {
            block();
        }
    }

    private void assignBehaviour() {
        agent.addBehaviour(new InformAboutLocationBehaviour(agent));

        if (agent instanceof MonsterAgent)
            agent.addBehaviour(new MonsterBehaviour((MonsterAgent) agent));

        if (agent instanceof WarriorAgent)
            agent.addBehaviour(new WarriorBehaviour((WarriorAgent) agent));
    }

    @Override
    public boolean done() {
        return finish;
    }
}
