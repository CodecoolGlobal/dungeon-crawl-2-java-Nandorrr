package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private List<String> mapFileNames = addMapNames();
    private int currentMap = 0;
    private String mapFileName = mapFileNames.get(currentMap);
    private GameMap map = MapLoader.loadMap(mapFileName);
    private Player player = map.getPlayer();
    private Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    private GraphicsContext context = canvas.getGraphicsContext2D();
    private Label healthLabel = new Label();
    private Label damageLabel = new Label();
    private Label armorLabel = new Label();
    private Label inventoryLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    private List<String> addMapNames() {
        List<String> fileNames = new ArrayList();
        fileNames.add("/map.txt");
//        fileNames.add("/map2.txt");
//        fileNames.add("/map3.txt");
        return fileNames;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane borderPane = new BorderPane();
        GridPane ui = createInventoryBar();
        VBox menu =  createSideMenuBar();

        borderPane.setCenter(canvas);
        borderPane.setLeft(menu);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
        primaryStage.setFullScreen(true);

        play();
    }

    private void moveEnemiesOnMap(){
        ArrayList<Actor> enemyArmy = map.getEnemyArmy();
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (Actor enemy : enemyArmy) {
                    if(!enemy.isAlive()){
                        map.removeEnemyFromArmy(enemy);
                    }
                    if (!player.isAlive()) {
                        System.out.println("YOU DIED");
                        timeline.stop();
                        break;
                    } else {
                        enemy.executeBehaviour();
                    }
                }
                refresh();
            }
        }));
        timeline.play();
    }

    private void play() {
        moveEnemiesOnMap();
//        setCameraOnPlayer(player.getCell());
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
        controls.setDisable(true);
        quit.setDisable(true);

        menu.getChildren().addAll(newGame, saveGame, controls, quit);

        return menu;
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
        switch (keyEvent.getCode()) {
            case W:
                player.move(0, -1);
                refresh();
                break;
            case S:
                player.move(0, 1);
                refresh();
                break;
            case A:
                player.move(-1, 0);
                refresh();
                break;
            case D:
                player.move(1,0);
                refresh();
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
    }

    private void refresh() {
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
