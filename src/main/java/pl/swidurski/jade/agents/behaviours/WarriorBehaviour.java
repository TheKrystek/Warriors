package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.AgentMode;
import pl.swidurski.jade.agents.WarriorAgent;
import pl.swidurski.jade.model.ElementType;
import pl.swidurski.jade.model.MapState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Krystek on 2016-07-17.
 */
public class WarriorBehaviour extends TickerBehaviour {
    private WarriorAgent agent;
    private Random random = new Random();
    private List<MapState> state;

    public WarriorBehaviour(WarriorAgent agent) {
        super(agent, (61 - agent.getInternalState().getSpeed()) * 1000);
        this.agent = agent;
    }


    private double chanceToWin;


    @Override
    protected void onTick() {
        state = agent.getMapState();
        if (state == null || state.isEmpty() || agent.getMode() == AgentMode.DIE)
            return;

        die();
        regenerateHP();
        chanceToWin = calculateChancesToWin();

        System.out.printf("%s: %s [%.2f]\r\n", agent.getLocalName().toUpperCase(), agent.getMode(), chanceToWin);
        if (agent.getMode() != AgentMode.DIE)
            makeDecision();


        switch (agent.getMode()) {
            case LOOK_FOR_TREASURE:
                lookForTreasure();
                break;
            case PICKUP_TREASURE:
                pickupTreasure();
                break;
            case FIGHT:
                fight();
                break;
            case LOOK_FOR_POTION:
                lookForPotion();
                break;
            case PICKUP_POTION:
                pickupPotion();
                break;
            case FLEE:
                flee();
                break;
        }

        informAboutLocation();
        if (agent.getMode() == AgentMode.DIE) {
            this.stop();
        }
    }

    private void makeDecision() {
        if (chanceToWin >= 0.2 && chanceToWin < 0.4 && (agent.getMode() != AgentMode.LOOK_FOR_POTION || agent.getMode() != AgentMode.PICKUP_POTION)) {
            agent.setMode(AgentMode.FLEE);
        } else if (chanceToWin < 0.2 && agent.getMode() != AgentMode.PICKUP_POTION) {
            agent.setMode(AgentMode.LOOK_FOR_POTION);
        } else {
            hasToFight();
        }
    }

    private void flee() {
        List<MapState> safeStates = new ArrayList<>();
        if (agent.getVisited().isEmpty()) {
            safeStates.addAll(state.stream().filter(s -> s.getType() != ElementType.WARRIOR && s.getType() != ElementType.MONSTER).collect(Collectors.toList()));

        } else {
            addCurrentState(state);

            for (MapState s : state) {
                for (MapState v : agent.getVisited()) {
                    if (s.getX() == v.getX() && s.getY() == s.getY() && s.getType() != ElementType.WARRIOR && s.getType() != ElementType.MONSTER) {
                        safeStates.add(s);
                        break;
                    }
                }
            }
        }
        if (!safeStates.isEmpty())
            move(safeStates.get(getRandom(safeStates.size())));
    }

    private void fight() {
        List<MapState> states = state.parallelStream().filter(p -> p.getX() == agent.getInternalState().getPosX() &&
                p.getY() == agent.getInternalState().getPosY()).collect(Collectors.toList());

        boolean fight = false;
        for (MapState mapState : states) {
            if ((mapState.getType() == ElementType.WARRIOR || mapState.getType() == ElementType.MONSTER) && !mapState.getAgent().equals(agent.getLocalName())) {
                attack(mapState.getAgent());
                fight = true;
            }
        }
        if (!fight) {
            if (chanceToWin > 0.2)
                agent.setMode(AgentMode.LOOK_FOR_TREASURE);
            else
                agent.setMode(AgentMode.LOOK_FOR_POTION);
        }
    }

    private void hasToFight() {
        if (agent.getMode() == AgentMode.DIE)
            return;
        List<MapState> states = state.parallelStream().filter(p -> p.getX() == agent.getInternalState().getPosX() &&
                p.getY() == agent.getInternalState().getPosY()).collect(Collectors.toList());
        for (MapState mapState : states) {
            if ((mapState.getType() == ElementType.WARRIOR || mapState.getType() == ElementType.MONSTER) && !mapState.getAgent().equals(agent.getLocalName())) {
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
            hp++;
        agent.getInternalState().setHp(hp);
        agent.update();
    }

    private void pickupTreasure() {
        agent.addBehaviour(new PickupBehaviour(agent, ElementType.TREASURE));
        agent.setMode(AgentMode.LOOK_FOR_TREASURE);
    }

    private void pickupPotion() {
        agent.addBehaviour(new PickupBehaviour(agent, ElementType.POTION));
        agent.setMode(AgentMode.LOOK_FOR_TREASURE);
    }

    private void lookForPotion() {
        addCurrentState(state);
        List<MapState> newStates = getAvailablePositions(state);

        Optional<MapState> potion = getFirstByType(state, ElementType.POTION);
        if (potion.isPresent()) {
            agent.setMode(AgentMode.PICKUP_POTION);
            move(potion.get());
            Optional<MapState> monster = getFirstByType(state, ElementType.MONSTER);
            Optional<MapState> warrior = getFirstByType(state, ElementType.WARRIOR);
            if (monster.isPresent() || warrior.isPresent())
                return;
            pickupPotion();
            return;
        }

        if (newStates.isEmpty()) {
            agent.getVisited().clear();
            addCurrentState(state);
            newStates = getAvailablePositions(state);
        }

        if (newStates.size() > 0) {
            move(newStates.get(getRandom(newStates.size())));
        }
    }

    private void lookForTreasure() {
        addCurrentState(state);
        List<MapState> newStates = getAvailablePositions(state);

        Optional<MapState> treasure = getFirstByType(state, ElementType.TREASURE);
        if (treasure.isPresent()) {
            agent.setMode(AgentMode.PICKUP_TREASURE);
            move(treasure.get());
            pickupTreasure();
            return;
        }

        if (newStates.size() == 0) {
            agent.getVisited().clear();
            addCurrentState(state);
            newStates = getAvailablePositions(state);
        }

        if (newStates.size() > 0) {
            move(newStates.get(getRandom(newStates.size())));
        }
    }

    private List<MapState> getAvailablePositions(List<MapState> state) {
        return state.stream().filter(mapState -> mapState.getType() != ElementType.MONSTER && agent.getVisited().parallelStream().filter(p -> p.getX() == mapState.getX() && p.getY() == mapState.getY()).count() == 0).collect(Collectors.toList());
    }

    private Optional<MapState> getFirstByType(List<MapState> state, ElementType type) {
        return state.parallelStream().filter(p -> p.getType() == type).findFirst();
    }

    private Optional<MapState> addCurrentState(List<MapState> state) {
        Optional<MapState> currState = state.parallelStream().filter(p -> p.getX() == agent.getInternalState().getPosX() &&
                p.getY() == agent.getInternalState().getPosY()).findFirst();

        if (currState.isPresent())
            agent.getVisited().add(currState.get());
        return currState;
    }

    private int getRandom(int max) {
        return Math.abs(random.nextInt()) % max;
    }

    private void move(MapState state) {
        agent.getInternalState().setPosX(state.getX());
        agent.getInternalState().setPosY(state.getY());

    }

    public void informAboutLocation() {
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


    public double calculateChancesToWin() {
        return agent.getInternalState().getHp() / (double) agent.getInternalState().getMaxHp();
    }
}
