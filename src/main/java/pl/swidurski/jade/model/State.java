package pl.swidurski.jade.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Krystek on 2016-07-17.
 */
public class State {
    @Getter
    @Setter
    int damage, maxHp, speed, hp, posX, posY, points;

    @Getter
    @Setter
    String agent;

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                getAgent(),
                getPosX(),
                getPosY(),
                getHp(),
                getMaxHp(),
                getDamage(),
                getSpeed(),
                getPoints());
    }


    public static State loadFromString(String string) {
        String[] s = string.split(";");
        State state = new State();
        state.setAgent(s[0]);
        state.setPosX(Integer.parseInt(s[1]));
        state.setPosY(Integer.parseInt(s[2]));
        state.setHp(Integer.parseInt(s[3]));
        state.setMaxHp(Integer.parseInt(s[4]));
        state.setDamage(Integer.parseInt(s[5]));
        state.setSpeed(Integer.parseInt(s[6]));
        state.setPoints(Integer.parseInt(s[7]));
        return state;
    }

    public void addHp(int i) {
        hp += i;
        if (hp > maxHp)
            hp = maxHp;
    }
}
