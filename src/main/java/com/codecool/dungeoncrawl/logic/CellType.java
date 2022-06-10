package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    GRASS("grass"),
    WALL("wall"),
    FOREST("forest"),
    STAIRS("stairs down"),
    CLOSED_DOOR("closed door"),
    OPEN_DOOR("open door"),
    OPEN_CHEST("open chest");
    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
