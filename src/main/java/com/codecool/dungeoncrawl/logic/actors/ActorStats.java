package com.codecool.dungeoncrawl.logic.actors;

public enum ActorStats {

    PLAYER(100, 10, 20),
    SKELETON(100, 10, 0);

    public int health;
    public int damage;
    public int baseArmor;

    ActorStats(int health, int damage, int baseArmor) {
        this.health = health;
        this.damage = damage;
        this.baseArmor = baseArmor;
    }

}
