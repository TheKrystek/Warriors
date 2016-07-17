package pl.swidurski.jade.agents.behaviours;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import pl.swidurski.jade.Const;
import pl.swidurski.jade.agents.MapAgent;

/**
 * Created by Krystek on 2016-07-16.
 */
public class FindWarriorsBehaviour extends TickerBehaviour {
    private final MapAgent mapAgent;
    private final DFAgentDescription template = new DFAgentDescription();
    private final ServiceDescription sd = new ServiceDescription();

    public FindWarriorsBehaviour(MapAgent mapAgent) {
        this(mapAgent, 5000);
    }

    public FindWarriorsBehaviour(MapAgent mapAgent, long period) {
        super(mapAgent, period);
        this.mapAgent = mapAgent;
        configureTemplate();
    }


    private void configureTemplate() {

    }

    @Override
    protected void onTick() {
        try {
            sd.setType(Const.WARRIOR_TYPE);
            template.addServices(sd);
            DFAgentDescription[] results = DFService.search(mapAgent, template);

            for (DFAgentDescription result : results) {
                AID agent = result.getName();
                if (mapAgent.getWarriors().contains(agent))
                    continue;
                System.out.println("FindWarriorsBehaviour() - new agent found: " + agent.getLocalName());
                mapAgent.getWarriors().add(agent);

                // Wyślij powitanie - agent do którego wyślemy powitanie odpowie wiadomością z informacją o własnym stanie
                sayHello(agent);
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public void sayHello(AID agent) {
        String content = getHelloMsg(agent);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(agent);
        msg.setContent(content);
        msg.setConversationId("hello");
        msg.setReplyWith(content);
        System.out.println("SENDING: " + content);
        mapAgent.send(msg);
    }

    private String getHelloMsg(AID agent){
        return String.format("HELLO_%s",agent.getLocalName().toUpperCase());
    }
}
