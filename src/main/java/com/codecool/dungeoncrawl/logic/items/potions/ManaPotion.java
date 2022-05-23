package com.codecool.dungeoncrawl.logic.items.potions;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class ManaPotion extends Item {

    public ManaPotion(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "mana";
    }
}