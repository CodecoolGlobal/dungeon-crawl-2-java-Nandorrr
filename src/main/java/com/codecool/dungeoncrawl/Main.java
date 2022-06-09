package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.popup.*;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.codecool.dungeoncrawl.logic.util.CurrentTime;
import com.codecool.dungeoncrawl.logic.util.WriteToFiles;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.sql.SQLException;
import java.io.IOException;

public class Main extends Application  {

    private static final String QUIT_LABEL = "quit";
    private static final String CONTROLS_LABEL = "controls";
    private static final String GAME_OVER_LABEL = "gameOver";
    private static final String NEW_GAME_LABEL = "new game";
    private static final String SAVE_LABEL = "save game";
    private static final String LOAD_LABEL = "load game";
    private final List<String> mapFileNames = addMapNames();
    private Timeline monsterTimeline = new Timeline();
    private int currentMapIndex = 0;
    private String mapFileName = mapFileNames.get(currentMapIndex);
    private GameMap map = MapLoader.loadMap(mapFileName);
    private  Player player = map.getPlayer();
    private Stage stage;
    private ScrollPane scrollPane = new ScrollPane();
    private BorderPane borderPane;
    private int WORLD_SIZE_X = map.getWidth() * Tiles.TILE_WIDTH;
    private int WORLD_SIZE_Y = map.getHeight() * Tiles.TILE_WIDTH;

    private Canvas canvas = new Canvas(WORLD_SIZE_X, WORLD_SIZE_Y);
    private GraphicsContext context = canvas.getGraphicsContext2D();
    private final Label healthLabel = new Label();
    private final Label damageLabel = new Label();
    private final Label armorLabel = new Label();
    private final Label inventoryLabel = new Label();
    private GameDatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }

    private List<String> addMapNames() {
        List<String> fileNames = new ArrayList<>();
        fileNames.add("/map1.txt");
        fileNames.add("/map2.txt");
        fileNames.add("/map3.txt");
        return fileNames;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        this.stage = primaryStage;
        initGameWindow();
        moveEnemiesOnMap();
    }

    private void initGameWindow() throws SQLException{
        borderPane = new BorderPane();
        GridPane inventoryBar = createInventoryBar();
        VBox menu = createSideMenuBar();

        initScrollPane();

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(menu);
        borderPane.setRight(inventoryBar);

        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

        this.stage.setScene(scene);
        refresh();

        scene.setOnKeyPressed(keyEvent -> {
            try {
                onKeyPressed(keyEvent);
            } catch (SQLException | IOException e) {
                try {
                    throw new SQLException(e);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        );

        this.stage.setTitle("Dungeon Crawl");
        this.stage.show();
        this.stage.setFullScreen(true);

        borderPane.requestFocus();
    }

    private void initScrollPane() {
        scrollPane.pannableProperty().set(true);
        scrollPane.setContent(canvas);

        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void moveEnemiesOnMap() {
        final List<Actor> enemyArmy = map.getEnemyArmy();
        monsterTimeline.setCycleCount(Animation.INDEFINITE);
        monsterTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            for (final Actor enemy : enemyArmy) {
                if (!player.isAlive()) {
                    System.out.println("YOU DIED");
                    monsterTimeline.stop();
                    monsterTimeline.setCycleCount(0);
                    break;
                } else {
                    if(!enemy.isAlive()){
                        map.removeEnemyFromArmy(enemy);
                    }
                    enemy.executeBehaviour();
                }
            }
            refresh();
        }));
        monsterTimeline.play();
    }

    private void initNextMap() {
        currentMapIndex++;
        mapFileName = mapFileNames.get(currentMapIndex);
        map = MapLoader.loadMap(mapFileName);

        final Cell cell = map.getPlayer().getCell();
        player.setCell(cell);
        map.setPlayer(player);

        WORLD_SIZE_X = map.getWidth() * Tiles.TILE_WIDTH;
        WORLD_SIZE_Y = map.getHeight() * Tiles.TILE_WIDTH;

        canvas = new Canvas(WORLD_SIZE_X, WORLD_SIZE_Y);

        context = canvas.getGraphicsContext2D();

        scrollPane = new ScrollPane();

        initScrollPane();

        borderPane.setCenter(scrollPane);

        player.setMovingToNextMap(false);

        moveEnemiesOnMap();

        borderPane.requestFocus();

        refresh();
    }

    private VBox createSideMenuBar() {
        final VBox menu = new VBox();
        menu.getStyleClass().add("menubar");

        final Button newGame = new Button("NEW GAME");
        final Button saveGame = new Button("SAVE GAME");
        final Button loadGame = new Button("LOAD GAME");
        final Button controls = new Button("CONTROLS");
        final Button quit = new Button("QUIT");

        newGame.setDisable(true);

        addButtonEventListener(saveGame);
        addButtonEventListener(loadGame);
        addButtonEventListener(controls);
        addButtonEventListener(quit);

        menu.getChildren().addAll(newGame, saveGame, loadGame, controls, quit);

        return menu;
    }

    private void addButtonEventListener(Button button) {
        if (button.getText().equalsIgnoreCase(QUIT_LABEL)) {
            button.setOnAction(e -> {
                openAlertBox(QUIT_LABEL);
            });
        } else if (button.getText().equalsIgnoreCase(CONTROLS_LABEL)) {
            button.setOnAction(e -> {
                openAlertBox(CONTROLS_LABEL);
            });
        } else if (button.getText().equalsIgnoreCase(SAVE_LABEL)) {
            button.setOnAction(e -> {
                openAlertBox(SAVE_LABEL);
            });
        } else if (button.getText().equalsIgnoreCase(LOAD_LABEL)) {
            button.setOnAction(e -> {
                openAlertBox(LOAD_LABEL);
            });
        }
    }

    private void openAlertBox(String boxType) {
        borderPane.requestFocus();
        monsterTimeline.pause();
        AlertBox alertBox = createAlertBox(boxType);
        if (boxType == LOAD_LABEL) {
            int gameStateId = alertBox.displayAndReturn();
            GameState chosenGameState = dbManager.getGameStateById(gameStateId);
            if (chosenGameState != null) loadGame(chosenGameState);
        } else {
            alertBox.display();
        }
        monsterTimeline.play();
    }

    private AlertBox createAlertBox(String alertBoxType) {
        switch (alertBoxType) {
            case QUIT_LABEL:
                return new ExitWindow("Quit Game", "Are you sure you want to quit?\n" +
                        "All unsaved progress will be lost.", "exitWindow");
            case GAME_OVER_LABEL:
                return new GameOverWindow("Game Over", "Your journey ends here...", "gameOverWindow");
            case CONTROLS_LABEL:
                return new ControlsWindow("Controls", "KEY BINDINGS", "controlsWindow");
            case SAVE_LABEL:
                return new SaveWindow("Save Game", "To save your current game state, click SAVE.",
                        "saveWindow", dbManager, player);
            case LOAD_LABEL:
                return new LoadWindow("Load Game", "Choose a game state you wish to load: ",
                        "loadWindow", dbManager);
            default:
                throw new IllegalArgumentException("Invalid alert box type: " + alertBoxType);
        }
    }

    private GridPane createInventoryBar() {
        final GridPane ui = new GridPane();
        ui.getStyleClass().add("player-stats");
        ui.getColumnConstraints().add(new ColumnConstraints(200)); // column 0 is 100 wide
        ui.getColumnConstraints().add(new ColumnConstraints(100)); // column 1 is 200 wide

        final Label health = new Label("HEALTH: ");
        final Label damage = new Label("DAMAGE: ");
        final Label armor = new Label("ARMOR: ");
        final Label inventory = new Label("INVENTORY: ");

        ui.add(health, 0, 0);
        ui.add(healthLabel, 1, 0);

        ui.add(damage, 0, 1);
        ui.add(damageLabel, 1, 1);

        ui.add(armor, 0, 2);
        ui.add(armorLabel, 1, 2);

        ui.add(inventory, 0, 4);
        ui.add(inventoryLabel, 0, 5);

        return ui;
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) throws SQLException, IOException {
        final Cell lastPlayerLocation =  player.getCell();

        //Nandi's dirt was left here. Shame on him.
        GameDatabaseManager dbManager = new GameDatabaseManager();
        WriteToFiles writeToFiles = new WriteToFiles();
        CurrentTime currentTime= new CurrentTime();
        String date = currentTime.getCurrentDateInString();


        String filename = mapFileName + date;
        String filePath = writeToFiles.fileCreater(filename);
        if (player.isAlive()) {
            switch (keyEvent.getCode()) {
                case W:
                    player.move(0, -1);
                    refresh();
                    if (lastPlayerLocation != player.getCell()) {
                        scrollPane.setVvalue(scrollPane.getVvalue() - 1.6 / map.getHeight());
                    }
                    break;
                case S:
                    player.move(0, 1);
                    refresh();
                    if (lastPlayerLocation != player.getCell()) {
                        scrollPane.setVvalue(scrollPane.getVvalue() + 1.6 / map.getHeight());
                    }
                    break;
                case A:
                    player.move(-1, 0);
                    refresh();
                    if (lastPlayerLocation != player.getCell()) {
                        scrollPane.setHvalue(scrollPane.getHvalue() - 1.6 / map.getWidth());
                    }
                    break;
                case D:
                    player.move(1,0);
                    refresh();
                    if (lastPlayerLocation != player.getCell()) {
                        scrollPane.setHvalue(scrollPane.getHvalue() + 1.6 / map.getWidth());
                    }
                    break;
                case F:
                    player.pickUpItem();
                    refresh();
                    break;
                case SPACE:
                    player.hitActor();
                    refresh();
                    break;

                case U:
                    dbManager.setup();
                    dbManager.saveGame(player);

                    writeToFiles.writeInTheFile(map.serializeMap(), filePath);
                    System.out.println("Save was finished");
                    refresh();
                    break;
            }
        }
        if (player.movingToNextMap()) initNextMap();
    }

    private void handleGameEndConditions() {
        //TODO: check win condition

        if (!player.isAlive()) {
            gameOver();
        }
    }

    private void gameOver() {
        final AlertBox gameOverWindow = createAlertBox("gameOver");
        gameOverWindow.display();
    }

    private void refresh() {
        handleGameEndConditions();

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                final Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + player.getHealth());
        damageLabel.setText("" + player.getDamage());
        armorLabel.setText("" + player.getArmor());
        inventoryLabel.setText("" + player.getInventoryContentText());
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }

    private void loadGame(GameState gameState) {
        currentMapIndex = gameState.getMapIndex();
        mapFileName = gameState.getCurrentMap();
        map = MapLoader.loadMap(mapFileName);
        final Cell cell = map.getPlayer().getCell();
        player = PlayerModelToPlayer(gameState.getPlayer(), cell, gameState.getInventory());

        player.setCell(cell);
        map.setPlayer(player);

        WORLD_SIZE_X = map.getWidth() * Tiles.TILE_WIDTH;
        WORLD_SIZE_Y = map.getHeight() * Tiles.TILE_WIDTH;

        canvas = new Canvas(WORLD_SIZE_X, WORLD_SIZE_Y);

        context = canvas.getGraphicsContext2D();

        scrollPane = new ScrollPane();

        initScrollPane();

        borderPane.setCenter(scrollPane);

        player.setMovingToNextMap(false);

        monsterTimeline = new Timeline();

        moveEnemiesOnMap();

        borderPane.requestFocus();

        refresh();

    }

    private Player PlayerModelToPlayer(PlayerModel playerModel, Cell cell, InventoryModel inventory){
        return new Player(cell, playerModel.getPlayerName(), playerModel.getHealth(), playerModel.getDamage(), playerModel.getArmor(), inventory.getInventoryItems());

    }

}
