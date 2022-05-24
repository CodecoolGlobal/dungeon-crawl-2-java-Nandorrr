package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.armors.ChestPlate;
import com.codecool.dungeoncrawl.logic.items.general.Key;
import com.codecool.dungeoncrawl.logic.items.potions.HealthPotion;
import com.codecool.dungeoncrawl.logic.items.weapons.Sword;

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
        List<String> inventoryItemNames = new ArrayList<>();

        for (Item item: this.inventory) {
            String itemName = item.getTileName();
            inventoryItemNames.add(itemName);
        }

        Set<String> itemNamesNoDuplicate = new HashSet<>(inventoryItemNames);

        for (String name: itemNamesNoDuplicate) {
            int itemCount = countItemInInventory(name);
            str.append("  - " + name);
            str.append(": " + itemCount + " piece(s)");
            str.append("\n");
        }

        return str.toString();
    }

    private int countItemInInventory(String itemName) {
        return (int) inventory.stream().filter(item -> item.getTileName().equalsIgnoreCase(itemName)).count();
    }

    public void pickUpItem() {
        List<Cell> surroundingCells = getSurroundingCells();

        for (Cell neighbor: surroundingCells) {
            Item item = neighbor.getItem();
            if (item != null) {
                if (item instanceof Key) {
                    hasKey = true;
                    inventory.add(item);
                } else if (item instanceof HealthPotion) {
                    increaseHealth(((HealthPotion) item).getHealingValue());
                    inventory.add(item);
                } else if (item instanceof ChestPlate) {
                    increaseArmor(((ChestPlate) item).increaseArmor());
                    inventory.add(item);
                } else if (item instanceof Sword) {
                    increaseDamage(((Sword) item).getDamage());
                    inventory.add(item);
                } else {
                    inventory.add(item);
                }
            }
        }

    }

    private void increaseHealth(int extraHealth) {
        if (this.health <= 80) {
            this.health += extraHealth;
        } else {
            this.health = 100;
        }
    }

    private void increaseDamage(int extraDamage) {
        this.damage += extraDamage;
    }

    private void increaseArmor(int extraArmor) {
        this.armor += extraArmor;
    }

    private void removeFromInventory(String itemName) {
        inventory.removeIf(item -> item.getTileName().equalsIgnoreCase(itemName));
    }

    public String getTileName() {
        return "player";
    }
}
