package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import org.postgresql.ds.PGSimpleDataSource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private InventoryDao inventoryDao;
    private GameStateDao gameStateDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        inventoryDao = new InventoryDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
    }

    public int saveGame(Player player) {
        PlayerModel playerModel = new PlayerModel(player);
        GameState gameState = new GameState("first", playerModel);

        playerDao.add(playerModel);
        gameStateDao.add(gameState);

        int playerId = playerModel.getId();
        List<Item> inventory = player.getInventory();
        inventory.forEach(item -> inventoryDao.add(item, playerId));

        return playerId;
    }

    public void updateSavedGame() {
        // TODO: implement method
        System.out.println("This was an existing player - update instead of save");
    }

    public List<GameState> getAllGameStates() {
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
