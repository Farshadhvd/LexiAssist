package com.farshad.lexiassist.ui;

import com.farshad.lexiassist.autocomplete.AutocompleteService;
import com.farshad.lexiassist.dictionary.DictionaryBundle;
import com.farshad.lexiassist.dictionary.DictionaryDataLoader;
import com.farshad.lexiassist.io.TextFileLoader;
import com.farshad.lexiassist.spelling.SpellingSuggestionService;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.io.File;
import java.util.List;

public class EditorController {

    private static final String SUGGESTION_PLACEHOLDER = "Start typing to see suggestions";
    private static final String DICTIONARY_RESOURCE = "/words-small.txt";
    private static final int SUGGESTION_LIMIT = 8;
    private static final int MIN_WORD_LENGTH_FOR_SUGGESTIONS = 2;
    private static final double POPUP_WIDTH = 240;
    private static final double POPUP_MAX_HEIGHT = 180;

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
    private SpellingSuggestionService spellingSuggestionService;

    private Popup suggestionPopup;
    private ListView<String> popupSuggestionListView;

    @FXML
    private void initialize() {
        statusLabel.setText("Ready");
        resetSuggestions();
        initializeDictionary();
        initializeSuggestionPopup();

        loadTextButton.setOnAction(event -> loadTextFile());
        clearButton.setOnAction(event -> clearEditor());

        editorTextArea.textProperty().addListener((observable, oldText, newText) -> updateSuggestions());
        editorTextArea.caretPositionProperty().addListener((observable, oldPosition, newPosition) -> updateSuggestions());

        autoCompleteCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateSuggestions());
        spellingSuggestionsCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateSuggestions());

        editorTextArea.addEventFilter(KeyEvent.KEY_PRESSED, this::handleEditorKeyPress);
    }

    private void initializeDictionary() {
        DictionaryDataLoader loader = new DictionaryDataLoader();
        DictionaryBundle dictionaryBundle = loader.loadBundleFromResource(DICTIONARY_RESOURCE);

        autocompleteService = new AutocompleteService(
                dictionaryBundle.prefixDictionary(),
                SUGGESTION_LIMIT
        );

        spellingSuggestionService = new SpellingSuggestionService(
                dictionaryBundle.wordRepository()
        );

        statusLabel.setText("Dictionary loaded: " + dictionaryBundle.prefixDictionary().size() + " words");
    }

    private void initializeSuggestionPopup() {
        popupSuggestionListView = new ListView<>();
        popupSuggestionListView.setPrefWidth(POPUP_WIDTH);
        popupSuggestionListView.setMaxHeight(POPUP_MAX_HEIGHT);
        popupSuggestionListView.getStyleClass().add("popup-suggestions-list");

        popupSuggestionListView.setOnMouseClicked(event -> {
            String selectedSuggestion = popupSuggestionListView.getSelectionModel().getSelectedItem();

            if (selectedSuggestion != null) {
                applySuggestion(selectedSuggestion);
            }
        });

        suggestionPopup = new Popup();
        suggestionPopup.setAutoHide(true);
        suggestionPopup.getContent().add(popupSuggestionListView);
    }

    private void updateSuggestions() {
        String currentWord = getCurrentWord();

        if (currentWord.length() < MIN_WORD_LENGTH_FOR_SUGGESTIONS) {
            resetSuggestions();
            hideSuggestionPopup();
            return;
        }

        if (autoCompleteCheckBox.isSelected()) {
            List<String> autocompleteSuggestions = autocompleteService.suggest(currentWord, SUGGESTION_LIMIT);

            if (!autocompleteSuggestions.isEmpty()) {
                showSuggestions("Autocomplete", autocompleteSuggestions);
                return;
            }
        }

        if (spellingSuggestionsCheckBox.isSelected()) {
            List<String> spellingSuggestions = spellingSuggestionService.suggest(currentWord, SUGGESTION_LIMIT);

            if (!spellingSuggestions.isEmpty()) {
                showSuggestions("Spelling", spellingSuggestions);
                return;
            }
        }

        suggestionsListView.getItems().setAll("No suggestions");
        hideSuggestionPopup();
    }

    private void showSuggestions(String suggestionType, List<String> suggestions) {
        suggestionsListView.getItems().setAll(suggestions);
        popupSuggestionListView.getItems().setAll(suggestions);
        popupSuggestionListView.getSelectionModel().selectFirst();

        showSuggestionPopup();

        statusLabel.setText(suggestionType + " suggestions for: " + getCurrentWord());
    }

    private void showSuggestionPopup() {
        Window window = editorTextArea.getScene().getWindow();

        if (window == null) {
            return;
        }

        Bounds editorBounds = editorTextArea.localToScreen(editorTextArea.getBoundsInLocal());

        if (editorBounds == null) {
            return;
        }

        double popupX = editorBounds.getMinX() + 24;
        double popupY = editorBounds.getMinY() + 48;

        if (!suggestionPopup.isShowing()) {
            suggestionPopup.show(window, popupX, popupY);
        } else {
            suggestionPopup.setX(popupX);
            suggestionPopup.setY(popupY);
        }
    }

    private void hideSuggestionPopup() {
        if (suggestionPopup != null && suggestionPopup.isShowing()) {
            suggestionPopup.hide();
        }
    }

    private void handleEditorKeyPress(KeyEvent event) {
        if (suggestionPopup == null || !suggestionPopup.isShowing()) {
            return;
        }

        if (event.getCode() == KeyCode.DOWN) {
            popupSuggestionListView.requestFocus();
            popupSuggestionListView.getSelectionModel().selectNext();
            event.consume();
            return;
        }

        if (event.getCode() == KeyCode.UP) {
            popupSuggestionListView.requestFocus();
            popupSuggestionListView.getSelectionModel().selectPrevious();
            event.consume();
            return;
        }

        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
            String selectedSuggestion = popupSuggestionListView.getSelectionModel().getSelectedItem();

            if (selectedSuggestion != null) {
                applySuggestion(selectedSuggestion);
                event.consume();
            }
            return;
        }

        if (event.getCode() == KeyCode.ESCAPE) {
            hideSuggestionPopup();
            event.consume();
        }
    }

    private void applySuggestion(String suggestion) {
        String text = editorTextArea.getText();
        int caretPosition = editorTextArea.getCaretPosition();

        if (text == null || text.isEmpty() || caretPosition == 0) {
            return;
        }

        int wordStart = findCurrentWordStart(text, caretPosition);
        int wordEnd = findCurrentWordEnd(text, caretPosition);

        editorTextArea.replaceText(wordStart, wordEnd, suggestion);
        editorTextArea.positionCaret(wordStart + suggestion.length());

        hideSuggestionPopup();
        suggestionsListView.getItems().setAll(suggestion);
        statusLabel.setText("Applied suggestion: " + suggestion);
        editorTextArea.requestFocus();
    }

    private String getCurrentWord() {
        String text = editorTextArea.getText();
        int caretPosition = editorTextArea.getCaretPosition();

        if (text == null || text.isEmpty() || caretPosition == 0) {
            return "";
        }

        int start = findCurrentWordStart(text, caretPosition);

        return text.substring(start, caretPosition);
    }

    private int findCurrentWordStart(String text, int caretPosition) {
        int start = caretPosition - 1;

        while (start >= 0 && Character.isLetter(text.charAt(start))) {
            start--;
        }

        return start + 1;
    }

    private int findCurrentWordEnd(String text, int caretPosition) {
        int end = caretPosition;

        while (end < text.length() && Character.isLetter(text.charAt(end))) {
            end++;
        }

        return end;
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
        hideSuggestionPopup();
        statusLabel.setText("Loaded: " + selectedFile.getName());
    }

    private void clearEditor() {
        editorTextArea.clear();
        resetSuggestions();
        hideSuggestionPopup();
        statusLabel.setText("Editor cleared");
    }

    private void resetSuggestions() {
        suggestionsListView.getItems().setAll(SUGGESTION_PLACEHOLDER);
    }
}