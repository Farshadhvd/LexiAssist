package com.farshad.lexiassist.ui;

import com.farshad.lexiassist.io.TextFileLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class EditorController {

    private static final String SUGGESTION_PLACEHOLDER = "Start typing to see suggestions";

    @FXML
    private TextArea editorTextArea;

    @FXML
    private Button loadTextButton;

    @FXML
    private Button clearButton;

    @FXML
    private CheckBox spellingSuggestionsCheckBox;

    @FXML
    private CheckBox autoCompleteCheckBox;

    @FXML
    private ListView<String> suggestionsListView;

    @FXML
    private Label statusLabel;

    private final TextFileLoader textFileLoader = new TextFileLoader();

    @FXML
    private void initialize() {
        statusLabel.setText("Ready");
        resetSuggestions();

        loadTextButton.setOnAction(event -> loadTextFile());
        clearButton.setOnAction(event -> clearEditor());
    }

    private void loadTextFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        Window window = editorTextArea.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile == null) {
            statusLabel.setText("No file selected");
            return;
        }

        String fileContent = textFileLoader.loadText(selectedFile.toPath());
        editorTextArea.setText(fileContent);

        resetSuggestions();
        statusLabel.setText("Loaded: " + selectedFile.getName());
    }

    private void clearEditor() {
        editorTextArea.clear();
        resetSuggestions();
        statusLabel.setText("Editor cleared");
    }

    private void resetSuggestions() {
        suggestionsListView.getItems().setAll(SUGGESTION_PLACEHOLDER);
    }
}