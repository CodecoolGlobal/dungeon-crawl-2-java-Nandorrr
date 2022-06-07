package com.codecool.dungeoncrawl.logic.items.general;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Coin extends Item {

    public Coin(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "coin";
    }
}
