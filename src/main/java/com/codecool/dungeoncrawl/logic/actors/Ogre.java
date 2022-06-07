package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.util.Directions;

import java.util.List;

public class Ogre extends Actor implements Enemy {

    private static final ActorStats STATS = new ActorStats(200, 30, 50);
    private Directions previousMove;

    public Ogre(Cell cell) {
        super(cell);
        this.health = STATS.health;
        this.damage = STATS.damage;
        this.armor = STATS.baseArmor;
    }

    @Override
    public String getTileName() {
        return "ogre";
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        CellType nextCellType = nextCell.getType();
        if (nextCellType == CellType.FLOOR && nextCell.getActor() == null){
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;}

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
        Directions left = Directions.LEFT;
        Directions right = Directions.RIGHT;
        if (this.isAlive()){
            if (isPlayerAround()){
                hitActor();
            } else {
                if (previousMove == null) {
                    if (cell.getNeighbor(left.dx, left.dy).getType() == CellType.FLOOR) {
                        previousMove = left;
                        move(left.dx, left.dy);
                    } else if (cell.getNeighbor(right.dx, right.dy).getType() == CellType.FLOOR) {
                        move(right.dx, right.dy);
                        previousMove = right;
                    }
                } else {
                    if (cell.getNeighbor(previousMove.dx, previousMove.dy).getType() == CellType.FLOOR){
                        move(previousMove.dx, previousMove.dy);
                    } else {
                        if (previousMove == left){
                            previousMove = right;
                            move(right.dx, right.dy);
                        } else if (previousMove == right) {
                            previousMove = left;
                            move(left.dx, left.dy);
                        }
                    }
                }
            }
        }
    }
}






