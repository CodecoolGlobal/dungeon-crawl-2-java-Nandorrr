package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable {

    private final Cell cell;

    public Item() {
        this.cell = null;
    }

    public Item(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
    }

    public Cell getCell() {
        return cell;
    }

    public int getValue() {
        return 0;
    }

}
