package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.ActorStats;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class PlayerModel extends BaseModel {
    private final String playerName;
    private int health;
    private int damage;
    private int armor;


    public PlayerModel(String playerName, int health, int damage, int armor) {
        this.playerName = playerName;
        this.health = health;
        this.damage = damage;
        this.armor = armor;

    }


    public PlayerModel(Player player) {
        this.playerName = player.getName();
        this.health = player.getHealth();
        this.damage = player.getDamage();
        this.armor = player.getArmor();




    }

    public String getPlayerName() {
        return playerName;
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }
}
