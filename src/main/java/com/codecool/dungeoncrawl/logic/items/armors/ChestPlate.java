package com.codecool.dungeoncrawl.logic.items.armors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class ChestPlate extends Item implements Armor {

    public ChestPlate(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "chestplate";
    }

    @Override
    public void increaseArmor() {

    }
}
