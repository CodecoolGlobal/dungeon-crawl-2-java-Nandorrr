package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.util.Directions;

import java.util.ArrayList;
import java.util.List;

public class Ogre extends Actor implements Enemy {

    int moveCount = 0;

    int goalCount = 0;
    private int[][] patrolCoords;

    public Ogre(Cell cell) {
        super(cell);
        this.health = ActorStats.OGRE.health;
        this.damage = ActorStats.OGRE.damage;
        this.armor = ActorStats.OGRE.baseArmor;
        this.patrolCoords = patrolCoords;
    }

    @Override
    public String getTileName() {
        return "ogre";
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        CellType nextCellType = nextCell.getType();
        if ((nextCellType == CellType.FLOOR && nextCell.getActor() == null)
                || nextCellType == CellType.OPEN_DOOR
                || (nextCellType == CellType.CLOSED_DOOR && hasKey)) {
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;}

    }

    @Override
    public void hitActor() {

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
    public void executeBehaviour() {
        if (this.isAlive()){
            if (isPlayerAround()){
                hitActor();
            } else {
                int xCoord = cell.getNeighbor(cell.getX(), cell.getY()).getX();
                int yCoord = cell.getNeighbor(cell.getX(), cell.getY()).getY();
                move(xCoord, yCoord);
            }
        }
    }





}
