package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerDao {
    int add(PlayerModel player);
    int update(PlayerModel player);
    PlayerModel get(int id);
    List<PlayerModel> getAll();
    int getPlayerIdByName(String playerName);
}
