package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.armors.ChestPlate;
import com.codecool.dungeoncrawl.logic.items.general.Chest;
import com.codecool.dungeoncrawl.logic.items.general.Jewel;
import com.codecool.dungeoncrawl.logic.items.general.Coin;
import com.codecool.dungeoncrawl.logic.items.general.Key;
import com.codecool.dungeoncrawl.logic.items.potions.HealthPotion;
import com.codecool.dungeoncrawl.logic.items.potions.ManaPotion;
import com.codecool.dungeoncrawl.logic.items.weapons.Sword;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {

    public static GameMap loadMap(String mapFileName) {
        String mapFilePath = mapFileName;
        if (mapFileName.contains("resources")) {
            mapFilePath = mapFileName.split("resources")[1];
        }
        final InputStream is = MapLoader.class.getResourceAsStream(mapFilePath);
        final Scanner scanner = new Scanner(is);
        final int width = scanner.nextInt();
        final int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            final String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    final Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '%':
                            cell.setType(CellType.FOREST);
                            break;
                        case 'u':
                            cell.setType(CellType.GRASS);
                            break;
                        case '*':
                            cell.setType(CellType.STAIRS);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSED_DOOR);
                            break;
                        case 'D':
                            cell.setType(CellType.OPEN_DOOR);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.addToEnemyArmy(new Skeleton(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell, "Nándi"));
                            break;
                        case 'j':
                            cell.setType(CellType.FLOOR);
                            new Jewel(cell);
                            break;
                        case 'b':
                            cell.setType(CellType.FLOOR);
                            new Chest(cell);
                            break;
                        case 'B':
                            cell.setType(CellType.OPEN_CHEST);
                            break;
                        case 'h':
                            cell.setType(CellType.FLOOR);
                            new HealthPotion(cell);
                            break;
                        case 'm':
                            cell.setType(CellType.FLOOR);
                            new ManaPotion(cell);
                            break;
                        case 'c':
                            cell.setType(CellType.FLOOR);
                            new Coin(cell);
                            break;
                        case 'p':
                            cell.setType(CellType.FLOOR);
                            new ChestPlate(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case 'w':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            break;
                        case 'i':
                            cell.setType(CellType.FLOOR);
                            map.addToEnemyArmy(new Scorpion(cell));
                            break;
                        case 'o':
                            cell.setType(CellType.FLOOR);
                            map.addToEnemyArmy(new Ogre(cell));
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            map.addToEnemyArmy(new Ghost(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
