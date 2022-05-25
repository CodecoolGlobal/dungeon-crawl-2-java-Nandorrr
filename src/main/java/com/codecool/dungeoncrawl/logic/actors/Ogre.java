package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public class Ogre extends Actor implements Enemy {

    public Ogre(Cell cell) {
        super(cell);
        this.health = ActorStats.OGRE.health;
        this.damage = ActorStats.OGRE.damage;
        this.armor = ActorStats.OGRE.baseArmor;
    }

    @Override
    public String getTileName() {
        return "ogre";
    }

    @Override
    public void move(int dx, int dy) {

    }

    @Override
    public void hitActor() {

    }

    @Override
    public void executeBehaviour() {

    }
}
