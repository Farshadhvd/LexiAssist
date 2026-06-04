package com.farshad.lexiassist.autocomplete;

import com.farshad.lexiassist.dictionary.autocompletelookup.PrefixDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AutocompleteServiceTest {

    private PrefixDictionary prefixDictionary;
    private AutocompleteService autocompleteService;

    @BeforeEach
    void setUp() {
        prefixDictionary = new PrefixDictionary();

        prefixDictionary.add("hello");
        prefixDictionary.add("help");
        prefixDictionary.add("helper");
        prefixDictionary.add("helpful");
        prefixDictionary.add("world");
        prefixDictionary.add("word");
        prefixDictionary.add("writer");
        prefixDictionary.add("writing");
        prefixDictionary.add("program");
        prefixDictionary.add("programmer");
        prefixDictionary.add("programming");
        prefixDictionary.add("progress");

        autocompleteService = new AutocompleteService(prefixDictionary, 5);
    }

    @Test
    void shouldSuggestWordsForPrefix() {
        List<String> suggestions = autocompleteService.suggest("hel");

        assertFalse(suggestions.isEmpty());
        assertTrue(suggestions.contains("hello"));
        assertTrue(suggestions.contains("help"));
    }

    @Test
    void shouldRespectDefaultSuggestionLimit() {
        List<String> suggestions = autocompleteService.suggest("pro");

        assertTrue(suggestions.size() <= 5);
    }

    @Test
    void shouldRespectCustomSuggestionLimit() {
        List<String> suggestions = autocompleteService.suggest("pro", 2);

        assertEquals(2, suggestions.size());
    }

    @Test
    void shouldReturnEmptyListForMissingPrefix() {
        List<String> suggestions = autocompleteService.suggest("xyz");

        assertTrue(suggestions.isEmpty());
    }

    @Test
    void shouldReturnEmptyListForInvalidPrefix() {
        assertTrue(autocompleteService.suggest(null).isEmpty());
        assertTrue(autocompleteService.suggest("").isEmpty());
        assertTrue(autocompleteService.suggest("   ").isEmpty());
    }

    @Test
    void shouldReturnEmptyListForInvalidLimit() {
        assertTrue(autocompleteService.suggest("hel", 0).isEmpty());
        assertTrue(autocompleteService.suggest("hel", -1).isEmpty());
    }

    @Test
    void shouldTrimPrefixBeforeSearching() {
        List<String> suggestions = autocompleteService.suggest("  hel  ");

        assertFalse(suggestions.isEmpty());
        assertTrue(suggestions.contains("hello"));
    }

    @Test
    void shouldRejectNullPrefixDictionary() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AutocompleteService(null, 5)
        );

        assertTrue(exception.getMessage().contains("Prefix dictionary cannot be null"));
    }

    @Test
    void shouldRejectInvalidDefaultSuggestionLimit() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AutocompleteService(prefixDictionary, 0)
        );

        assertTrue(exception.getMessage().contains("Default suggestion limit must be positive"));
    }
}