package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.util.Directions;

import java.util.List;

public class Scorpion extends Actor implements Enemy {

    private static final ActorStats STATS = new ActorStats(80, 20, 20);

    public Scorpion(Cell cell) {
        super(cell);
        this.health = STATS.health;
        this.damage = STATS.damage;
        this.armor = STATS.baseArmor;
    }

    @Override
    public String getTileName() {
        return "scorpion";
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
    public void hitActor() {
        List<Cell>  surroundingCells = super.getSurroundingCells();

        for(Cell cell : surroundingCells){
            Actor otherActor = cell.getActor();
            if (otherActor instanceof Player){
                otherActor.getHurt(this.damage);
            }
        }
    }

    @Override
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
