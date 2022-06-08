package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.List;

public class InventoryModel extends BaseModel {

    private List<Item> inventory;

    public InventoryModel(List<Item> inventory) {
        this.inventory = inventory;
    }

    public List<Item> getInventory() {
        return this.inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
}
