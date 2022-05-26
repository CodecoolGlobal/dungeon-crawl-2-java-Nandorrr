package com.codecool.dungeoncrawl.logic.popup;

import javafx.scene.layout.GridPane;

public class GameOverWindow extends AlertBox {

    public GameOverWindow(String title, String message, String className) {
        super(title, message, className);
    }

    @Override
    public void display() {
        window.show();
    }

    @Override
    protected void addWindowElements(GridPane controlSettingsContainer) {

    }

}
