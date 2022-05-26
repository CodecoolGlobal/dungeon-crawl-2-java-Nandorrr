package com.codecool.dungeoncrawl.logic.popup;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class ControlsWindow extends AlertBox {

    public ControlsWindow(String title, String message, String className) {
        super(title, message, className);
    }

    @Override
    protected Stage createPopUp(String title) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        this.gridPane = new BorderPane();
        gridPane.setMinSize(600, 400);

        addWindowElements();

        Scene scene = new Scene(this.gridPane);
        scene.getStylesheets().add(Objects.requireNonNull(GameOverWindow.class.getResource("/style.css")).toExternalForm());
        window.setScene(scene);

        return window;
    }

    @Override
    protected void addWindowElements() {
        GridPane controlSettings = new GridPane();
        controlSettings.setMinSize(600, 200);
        controlSettings.getStyleClass().add(this.className);
        controlSettings.getColumnConstraints().add(new ColumnConstraints(200)); // column 0 is 100 wide
        controlSettings.getColumnConstraints().add(new ColumnConstraints(100)); // column 1 is 200 wide


        // add key bindings info
        Label moveUp = new Label("MOVE UP: ");
        Label moveDown = new Label("MOVE DOWN: ");
        Label moveLeft = new Label("MOVE LEFT: ");
        Label moveRight = new Label("MOVE RIGHT: ");
        Label moveUpLabel = new Label("W");
        Label moveDownLabel = new Label("A");
        Label moveLeftLabel = new Label("S");
        Label moveRightLabel = new Label("D");
        Label attack = new Label("ATTACK: ");
        Label attackLabel = new Label("SPACE");
        Label loot = new Label("LOOT: ");
        Label lootLabel = new Label("F");

        controlSettings.add(moveUp, 0, 0);
        controlSettings.add(moveUpLabel, 1, 0);

        controlSettings.add(moveDown, 0, 1);
        controlSettings.add(moveDownLabel, 1, 1);

        controlSettings.add(moveLeft, 0, 2);
        controlSettings.add(moveLeftLabel, 1, 2);

        controlSettings.add(moveRight, 0, 3);
        controlSettings.add(moveRightLabel, 1, 3);

        controlSettings.add(attack, 0, 4);
        controlSettings.add(attackLabel, 1, 4);

        controlSettings.add(loot, 0, 5);
        controlSettings.add(lootLabel, 1, 5);

        // add title
        HBox titleContainer = new HBox();
        titleContainer.getStyleClass().add("titleContainer");
        titleContainer.setMinSize(600, 100);
        Label keyBindings = new Label(this.message);
        keyBindings.getStyleClass().add("controlsTitle");
        titleContainer.getChildren().add(keyBindings);

        // add buttons
        HBox buttonContainer = new HBox();
        buttonContainer.getStyleClass().add("buttonContainer");
        buttonContainer.setMinSize(600, 100);
        Button closeButton = new Button("CLOSE");

        closeButton.setOnAction(e -> {
            this.window.close();
        });

        buttonContainer.getChildren().add(closeButton);
        buttonContainer.setAlignment(Pos.BASELINE_CENTER);

        this.gridPane.setTop(titleContainer);
        this.gridPane.setCenter(controlSettings);
        this.gridPane.setBottom(buttonContainer);
    }

    @Override
    protected void addWindowElements(GridPane controlSettingsContainer) {

    }

}
