package com.codecool.dungeoncrawl.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GameState extends BaseModel {
    private Timestamp savedAt;
    private String currentMap;
    private List<String> discoveredMaps = new ArrayList<>();
    private PlayerModel player;
    private InventoryModel inventory;

    public GameState(String currentMapPath, Timestamp savedAt, PlayerModel player, InventoryModel inventory) {
        this.currentMap = currentMapPath;
        this.savedAt = savedAt;
        this.player = player;
        this.inventory = inventory;
    }

    public GameState(String currentMap, PlayerModel player) {
        this.currentMap = currentMap;
        this.player = player;
    }

    public Timestamp getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Timestamp savedAt) {
        this.savedAt = savedAt;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public List<String> getDiscoveredMaps() {
        return discoveredMaps;
    }

    public void addDiscoveredMap(String map) {
        this.discoveredMaps.add(map);
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public int getMapIndex() {
        return Integer.parseInt(currentMap.split("savedMaps/")[1].substring(3, 4)) - 1;
    }

    public InventoryModel getInventory() {
        return inventory;
    }
}
