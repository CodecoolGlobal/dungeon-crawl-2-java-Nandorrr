package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Date;
//import java.util.Date;
import java.util.Calendar;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private InventoryDao inventoryDao;
    private GameStateDao gameStateDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        inventoryDao = new InventoryDaoJdbc(dataSource, playerDao);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
    }

    public int saveGame(Player player) throws SQLException {
        PlayerModel playerModel = new PlayerModel(player);
        int playerId = playerDao.add(playerModel);
        GameState gameState = new GameState("first", playerModel);
        gameStateDao.add(gameState);
        return playerId;
    }

    public void updateSavedGame() {
        // TODO: implement method
    }

    public List<GameState> getAllGameState() {
        return gameStateDao.getAll();
    }

    public boolean doesPlayerExist(String playerName) {
        List<PlayerModel> players = playerDao.getAll();

        for (PlayerModel player : players) {
            if (player.getPlayerName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("PSQL_DB_NAME");
        String user = System.getenv("PSQL_USER");
        String password = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
