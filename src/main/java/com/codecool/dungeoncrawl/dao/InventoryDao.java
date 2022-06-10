package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.InventoryModel;

public interface InventoryDao {
    void add(Item item, int playerId, int gameStateId);
    InventoryModel getAll(int playerId, int gameStateId);
}
