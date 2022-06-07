package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.util.RandomChoose;

import java.util.List;

public class Ghost extends Actor implements Enemy{

    private static final ActorStats STATS = new ActorStats(50, 30, 0);

    public Ghost(Cell cell) {
        super(cell);
        this.health = STATS.health;
        this.armor = STATS.baseArmor;
        this.damage = STATS.damage;
    }

    @Override
    public String getTileName() {
        return "ghost";
    }

    @Override
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
