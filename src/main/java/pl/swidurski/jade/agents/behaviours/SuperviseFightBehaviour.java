package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MapAgent;
import pl.swidurski.jade.model.State;

import java.util.Random;

/**
 * Created by Krystek on 2016-07-20.
 */
public class SuperviseFightBehaviour extends Behaviour {
    private final MapAgent agent;
    private final MessageTemplate mt = MessageTemplate.MatchConversationId(Const.ATTACK);
    private final Random random = new Random();

    public SuperviseFightBehaviour(MapAgent agent) {
        this.agent = agent;
    }


    @Override
    public void action() {
        ACLMessage reply = agent.receive(mt);
        if (reply != null) {
            if (reply.getPerformative() == ACLMessage.INFORM) {
                State attacker = agent.getStates().get(reply.getSender().getLocalName());
                State receiver = agent.getStates().get(reply.getContent());
                int dmg = attacker.getDamage();
                if (getRand() > 40)
                    dmg /= 2;

                String rec = receiver.getAgent();
                System.out.printf("%s has attacked %s [%sdmg]\r\n", attacker.getAgent(), rec, dmg);
                send(attacker.getAgent(), rec, dmg);
            }
        } else {
            block();
        }
    }

    private void move(State attacker, State receiver) {
        if (attacker.getPosX() == receiver.getPosX() && attacker.getPosY() == receiver.getPosY())
            return;
        receiver.setPosX(attacker.getPosX());
        receiver.setPosY(attacker.getPosY());

        agent.getStates().put(receiver.getAgent(), receiver);
        agent.move(receiver.getAgent());
    }

    @Override
    public boolean done() {
        return false;
    }

    public void send(String sender, String receiver, int dmg) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getAIDByName(receiver));
        msg.setConversationId(Const.ATTACK);
        msg.setReplyWith(sender);
        msg.setContent(String.valueOf(dmg));
        agent.send(msg);
    }


    public int getRand() {
        int rand = Math.abs(random.nextInt());
        return rand % 100;
    }
}
