package com.codecool.dungeoncrawl.logic.items.potions;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class HealthPotion extends Item {

    public final static int HEALING_VALUE = 20;

    public HealthPotion(Cell cell) {
        super(cell);
    }

    public HealthPotion() {
        super();
    }

    @Override
    public int getValue() {
        return HEALING_VALUE;
    }

    @Override
    public String getTileName() {
        return "health potion";
    }
}
