package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.AgentMode;
import pl.swidurski.jade.agents.MonsterAgent;
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.MapState;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Krystek on 2016-07-17.
 */
public class MonsterBehaviour extends TickerBehaviour {
    private MonsterAgent agent;
    private List<MapState> state;

    public MonsterBehaviour(MonsterAgent agent) {
        super(agent, 1000);
        this.agent = agent;
    }

    @Override
    protected void onTick() {
        state = agent.getMapState();
        if (state == null || state.isEmpty() || agent.getMode() == AgentMode.DIE)
            return;

        die();
        regenerateHP();
        hasToFight();

        switch (agent.getMode()) {
            case WAIT:
                break;
            case FIGHT:
                fightWarrior();
                break;
        }

        inform();
        if (agent.getMode() == AgentMode.DIE) {
            this.stop();
        }
    }


    private void fightWarrior() {
        List<MapState> states = state.parallelStream().filter(p -> p.getX() == agent.getInternalState().getPosX() &&
                p.getY() == agent.getInternalState().getPosY()).collect(Collectors.toList());

        boolean fight = false;
        for (MapState mapState : states) {
            if (mapState.getType() == ElementType.WARRIOR && !mapState.getAgent().equals(agent.getLocalName())) {
                attack(mapState.getAgent());
                fight = true;
            }
        }
        if (!fight)
            agent.setMode(AgentMode.WAIT);
    }

    private void hasToFight() {
        if (agent.getMode() == AgentMode.DIE)
            return;
        List<MapState> states = state.parallelStream().filter(p -> p.getX() == agent.getInternalState().getPosX() &&
                p.getY() == agent.getInternalState().getPosY()).collect(Collectors.toList());
        for (MapState mapState : states) {
            if (mapState.getType() == ElementType.WARRIOR && !mapState.getAgent().equals(agent.getLocalName())) {
                agent.setMode(AgentMode.FIGHT);
            }
        }
    }

    private void die() {
        int hp = agent.getInternalState().getHp();
        if (hp > 0)
            return;
        agent.setMode(AgentMode.DIE);
    }

    private void regenerateHP() {
        int hp = agent.getInternalState().getHp();
        int maxhp = agent.getInternalState().getMaxHp();
        if (hp < maxhp)
            hp += 3;
        agent.getInternalState().setHp(hp);
        agent.update();
    }

    public void inform() {
        agent.addBehaviour(new InformAboutLocationBehaviour(agent));
        agent.update();
    }

    public void attack(String warrior) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent.getMapAgent());
        msg.setConversationId(Const.ATTACK);
        msg.setContent(warrior);
        agent.send(msg);
    }
}
