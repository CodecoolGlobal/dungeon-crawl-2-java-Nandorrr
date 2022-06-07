package com.codecool.dungeoncrawl.logic.actors;

public class ActorStats {

    public final int health;
    public final int damage;
    public final int baseArmor;

    ActorStats(int health, int damage, int baseArmor) {
        this.health = health;
        this.damage = damage;
        this.baseArmor = baseArmor;
    }

}
