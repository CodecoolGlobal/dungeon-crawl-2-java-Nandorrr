package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    private Player player;

    private final List<Actor> enemyArmy;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        this.enemyArmy = new ArrayList<>();
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void addToEnemyArmy(Actor enemy){
        enemyArmy.add(enemy);}

    public List<Actor> getEnemyArmy(){return enemyArmy;}

    public void removeEnemyFromArmy(Actor enemy){
        if (!(enemy instanceof Player)){
            enemyArmy.remove(enemy);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public String serializeMap(){
        StringBuilder mapToString = new StringBuilder(String.format("%d %d\n", width, height));
        for (int h=0; h<height;h++){
            for (int w=0; w<width;w++){
                Cell cell= cells[w][h];
                if (cell.getActor() != null){
                    switch (cell.getActor().getTileName()){
                        case "player":
                            mapToString.append("@");
                            break;
                        case "ghost":
                            mapToString.append("g");
                            break;
                        case "ogre":
                            mapToString.append("o");
                            break;
                        case "scorpion":
                            mapToString.append("i");
                            break;
                        case "skeleton":
                            mapToString.append("s");
                            break;
                    }
                } else if (cell.getItem() != null) {
                    switch (cell.getItem().getTileName()){
                        case "chest plate":
                            mapToString.append("p");
                            break;
                        case "chest":
                            mapToString.append("b");
                            break;
                        case "coin":
                            mapToString.append("c");
                            break;
                        case "jewel":
                            mapToString.append("j");
                            break;
                        case "key":
                            mapToString.append("k");
                            break;
                        case "health potion":
                            mapToString.append("h");
                            break;
                        case "mana potion":
                            mapToString.append("m");
                            break;
                        case "sword":
                            mapToString.append("w");
                            break;
                    }
                } else {
                    switch (cell.getType().getTileName()){
                        case "wall":
                            mapToString.append("#");
                            break;
                        case "forest":
                            mapToString.append("%");
                            break;
                        case "grass":
                            mapToString.append("u");
                            break;
                        case "stairs":
                            mapToString.append("*");
                            break;
                        case "closed_door":
                            mapToString.append("d");
                            break;
                        case "floor":
                            mapToString.append(".");
                            break;
                        default:
                            mapToString.append(" ");
                    }
                }

            }
            mapToString.append(System.lineSeparator());
        }
            mapToString.append(System.lineSeparator());

        return mapToString.toString();
    }
}
