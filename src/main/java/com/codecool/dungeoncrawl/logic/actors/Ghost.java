package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.util.Directions;
import com.codecool.dungeoncrawl.logic.util.RandomChoose;

import java.util.List;

public class Ghost extends Actor implements Enemy{

    public Ghost(Cell cell) {
        super(cell);
        this.health = ActorStats.GHOST.health;
        this.armor = ActorStats.GHOST.baseArmor;
        this.damage = ActorStats.GHOST.damage;
    }

    @Override
    public String getTileName() {
        return "ghost";
    }

    @Override
    public boolean isPlayerAround(){
        List<Cell> surroundingCells = super.getSurroundingCells();

        for(Cell cell : surroundingCells){
            Actor otherActor = cell.getActor();
            if (otherActor instanceof Player){ return true;}
        }
        return false;
    }

    public void executeBehaviour() {
        if (this.isAlive()){
            if (isPlayerAround()){
                hitActor();
            } else {
                RandomChoose randomChoose = new RandomChoose();
                List<Cell> availableCells = this.getCell().getFloorTiles();

                Cell randomCell = randomChoose.randomChooseFromAList(availableCells);
                move(randomCell.getX(), randomCell.getY());
            }
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
    public void move(int dx, int dy) {
        GameMap gameMap = cell.getGameMap();
        Cell target = gameMap.getCell(dx, dy);
        CellType targetType = target.getType();

        if (targetType == CellType.FLOOR && target.getActor()==null
                || targetType == CellType.OPEN_DOOR && target.getActor()==null){
            cell.setActor(null);
            target.setActor(this);
            cell = target;
        }

    }
}
