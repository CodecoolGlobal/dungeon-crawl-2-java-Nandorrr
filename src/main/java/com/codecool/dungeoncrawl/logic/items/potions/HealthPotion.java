package com.codecool.dungeoncrawl.logic.items.potions;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class HealthPotion extends Item {

    private int healingValue = 20;

    public HealthPotion(Cell cell) {
        super(cell);
    }

    public int getHealingValue() {
        return this.healingValue;
    }

    @Override
    public String getTileName() {
        return "health potion";
    }
}
