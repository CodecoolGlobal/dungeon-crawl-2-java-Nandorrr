package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.armors.ChestPlate;
import com.codecool.dungeoncrawl.logic.items.general.Chest;
import com.codecool.dungeoncrawl.logic.items.general.Coin;
import com.codecool.dungeoncrawl.logic.items.general.Jewel;
import com.codecool.dungeoncrawl.logic.items.general.Key;
import com.codecool.dungeoncrawl.logic.items.potions.HealthPotion;
import com.codecool.dungeoncrawl.logic.items.potions.ManaPotion;
import com.codecool.dungeoncrawl.logic.items.weapons.Sword;
import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao {

    private final DataSource dataSource;

    public InventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Item item, int playerId, int gameStateId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO inventory (player_id, game_state_id, item_name) VALUES (?, ?, ?)";
            PreparedStatement prepStat = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepStat.setInt(1, playerId);
            prepStat.setInt(2, gameStateId);
            prepStat.setString(3, item.getTileName());
            prepStat.executeUpdate();
            ResultSet resultSet = prepStat.getGeneratedKeys();
            resultSet.next();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public InventoryModel getAll(int playerId, int gameStateId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT item_name FROM inventory WHERE player_id = ? AND game_state_id = ?";
            PreparedStatement prepStat = connection.prepareStatement(sql);
            prepStat.setInt(1, playerId);
            prepStat.setInt(2, gameStateId);
            ResultSet resultSet = prepStat.executeQuery();

            List<Item> inventoryItems = getItems(resultSet);
            return new InventoryModel(inventoryItems);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Item> getItems(ResultSet resultSet) throws SQLException {
        List<Item> inventoryItems = new ArrayList<>();
        while (resultSet.next()) {
            String itemName = resultSet.getString(1);

            if (itemName.equalsIgnoreCase("chest plate")) {
                ChestPlate chestPlate = new ChestPlate();
                inventoryItems.add(chestPlate);
            } else if (itemName.equalsIgnoreCase("chest")) {
                Chest chest = new Chest();
                inventoryItems.add(chest);
            } else if (itemName.equalsIgnoreCase("coin")) {
                Coin coin = new Coin();
                inventoryItems.add(coin);
            } else if (itemName.equalsIgnoreCase("jewel")) {
                Jewel jewel = new Jewel();
                inventoryItems.add(jewel);
            } else if (itemName.equalsIgnoreCase("key")) {
                Key key = new Key();
                inventoryItems.add(key);
            } else if (itemName.equalsIgnoreCase("health potion")) {
                HealthPotion healthPotion = new HealthPotion();
                inventoryItems.add(healthPotion);
            } else if (itemName.equalsIgnoreCase("mana potion")) {
                ManaPotion manaPotion = new ManaPotion();
                inventoryItems.add(manaPotion);
            } else if (itemName.equalsIgnoreCase("sword")) {
                Sword sword = new Sword();
                inventoryItems.add(sword);
            }
        }
        return inventoryItems;
    }
}
