package com.codecool.dungeoncrawl.logic.items.potions;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class ManaPotion extends Item {

    private int manaValue = 20;

    public ManaPotion(Cell cell) {
        super(cell);
    }

    public int getManaValue() {
        return this.manaValue;
    }

    @Override
    public String getTileName() {
        return "mana potion";
    }
}