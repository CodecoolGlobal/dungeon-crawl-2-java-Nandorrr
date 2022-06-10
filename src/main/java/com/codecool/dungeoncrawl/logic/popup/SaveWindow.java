package com.codecool.dungeoncrawl.logic.popup;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SaveWindow extends AlertBox {
    private boolean wantsToSave;

    public SaveWindow(String title, String message, String className) {
        super(title, message, className);
    }

    public void display() {
        window.showAndWait();
    }

    @Override
    public boolean displayAndReturnReply() {
        window.showAndWait();
        return wantsToSave;
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
            wantsToSave = true;
            this.window.close();
        });

        cancelButton.setOnAction(e -> {
            wantsToSave = false;
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
