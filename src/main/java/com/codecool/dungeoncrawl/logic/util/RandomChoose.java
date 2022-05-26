package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;
import java.util.Random;

public class RandomChoose {

    private Random random = new Random();

    public RandomChoose() {
    }

    public Cell randomChooseFromAList(List<Cell> listOfCells){
        return listOfCells.get(random.nextInt(listOfCells.size()));
    }
}
