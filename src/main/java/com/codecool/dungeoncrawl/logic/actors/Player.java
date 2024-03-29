package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.armors.ChestPlate;
import com.codecool.dungeoncrawl.logic.items.general.Chest;
import com.codecool.dungeoncrawl.logic.items.general.Key;
import com.codecool.dungeoncrawl.logic.items.potions.HealthPotion;
import com.codecool.dungeoncrawl.logic.items.weapons.Sword;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player extends Actor {

    private int id;

    private String name;

    private static final ActorStats STATS = new ActorStats(2000, 10, 20);

    private boolean movingToNextMap = false;

    private List<Item> inventory;

    public Player(Cell cell, String name, int health, int damage, int armor, List<Item> inventory) {
        super(cell);
        this.id = 0;
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.armor = armor;
        this.inventory = inventory;
    }


    public Player(Cell cell, String name) {
        super(cell);
        this.name = name;
        this.health = STATS.health;
        this.damage = STATS.damage;
        this.armor = STATS.baseArmor;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void move(int dx, int dy) {
        if (isAlive()) {
            Cell nextCell = cell.getNeighbor(dx, dy);
            CellType nextCellType = nextCell.getType();
            if ((nextCellType == CellType.FLOOR && nextCell.getActor() == null)
                    || (nextCellType == CellType.GRASS && nextCell.getActor() == null)
                    || nextCellType == CellType.STAIRS
                    || nextCellType == CellType.OPEN_DOOR
                    || (nextCellType == CellType.CLOSED_DOOR && hasKey)) {
                if (nextCellType == CellType.STAIRS) {
                    this.movingToNextMap = true;
                } else {
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
    }

    @Override
    public void executeBehaviour() {
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
            str.append("  - ").append(name);
            str.append(": ").append(itemCount);
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
                        addKey();
                        inventory.add(item);
                    } else if (item instanceof HealthPotion) {
                        if (this.health <= 80) {
                            increaseHealth();
                        } else if (this.health >= 100) {
                            inventory.add(item);
                        } else {
                            this.health = 100;
                        }
                    } else if (item instanceof ChestPlate) {
                        increaseArmor(ChestPlate.PROTECTION_VALUE);
                        inventory.add(item);
                    } else if (item instanceof Sword) {
                        increaseDamage(Sword.DAMAGE_VALUE);
                        inventory.add(item);
                    } else if (item instanceof Chest) {
                         item.getCell().setType(CellType.OPEN_CHEST);
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

    private void increaseHealth() {
        this.health += HealthPotion.HEALING_VALUE;
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

    public void setHasKey() {
        this.hasKey = true;
    }

    private void removeKey() {
        this.hasKey = false;
    }

    public String getTileName() {
        return "player";
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public void setMovingToNextMap(Boolean isMovingToNextMap) {
        this.movingToNextMap = isMovingToNextMap;
    }

    public boolean movingToNextMap() {
        return this.movingToNextMap;
    }

    public void setInventory(List<Item> inventory){
        this.inventory = inventory;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
