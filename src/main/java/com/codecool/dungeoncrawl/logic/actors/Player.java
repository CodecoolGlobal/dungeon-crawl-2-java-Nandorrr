package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;
import java.util.List;

public class Player extends Actor {

    private int health;

    private int damage;

    private int armor;

    private List<Item> inventory;

    public Player(Cell cell) {
        super(cell);
        this.health = ActorStats.PLAYER.health;
        this.damage = ActorStats.PLAYER.damage;
        this.armor = ActorStats.PLAYER.baseArmor;
        inventory = new ArrayList<>();
    }

    private int countItemInInventory(String itemName) {
        int itemCount = (int) inventory.stream().filter(item -> item.getTileName().equalsIgnoreCase(itemName)).count();
        return itemCount;
    }

    private void removeFromInventory(String itemName) {
        inventory.removeIf(item -> item.getTileName().equalsIgnoreCase(itemName));
    }

    public String getTileName() {
        return "player";
    }
}
