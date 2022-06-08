package com.codecool.dungeoncrawl.logic.popup;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.actors.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SaveWindow extends AlertBox {

    private final GameDatabaseManager dbManager;
    private final Player player;

    public SaveWindow(String title, String message, String className, GameDatabaseManager dbManager, Player player) {
        super(title, message, className);
        this.dbManager = dbManager;
        this.player = player;
    }

    public void display() {
        window.show();
    }

    @Override
    protected void addWindowElements() {
        VBox container = new VBox(20);
        container.getStyleClass().add(this.className);

        Label saveGameLabel = new Label();
        saveGameLabel.setText(this.title.toUpperCase());

        Label confirmLabel = new Label();
        confirmLabel.setText(this.message);

        Button saveButton = new Button("SAVE");
        Button cancelButton = new Button("CANCEL");

        saveButton.setOnAction(e -> {
            if (dbManager.doesPlayerExist(player.getName())) {
                dbManager.updateSavedGame();
            } else {
                dbManager.saveGame(player);
            }
            this.window.close();
        });

        cancelButton.setOnAction(e -> {
            this.window.close();
        });

        container.getChildren().addAll(saveGameLabel, confirmLabel, saveButton, cancelButton);

        container.setAlignment(Pos.CENTER);
        this.gridPane.setCenter(container);
    }

    @Override
    protected void addWindowElements(GridPane controlSettingsContainer) {

    }
}
