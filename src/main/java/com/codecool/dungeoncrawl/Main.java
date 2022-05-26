package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.popup.AlertBox;
import com.codecool.dungeoncrawl.logic.popup.ControlsWindow;
import com.codecool.dungeoncrawl.logic.popup.ExitWindow;
import com.codecool.dungeoncrawl.logic.popup.GameOverWindow;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    private final List<String> mapFileNames = addMapNames();
    private Timeline monsterTimeline;
    private int currentMap = 0;
    private String mapFileName = mapFileNames.get(currentMap);
    private GameMap map = MapLoader.loadMap(mapFileName);
    private final Player player = map.getPlayer();
    private Stage stage;
    private ScrollPane scrollPane = new ScrollPane();
    private BorderPane borderPane;
    private VBox menu;
    private Scene scene;
    private int WORLD_SIZE_X = map.getWidth() * Tiles.TILE_WIDTH;
    private int WORLD_SIZE_Y = map.getHeight() * Tiles.TILE_WIDTH;

    private Canvas canvas = new Canvas(WORLD_SIZE_X, WORLD_SIZE_Y);
    private GraphicsContext context = canvas.getGraphicsContext2D();
    private final Label healthLabel = new Label();
    private final Label damageLabel = new Label();
    private final Label armorLabel = new Label();
    private final Label inventoryLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    private List<String> addMapNames() {
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add("/map.txt");
        fileNames.add("/map2.txt");
        fileNames.add("/map3.txt");
        return fileNames;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        initGameWindow();
        moveEnemiesOnMap();
    }

    private void initGameWindow() {
        borderPane = new BorderPane();
        GridPane inventoryBar = createInventoryBar();
        menu =  createSideMenuBar();

        initScrollPane();

        borderPane.setCenter(scrollPane);
        borderPane.setLeft(menu);
        borderPane.setRight(inventoryBar);

        scene = new Scene(borderPane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

        this.stage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

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

    private void moveEnemiesOnMap(){
        ArrayList<Actor> enemyArmy = map.getEnemyArmy();
        monsterTimeline = new Timeline();
        monsterTimeline.setCycleCount(Animation.INDEFINITE);
        monsterTimeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            for (Actor enemy : enemyArmy) {
                if(!enemy.isAlive()){
                    map.removeEnemyFromArmy(enemy);
                }
                if (!player.isAlive()) {
                    System.out.println("YOU DIED");
                    monsterTimeline.stop();
                    break;
                } else {
                    enemy.executeBehaviour();
                }
            }
            refresh();
        }));
        monsterTimeline.play();
    }

    private void initNextMap() {
        currentMap++;
        mapFileName = mapFileNames.get(currentMap);
        map = MapLoader.loadMap(mapFileName);

        Cell cell = map.getPlayer().getCell();
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
        VBox menu = new VBox();
        menu.getStyleClass().add("menubar");

        Button newGame = new Button("NEW GAME");
        Button saveGame = new Button("SAVE GAME");
        Button controls = new Button("CONTROLS");
        Button quit = new Button("QUIT");

        newGame.setDisable(true);
        saveGame.setDisable(true);

        addButtonEventListener(controls);
        addButtonEventListener(quit);

        menu.getChildren().addAll(newGame, saveGame, controls, quit);

        return menu;
    }

    private void addButtonEventListener(Button button) {
        if (button.getText().equalsIgnoreCase("quit")) {
            button.setOnAction(e -> {
                borderPane.requestFocus();
                monsterTimeline.pause();
                AlertBox exitWindow = createAlertBox("quit");
                exitWindow.display();
                monsterTimeline.play();
            });
        } else if (button.getText().equalsIgnoreCase("controls")) {
            button.setOnAction(e -> {
                borderPane.requestFocus();
                monsterTimeline.pause();
                AlertBox controlsWindow = createAlertBox("controls");
                controlsWindow.display();
                monsterTimeline.play();
            });
        }
    }

    private AlertBox createAlertBox(String alertBoxType) {
        switch (alertBoxType) {
            case "quit":
                ExitWindow exitWindow = new ExitWindow("Quit Game", "Are you sure you want to quit?\n" +
                        "All unsaved progress will be lost.", "exitWindow");
                return exitWindow;
            case "gameOver":
                GameOverWindow gameOverWindow = new GameOverWindow("Game Over", "Your journey ends here...", "gameOverWindow");
                return gameOverWindow;
            case "controls":
                ControlsWindow controlsWindow = new ControlsWindow("Controls", "KEY BINDINGS", "controlsWindow");
                return controlsWindow;
            default:
                return null;
        }
    }

    private GridPane createInventoryBar() {
        GridPane ui = new GridPane();
        ui.getStyleClass().add("player-stats");
        ui.getColumnConstraints().add(new ColumnConstraints(200)); // column 0 is 100 wide
        ui.getColumnConstraints().add(new ColumnConstraints(100)); // column 1 is 200 wide

        Label health = new Label("HEALTH: ");
        Label damage = new Label("DAMAGE: ");
        Label armor = new Label("ARMOR: ");
        Label inventory = new Label("INVENTORY: ");

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

    private void onKeyPressed(KeyEvent keyEvent) {
        Cell lastPlayerLocation =  player.getCell();

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
        }
        if (player.movingToNextMap()) initNextMap();
    }

    private void checkGameEndConditions() {
        //TODO: check win condition

        if (!player.isAlive()) {
            gameOver();
        }
    }

    private void gameOver() {
        monsterTimeline.stop();
        borderPane.requestFocus();
        AlertBox gameOverWindow = createAlertBox("gameOver");
        gameOverWindow.display();
    }

    private void refresh() {
        checkGameEndConditions();

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
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
}
