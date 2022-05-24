package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.List;

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

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        CellType nextCellType = nextCell.getType();
        if ((nextCellType == CellType.FLOOR && nextCell.getActor() == null) || (nextCellType == CellType.DOOR && hasKey)) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public List<Cell> getSurroundingCells() {
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

    public int getDamage() {
        return damage;
    }

    protected void getHurt(int damage){
        this.health -= damage;
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

}
