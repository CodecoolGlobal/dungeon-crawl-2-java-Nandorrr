package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("forest", new Tile(3, 2));
        tileMap.put("stairs down", new Tile(4, 6));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("closed door", new Tile(3, 3));
        tileMap.put("open door", new Tile(4, 3));
        tileMap.put("key", new Tile(16, 23));
        tileMap.put("chest", new Tile(8, 6));
        tileMap.put("open chest", new Tile(9, 6));
        tileMap.put("jewel", new Tile(17, 22));
        tileMap.put("chest plate", new Tile(4, 23));
        tileMap.put("sword", new Tile(0, 30));
        tileMap.put("health potion", new Tile(23, 22));
        tileMap.put("mana potion", new Tile(16, 25));
        tileMap.put("coin", new Tile(9, 25));
        tileMap.put("ogre", new Tile(30, 6));
        tileMap.put("scorpion", new Tile(24, 5));
        tileMap.put("ghost", new Tile(27,6));
        tileMap.put("grass", new Tile(5,0));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
