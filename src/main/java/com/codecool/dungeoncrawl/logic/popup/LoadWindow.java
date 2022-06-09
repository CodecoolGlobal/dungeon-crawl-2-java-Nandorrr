package com.codecool.dungeoncrawl.logic.popup;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.model.GameState;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadWindow extends AlertBox {

    private VBox mainContainer;
    private int saveId;
    private Label chosenGame;
    private final List<String> saveNames = new ArrayList<>();

    private final GameDatabaseManager dbManager;
    private static final ToggleGroup group = new ToggleGroup();

    public LoadWindow(String title, String message, String className, GameDatabaseManager dbManager) {
        super(title, message, className);
        this.dbManager = dbManager;
    }

    @Override
    public int displayAndReturn() {
        addPopUpContent();
        window.showAndWait();
        return saveId;
    }

    private void generateSavedGamesList(VBox layout) {
        List<GameState> gameStates = dbManager.getAllGameStates();
        int counter = 1;
        RadioButton saveFileButton;
        for (GameState state: gameStates){
            saveFileButton = new RadioButton();
            saveFileButton.getStyleClass().add("saveFileButton");
            String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(state.getSavedAt());
            saveNames.add(time);
            saveFileButton.setText(
                    "#" + counter + " | "
                            + time);
            saveFileButton.setFont(Font.font("Verdana"));
            saveFileButton.getStyleClass().add("savedGameBtn");
            saveFileButton.setToggleGroup(group);
            layout.getChildren().add(saveFileButton);
            saveFileButton.setOnAction( e -> {
                chosenGame.setText(time);
                saveId = state.getId();
            });
            counter++;
        }
    }

    @Override
    protected Stage createPopUp(String title) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(600);

        return window;
    }

    private void addPopUpContent() {
        mainContainer = new VBox(10);
        mainContainer.setMinSize(500, 600);
        mainContainer.setAlignment(Pos.CENTER);

        chosenGame = new Label("test");

        generateTopLabel();
        addWindowElements();
        createButtons();

        chosenGame.setId("chosenGame");
        chosenGame.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainContainer);
        scene.getStylesheets().add(Objects.requireNonNull(LoadWindow.class.getResource("/style.css")).toExternalForm());
        window.setScene(scene);
    }

    private void generateTopLabel() {
        VBox topLayout = new VBox(10);
        Label label = new Label("Chosen File: ");
        topLayout.getChildren().addAll(label, chosenGame);
        mainContainer.getChildren().add(topLayout);
        topLayout.setAlignment(Pos.CENTER);
    }

    private void createButtons() {
        Button loadButton = new Button("LOAD");
        Button cancelButton = new Button("CANCEL");

        loadButton.setOnAction( e -> {
            if (!chosenGame.getText().equals("")){
                window.close();
            }
        });

        cancelButton.setOnAction(e -> {
            this.window.close();
        });

        mainContainer.getChildren().addAll(loadButton, cancelButton);
    }


    @Override
    protected void addWindowElements() {
        VBox container = new VBox(10);
        container.getStyleClass().add(this.className);

        ScrollPane sp = new ScrollPane();

        generateSavedGamesList(container);

        sp.setContent(container);
        sp.setVmax(500);
        sp.setPrefSize(300, 300);

        mainContainer.getChildren().add(sp);
        container.setAlignment(Pos.CENTER);

        sp.setFitToWidth(true);

        mainContainer.getChildren().add(container);
        container.setAlignment(Pos.CENTER);
    }

    @Override
    protected void addWindowElements(GridPane controlSettingsContainer) {
    }

}
