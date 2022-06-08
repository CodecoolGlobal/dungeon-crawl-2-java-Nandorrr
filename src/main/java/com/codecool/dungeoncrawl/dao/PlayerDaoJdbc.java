package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, health, armor, damage) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHealth());
            statement.setInt(3, player.getArmor());
            statement.setInt(4, player.getDamage());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = " UPDATE player SET health = ?, armor = ?, damage = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1,player.getHealth());
            st.setInt(2, player.getArmor());
            st.setInt(3, player.getDamage());
            st.setInt(4, player.getId());
            st.executeUpdate();
            return player.getId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public PlayerModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT player_name, health, armor, damage FROM player WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.next()) { // first row was not found == no data was returned by the query
                return null;
            }
            PlayerModel playerModel = new PlayerModel(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
            playerModel.setId(id); // we already knew author id, so we do not read it from database.
            return playerModel;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, player_name, health, damage, armor FROM player";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<PlayerModel> result = new ArrayList<>();
            while (rs.next()) { // while result set pointer is positioned before or on last row read authors
                PlayerModel playerModel = new PlayerModel(rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5));
                playerModel.setId(rs.getInt(1));
                result.add(playerModel);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all players data", e);
        }
    }
}
