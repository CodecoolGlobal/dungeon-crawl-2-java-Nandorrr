package com.codecool.dungeoncrawl.logic.items.general;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Chest extends Item {

    public Chest(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "chest";
    }
}