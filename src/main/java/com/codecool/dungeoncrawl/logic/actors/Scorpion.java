package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public class Scorpion extends Actor {

    public Scorpion(Cell cell) {
        super(cell);
        this.health = ActorStats.SCORPION.health;
        this.damage = ActorStats.SCORPION.damage;
        this.armor = ActorStats.SCORPION.baseArmor;
    }

    @Override
    public String getTileName() {
        return "scorpion";
    }

    @Override
    public void move(int dx, int dy) {

    }
}
