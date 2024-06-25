package ru.morozovit.recipeviewer.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;
    private static App instance;
    @Override
    public void start(@NotNull Stage stage) throws IOException {
        // Store the data in the static field so it can be accessed from anywhere
        primaryStage = stage;
        instance = this;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 670, 490);
        // Some settings
        stage.setMaxHeight(scene.getHeight());
        stage.setMaxWidth(scene.getWidth());
        stage.setResizable(false);
        stage.setTitle("RecipeViewer 1.0 (GUI)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void openURI(String uri) {
        getHostServices().showDocument(uri);
    }

    public static App getInstance() {
        return instance;
    }

    public static void openURL(String url) {
        getInstance().openURI(url);
    }
}