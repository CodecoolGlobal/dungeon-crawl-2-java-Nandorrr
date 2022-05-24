package com.codecool.dungeoncrawl.logic.util;

public enum Directions {
    UP(0, -1),
    RIGHT(1,0),
    DOWN(0, 1),
    LEFT(-1, 0);

    public int dx;

    public int dy;


    Directions(int dx, int dy) {
    }
}
