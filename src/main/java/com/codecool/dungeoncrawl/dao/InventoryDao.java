package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.InventoryModel;

import java.util.List;

public interface InventoryDao {
    void add(Item item, int playerId);
    void update(Item item, int playerId);
    void delete(int playerId);
    InventoryModel get(int id);
    List<InventoryModel> getAll();
}
