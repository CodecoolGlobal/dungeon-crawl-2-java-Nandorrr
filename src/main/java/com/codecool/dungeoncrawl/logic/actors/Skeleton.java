package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;
import java.util.Random;

public class Skeleton extends Actor implements Enemy{
    public Skeleton(Cell cell) {
        super(cell);
        this.health = ActorStats.SKELETON.health;
        this.damage = ActorStats.SKELETON.damage;
        this.armor = ActorStats.SKELETON.baseArmor;
    }

    @Override
    public void move(int dx, int dy) {

    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    @Override
    public void hitPlayer() {
        List<Cell>  surroundingCells = super.getSurroundingCells();

        for(Cell cell : surroundingCells){
            if (cell.getActor() != null){
                int hitDamage = ActorStats.SKELETON.damage;
                cell.getActor().getHurt(hitDamage);
            }
        }
    }

}
