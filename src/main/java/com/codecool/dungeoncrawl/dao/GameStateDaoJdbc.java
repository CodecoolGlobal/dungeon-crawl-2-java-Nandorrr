package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {

    private final DataSource dataSource;
    private final PlayerDao playerDao;
    private final InventoryDao inventoryDao;

    public GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao, InventoryDao inventoryDao){
        this.dataSource = dataSource;
        this.playerDao = playerDao;
        this.inventoryDao = inventoryDao;
    }
    @Override
    public void add(GameState state) {
        try(Connection conn = dataSource.getConnection()){
            String sql = "INSERT INTO game_state (current_map,  player_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,state.getCurrentMap());
            statement.setInt(2, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
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
            st.setTimestamp(2, state.getSavedAt());
            st.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()){
                String sql = "SELECT id, player_id, current_map, saved_at FROM game_state WHERE id = ?";
                PreparedStatement st = conn.prepareStatement(sql);
                st.setInt(1, id);
                ResultSet resultSet = st.executeQuery();

                if (!resultSet.next()) return null;

                int gameStateId = resultSet.getInt(1);
                int playerId = resultSet.getInt(2);
                String currentMap = resultSet.getString(3);
                Timestamp savedAt = resultSet.getTimestamp(4);
                PlayerModel player = playerDao.get(playerId);
                InventoryModel inventory = inventoryDao.getAll(playerId, gameStateId);
                GameState gameState = new GameState(currentMap, savedAt, player, inventory);
                gameState.setId(gameStateId);
                return gameState;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, player_id, current_map, saved_at FROM game_state";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<GameState> listOfGameStates = new ArrayList<>();

            while(resultSet.next()) {
                int gameStateId = resultSet.getInt(1);
                int playerId = resultSet.getInt(2);
                String currentMap = resultSet.getString(3);
                Timestamp savedAt = resultSet.getTimestamp(4);
                PlayerModel player = playerDao.get(playerId);
                InventoryModel inventory = inventoryDao.getAll(playerId, gameStateId);
                GameState state = new GameState(currentMap, savedAt, player, inventory);
                state.setId(resultSet.getInt(1));
                listOfGameStates.add(state);
            }
            return listOfGameStates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
