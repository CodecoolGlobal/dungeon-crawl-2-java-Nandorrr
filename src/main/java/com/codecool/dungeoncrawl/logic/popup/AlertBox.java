package com.codecool.dungeoncrawl.logic.popup;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public abstract class AlertBox {

    protected String title;

    protected String message;

    protected String className;

    protected Stage window;

    protected BorderPane gridPane;

    public AlertBox(String title, String message, String className) {
        this.title = title;
        this.message = message;
        this.className = className;
        this.window = createPopUp(title);
    }

    public void display() {
        window.showAndWait();
    }

    public int displayAndReturn() {
        window.showAndWait();
        return 0;
    }

    protected Stage createPopUp(String title) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        this.gridPane = new BorderPane();
        gridPane.setMinSize(600, 350);

        addWindowElements();

        Scene scene = new Scene(gridPane);
        scene.getStylesheets().add(Objects.requireNonNull(GameOverWindow.class.getResource("/style.css")).toExternalForm());
        window.setScene(scene);

        return window;
    }

    protected void addWindowElements() {
        VBox container = new VBox(10);
        container.getStyleClass().add(this.className);

        Label gameOverLabel = new Label();
        gameOverLabel.setText(this.title.toUpperCase());

        Label confirmLabel = new Label();
        confirmLabel.setText(this.message);

        Button closeButton = new Button("QUIT");
        Button cancelButton = new Button("CANCEL");

        closeButton.setOnAction(e -> {
            this.window.close();
            System.exit(0);
        });

        cancelButton.setOnAction(e -> {
            this.window.close();
        });

        container.getChildren().addAll(gameOverLabel, confirmLabel, closeButton, cancelButton);

        container.setAlignment(Pos.CENTER);
        this.gridPane.setBottom(container);
    }

    protected abstract void addWindowElements(GridPane controlSettingsContainer);
}
