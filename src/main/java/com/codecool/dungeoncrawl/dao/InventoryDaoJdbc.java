package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao {

    private DataSource dataSource;
    private PlayerDao playerDao;

    public InventoryDaoJdbc(DataSource dataSource, PlayerDao playerDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
    }

    @Override
    public void add(Item item, int playerId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO inventory (player_id, item_name, item_value) VALUES (?, ?, ?)";
            PreparedStatement prepStat = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            prepStat.setInt(1, playerId);
            prepStat.setString(2, item.getTileName());
            prepStat.setInt(3, item.getValue());
            prepStat.executeUpdate();
            ResultSet resultSet = prepStat.getGeneratedKeys();
            resultSet.next();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(int playerId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM inventory WHERE player_id = ?";
            PreparedStatement prepStat = connection.prepareStatement(sql);
            prepStat.executeQuery(sql);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<InventoryModel> getAll() {
        try (Connection connection = dataSource.getConnection()) {
            // FIRST STEP - player_id, item_name, item_value
            String sql = "SELECT player_id, item_name, item_value FROM inventory";
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            List<Item> result = new ArrayList<>();
            while (resultSet.next()) {
                // SECOND STEP - read all data from result set
                int playerId = resultSet.getInt(1);
                String itemName = resultSet.getString(2);
                int itemValue = resultSet.getInt(3);

                // THIRD STEP - find player with id == authorId
                PlayerModel playerModel = playerDao.get(playerId);

                // FOURTH STEP - create a new InventoryModel class instance and add it to result list.
//                Book book = new Book(author, title);
//                book.setId(id);
//                result.add(book);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
