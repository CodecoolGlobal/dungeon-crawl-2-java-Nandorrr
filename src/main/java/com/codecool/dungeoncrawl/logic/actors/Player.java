package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.general.Key;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player extends Actor {

    private List<Item> inventory;

    public Player(Cell cell) {
        super(cell);
        this.health = ActorStats.PLAYER.health;
        this.damage = ActorStats.PLAYER.damage;
        this.armor = ActorStats.PLAYER.baseArmor;
        inventory = new ArrayList<>();
    }

    public List<Item> getInventory() {
        return this.inventory;
    }

    public String getInventoryContentText() {
        StringBuilder str = new StringBuilder();
        Set<Item> inventoryItems = new HashSet<>(this.inventory);

        for (Item item: inventoryItems) {
            String itemName = item.getClass().getSimpleName();
            int itemCount = countItemInInventory(itemName);
            str.append(itemName);
            str.append(": " + itemCount + " piece(s)");
            str.append("\n");
        }
        return str.toString();
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
