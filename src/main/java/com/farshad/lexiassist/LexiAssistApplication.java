package com.farshad.lexiassist;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LexiAssistApplication extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("LexiAssist"));

        Scene scene = new Scene(root, 1000, 650);

        stage.setTitle("LexiAssist");
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}