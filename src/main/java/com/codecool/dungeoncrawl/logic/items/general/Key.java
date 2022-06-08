package com.codecool.dungeoncrawl.logic.items.general;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Key extends Item {

    public Key(Cell cell) {
        super(cell);
    }

    public Key() {
        super();
    }

    @Override
    public String getTileName() {
        return "key";
    }
}
