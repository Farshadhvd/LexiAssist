package com.farshad.lexiassist.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class EditorController {

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

    @FXML
    private void initialize() {
        statusLabel.setText("Ready");

        suggestionsListView.getItems().setAll(
                "Start typing to see suggestions"
        );
    }
}