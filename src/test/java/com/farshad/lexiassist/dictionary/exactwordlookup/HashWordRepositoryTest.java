package com.farshad.lexiassist.dictionary.exactwordlookup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashWordRepositoryTest {

    private HashWordRepository repository;

    @BeforeEach
    void setUp() {
        repository = new HashWordRepository();
    }

    @Test
    void newRepositoryShouldBeEmpty() {
        assertEquals(0, repository.size());
    }

    @Test
    void shouldAddNewWord() {
        boolean added = repository.add("hello");

        assertTrue(added);
        assertTrue(repository.contains("hello"));
        assertEquals(1, repository.size());
    }

    @Test
    void shouldFindWordsWithoutMatchingCase() {
        repository.add("Hello");

        assertTrue(repository.contains("hello"));
        assertTrue(repository.contains("HELLO"));
        assertTrue(repository.contains("HeLlO"));
    }

    @Test
    void shouldNotCountDuplicateWordsWithDifferentCase() {
        assertTrue(repository.add("Hello"));
        assertFalse(repository.add("hello"));
        assertFalse(repository.add("HELLO"));

        assertEquals(1, repository.size());
    }

    @Test
    void shouldReturnFalseForMissingWord() {
        repository.add("hello");

        assertFalse(repository.contains("world"));
    }

    @Test
    void shouldIgnoreNullWords() {
        assertFalse(repository.add(null));
        assertFalse(repository.contains(null));
        assertEquals(0, repository.size());
    }

    @Test
    void shouldIgnoreBlankWords() {
        assertFalse(repository.add(""));
        assertFalse(repository.add("   "));
        assertFalse(repository.contains(""));
        assertFalse(repository.contains("   "));

        assertEquals(0, repository.size());
    }
}