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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Krystek on 2016-07-16.
 */
public class FindWarriorsBehaviour extends TickerBehaviour {
    private final MapAgent mapAgent;
    private final DFAgentDescription template = new DFAgentDescription();


    public FindWarriorsBehaviour(MapAgent mapAgent) {
        this(mapAgent, 1000);
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
            DFAgentDescription[] warriors = DFService.search(mapAgent, getTemplate(Const.WARRIOR_TYPE));
            DFAgentDescription[] monsters = DFService.search(mapAgent, getTemplate(Const.MONSTER_TYPE));

            List<DFAgentDescription> results = new ArrayList<>();
            results.addAll(Arrays.asList(warriors));
            results.addAll(Arrays.asList(monsters));

            for (DFAgentDescription result : results) {
                AID agent = result.getName();
                if (mapAgent.getWarriors().contains(agent))
                    continue;
                mapAgent.getWarriors().add(agent);
                sayHello(agent);
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private ServiceDescription getService(String type) {
        ServiceDescription service = new ServiceDescription();
        service.setType(type);
        return service;
    }

    private DFAgentDescription getTemplate(String type){
        DFAgentDescription template = new DFAgentDescription();
        template.addServices(getService(type));
        return template;
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
