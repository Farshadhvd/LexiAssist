package com.farshad.lexiassist.dictionary.autocomplete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrefixDictionaryTest {

    private PrefixDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new PrefixDictionary();
    }

    @Test
    void newDictionaryShouldBeEmpty() {
        assertEquals(0, dictionary.size());
    }

    @Test
    void shouldAddWord() {
        boolean added = dictionary.add("hello");

        assertTrue(added);
        assertTrue(dictionary.contains("hello"));
        assertEquals(1, dictionary.size());
    }

    @Test
    void shouldFindWordsWithoutMatchingCase() {
        dictionary.add("Hello");

        assertTrue(dictionary.contains("hello"));
        assertTrue(dictionary.contains("HELLO"));
        assertTrue(dictionary.contains("HeLlO"));
    }

    @Test
    void shouldNotCountDuplicateWordsWithDifferentCase() {
        assertTrue(dictionary.add("Hello"));
        assertFalse(dictionary.add("hello"));
        assertFalse(dictionary.add("HELLO"));

        assertEquals(1, dictionary.size());
    }

    @Test
    void shouldReturnFalseForPrefixThatIsNotCompleteWord() {
        dictionary.add("hello");

        assertFalse(dictionary.contains("hell"));
        assertTrue(dictionary.contains("hello"));
    }

    @Test
    void shouldSuggestCompletionsForPrefix() {
        dictionary.add("help");
        dictionary.add("hello");
        dictionary.add("helmet");
        dictionary.add("world");

        List<String> suggestions = dictionary.suggestCompletions("hel", 5);

        assertEquals(3, suggestions.size());
        assertTrue(suggestions.contains("help"));
        assertTrue(suggestions.contains("hello"));
        assertTrue(suggestions.contains("helmet"));
        assertFalse(suggestions.contains("world"));
    }

    @Test
    void shouldRespectSuggestionLimit() {
        dictionary.add("program");
        dictionary.add("programmer");
        dictionary.add("programming");
        dictionary.add("progress");

        List<String> suggestions = dictionary.suggestCompletions("pro", 2);

        assertEquals(2, suggestions.size());
    }

    @Test
    void shouldReturnEmptyListForMissingPrefix() {
        dictionary.add("hello");

        List<String> suggestions = dictionary.suggestCompletions("xyz", 5);

        assertTrue(suggestions.isEmpty());
    }

    @Test
    void shouldReturnEmptyListForInvalidPrefix() {
        dictionary.add("hello");

        assertTrue(dictionary.suggestCompletions(null, 5).isEmpty());
        assertTrue(dictionary.suggestCompletions("", 5).isEmpty());
        assertTrue(dictionary.suggestCompletions("   ", 5).isEmpty());
    }

    @Test
    void shouldReturnEmptyListForInvalidLimit() {
        dictionary.add("hello");

        assertTrue(dictionary.suggestCompletions("he", 0).isEmpty());
        assertTrue(dictionary.suggestCompletions("he", -1).isEmpty());
    }
}