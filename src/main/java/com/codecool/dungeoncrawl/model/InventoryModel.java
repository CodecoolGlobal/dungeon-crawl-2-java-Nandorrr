package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;
import java.util.List;

public class InventoryModel extends BaseModel {

    private List<Item> inventoryItems;

    public InventoryModel(List<Item> inventoryItems) {
        if (inventoryItems.size() == 0) {
            this.inventoryItems = new ArrayList<>();
        } else {
            this.inventoryItems = inventoryItems;
        }
    }

    public List<Item> getInventoryItems() {
        return this.inventoryItems;
    }

    public void setInventoryItems(List<Item> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}
