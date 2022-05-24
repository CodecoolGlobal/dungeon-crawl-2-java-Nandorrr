package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;
import java.util.Random;

public class Skeleton extends Actor{
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
        public void hitActor() {
            List<Cell>  surroundingCells = super.getSurroundingCells();

            for(Cell cell : surroundingCells){
                Actor otherActor = cell.getActor();
                if (otherActor instanceof Player){
                    int hitDamage = ActorStats.SKELETON.damage;
                    otherActor.getHurt(hitDamage);
                }
            }
        }

}
