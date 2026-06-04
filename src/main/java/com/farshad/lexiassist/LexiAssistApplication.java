package com.farshad.lexiassist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LexiAssistApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                LexiAssistApplication.class.getResource("/ui/editor-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1100, 700);
        scene.getStylesheets().add(
                LexiAssistApplication.class.getResource("/ui/styles.css").toExternalForm()
        );

        stage.setTitle("LexiAssist");
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}