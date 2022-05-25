package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.general.Key;
import com.codecool.dungeoncrawl.logic.util.Directions;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class Skeleton extends Actor implements Enemy {
    public Skeleton(Cell cell) {
        super(cell);
        this.health = ActorStats.SKELETON.health;
        this.damage = ActorStats.SKELETON.damage;
        this.armor = ActorStats.SKELETON.baseArmor;
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        CellType nextCellType = nextCell.getType();

        if (nextCellType == CellType.FLOOR && nextCell.getActor()==null
                || nextCellType == CellType.OPEN_DOOR && nextCell.getActor()==null){
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }

    }

    @Override
    public String getTileName() {
        return "skeleton";
    }


    private boolean isPlayerAround(){
        List<Cell>  surroundingCells = super.getSurroundingCells();

        for(Cell cell : surroundingCells){
            Actor otherActor = cell.getActor();
            if (otherActor instanceof Player){ return true;}
        }
        return false;
    }


    @Override
    public void hitActor() {
        List<Cell>  surroundingCells = super.getSurroundingCells();

        for(Cell cell : surroundingCells){
            Actor otherActor = cell.getActor();
            if (otherActor instanceof Player){
                otherActor.getHurt(this.damage);
            }
        }
    }

    public void executeBehaviour() {
        if (this.isAlive()){
            if (isPlayerAround()){
                hitActor();
            } else {
                Directions directionToMove = getRandomStepDirection();
                move(directionToMove.dx, directionToMove.dy);
            }
        }
    }


}
