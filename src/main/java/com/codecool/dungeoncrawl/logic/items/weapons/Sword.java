package com.codecool.dungeoncrawl.logic.items.weapons;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Sword extends Item implements Weapon {

    public final static int DAMAGE_VALUE = 20;

    public Sword(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "sword";
    }
}
