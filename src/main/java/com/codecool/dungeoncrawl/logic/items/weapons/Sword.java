package com.codecool.dungeoncrawl.logic.items.weapons;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Sword extends Item implements Weapon {

    private int damage = 20;

    public Sword(Cell cell) {
        super(cell);
    }

    public int getDamage() {
        return this.damage;
    }

    @Override
    public String getTileName() {
        return "sword";
    }
}
