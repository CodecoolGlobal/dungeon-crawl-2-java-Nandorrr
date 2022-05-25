package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
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

    @Override
    public void move(int dx, int dy) {
        if (isAlive()) {
            Cell nextCell = cell.getNeighbor(dx, dy);
            CellType nextCellType = nextCell.getType();
            if ((nextCellType == CellType.FLOOR && nextCell.getActor() == null)
                    || nextCellType == CellType.OPEN_DOOR
                    || (nextCellType == CellType.CLOSED_DOOR && hasKey)) {
                if (nextCellType == CellType.CLOSED_DOOR) {
                    nextCell.setType(CellType.OPEN_DOOR);
                    removeFromInventory("key");
                    removeKey();
                }
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
        }
    }

    @Override
    public void executeBehaviour() {
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
            str.append(": " + itemCount);
            str.append("\n");
        }

        return str.toString();
    }

    private int countItemInInventory(String itemName) {
        return (int) inventory.stream().filter(item -> item.getTileName().equalsIgnoreCase(itemName)).count();
    }

    public void pickUpItem() {
        if (isAlive()) {
            List<Cell> surroundingCells = getSurroundingCells();

            for (Cell neighbor: surroundingCells) {
                Item item = neighbor.getItem();
                if (item != null) {
                    if (item instanceof Key) {
                        hasKey = true;
                        inventory.add(item);
                    } else if (item instanceof HealthPotion) {
                        if (this.health <= 80) {
                            increaseHealth(((HealthPotion) item).getHealingValue());
                        } else if (this.health >= 100) {
                            inventory.add(item);
                        } else {
                            this.health = 100;
                        }
                    } else if (item instanceof ChestPlate) {
                        increaseArmor(((ChestPlate) item).increaseArmor());
                        inventory.add(item);
                    } else if (item instanceof Sword) {
                        increaseDamage(((Sword) item).getDamage());
                        inventory.add(item);
                    } else {
                        inventory.add(item);
                    }
                    neighbor.setItem(null);
                }
            }
        }
    }

    @Override
    public void hitActor() {
        if (isAlive()) {
            List<Cell>  surroundingCells = super.getSurroundingCells();

            for(Cell cell : surroundingCells){
                Actor enemy = cell.getActor();
                if (enemy instanceof Enemy){
                    enemy.getHurt(this.damage);
                }
            }
        }
    }

    private void increaseHealth(int extraHealth) {
        this.health += extraHealth;
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

    private void removeKey() {
        this.hasKey = false;
    }

    public String getTileName() {
        return "player";
    }
}
