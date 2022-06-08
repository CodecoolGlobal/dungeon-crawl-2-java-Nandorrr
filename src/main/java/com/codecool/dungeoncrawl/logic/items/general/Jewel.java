package com.codecool.dungeoncrawl.logic.items.general;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Jewel extends Item {

    public Jewel(Cell cell) {
        super(cell);
    }

    public Jewel() {
        super();
    }

    @Override
    public String getTileName() {
        return "jewel";
    }
}