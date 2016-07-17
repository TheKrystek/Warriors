package pl.swidurski.jade.agents.behaviours;

import jade.core.behaviours.TickerBehaviour;
import pl.swidurski.jade.agents.MapAgent;

/**
 * Created by Krystek on 2016-07-16.
 * Agent mapy wykonuję tą akcję tak długo aż użytkownik nie wybierze z listy rozwijanej mapy, którą chce załadować.
 */
public class GetMapModelBehaviour extends TickerBehaviour {
    private final MapAgent mapAgent;

    public GetMapModelBehaviour(MapAgent mapAgent) {
        this(mapAgent, 2000);
    }

    public GetMapModelBehaviour(MapAgent mapAgent, long period) {
        super(mapAgent, period);
        this.mapAgent = mapAgent;
    }

    @Override
    protected void onTick() {
        if (mapAgent.getGui().getMapModel() == null)
            return;
        System.out.println("GetMapModelBehaviour() - map has been choosed!");
        mapAgent.setModel(mapAgent.getGui().getMapModel());
        stop();
    }

}
