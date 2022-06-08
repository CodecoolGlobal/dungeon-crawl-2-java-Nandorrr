package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private  DataSource dataSource;
    @Override
    public void add(GameState state) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO game_state (id, current_map, saved_at, player_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, state.getId());
            statement.setString(2,state.getCurrentMap());
            statement.setDate(3, state.getSavedAt());
            statement.setInt(4, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "UPDATE game_state SET current_map = ?, saved_at = ? WHERE id = ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, state.getCurrentMap());
            st.setDate(2, state.getSavedAt());
            st.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        return null;
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }
}
