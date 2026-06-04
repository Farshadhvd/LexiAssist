package com.farshad.lexiassist.ui;

import com.farshad.lexiassist.autocomplete.AutocompleteService;
import com.farshad.lexiassist.dictionary.DictionaryBundle;
import com.farshad.lexiassist.dictionary.DictionaryDataLoader;
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
import java.util.List;

public class EditorController {

    private static final String SUGGESTION_PLACEHOLDER = "Start typing to see suggestions";
    private static final String DICTIONARY_RESOURCE = "/words-small.txt";
    private static final int SUGGESTION_LIMIT = 8;

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
    private AutocompleteService autocompleteService;

    @FXML
    private void initialize() {
        statusLabel.setText("Ready");
        resetSuggestions();
        initializeDictionary();

        loadTextButton.setOnAction(event -> loadTextFile());
        clearButton.setOnAction(event -> clearEditor());

        editorTextArea.textProperty().addListener((observable, oldText, newText) -> updateAutocompleteSuggestions());
        editorTextArea.caretPositionProperty().addListener((observable, oldPosition, newPosition) -> updateAutocompleteSuggestions());

        autoCompleteCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateAutocompleteSuggestions());
    }

    private void initializeDictionary() {
        DictionaryDataLoader loader = new DictionaryDataLoader();
        DictionaryBundle dictionaryBundle = loader.loadBundleFromResource(DICTIONARY_RESOURCE);

        autocompleteService = new AutocompleteService(
                dictionaryBundle.prefixDictionary(),
                SUGGESTION_LIMIT
        );

        statusLabel.setText("Dictionary loaded: " + dictionaryBundle.prefixDictionary().size() + " words");
    }

    private void updateAutocompleteSuggestions() {
        if (autocompleteService == null || !autoCompleteCheckBox.isSelected()) {
            resetSuggestions();
            return;
        }

        String currentWord = getCurrentWord();

        if (currentWord.length() < 2) {
            resetSuggestions();
            return;
        }

        List<String> suggestions = autocompleteService.suggest(currentWord, SUGGESTION_LIMIT);

        if (suggestions.isEmpty()) {
            suggestionsListView.getItems().setAll("No autocomplete suggestions");
        } else {
            suggestionsListView.getItems().setAll(suggestions);
        }
    }

    private String getCurrentWord() {
        String text = editorTextArea.getText();
        int caretPosition = editorTextArea.getCaretPosition();

        if (text == null || text.isEmpty() || caretPosition == 0) {
            return "";
        }

        int start = caretPosition - 1;

        while (start >= 0 && Character.isLetter(text.charAt(start))) {
            start--;
        }

        return text.substring(start + 1, caretPosition);
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