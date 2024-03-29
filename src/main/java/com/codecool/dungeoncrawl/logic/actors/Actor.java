package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.util.Directions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Actor implements Drawable {

    protected Cell cell;
    protected int health;
    protected int damage;
    protected int armor;

    protected Boolean hasKey = false;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public abstract void move(int dx, int dy);

    public abstract void executeBehaviour();

    public abstract void hitActor();

    public boolean isPlayerAround(){
        List<Cell>  surroundingCells = getSurroundingCells();

        for(Cell cell : surroundingCells){
            Actor otherActor = cell.getActor();
            if (otherActor instanceof Player){ return true;}
        }
        return false;
    }

    protected List<Cell> getSurroundingCells() {
        List<Cell> surroundingCells = new ArrayList<>();

        surroundingCells.add(cell.getNeighbor(0, -1)); // upper neighbor
        surroundingCells.add(cell.getNeighbor(-1, -1)); // upper left neighbor
        surroundingCells.add(cell.getNeighbor(1, -1)); // upper right neighbor
        surroundingCells.add(cell.getNeighbor(0, 1)); // bottom neighbor
        surroundingCells.add(cell.getNeighbor(-1, 1)); // bottom left neighbor
        surroundingCells.add(cell.getNeighbor(1, 1)); // bottom right neighbor
        surroundingCells.add(cell.getNeighbor(-1, 0)); // left neighbor
        surroundingCells.add(cell.getNeighbor(1, 0)); // right neighbor
        surroundingCells.add(cell.getNeighbor(0, 0)); // standing on it

        return surroundingCells;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive(){
        return getHealth() > 0;
    }

    public int getDamage() {
        return damage;
    }

    protected void getHurt(int damage){
        if (isAlive()) {
            this.health -= damage;
        }
        else {
            cell.setActor(null);
            cell.getGameMap().removeEnemyFromArmy(this);
        }
    }

    public int getArmor() {
        return armor;
    }

    protected void setHealth(int newHealth) {
        this.health = Math.max(newHealth, 0);
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    protected void addKey() {
        this.hasKey = true;
    }

    protected Directions getRandomStepDirection (){
        Random rand = new Random();
        int upperBound = 4;
        int directionNum = rand.nextInt(upperBound);

        switch (directionNum){
            case 0:
                return Directions.UP;
            case 1:
                return Directions.DOWN;
            case 2:
                return Directions.RIGHT;
            default:
                return Directions.LEFT;
        }

    }

}
