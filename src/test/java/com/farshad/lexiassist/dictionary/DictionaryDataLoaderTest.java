package com.farshad.lexiassist.dictionary;

import com.farshad.lexiassist.dictionary.autocompletelookup.PrefixDictionary;
import com.farshad.lexiassist.dictionary.exactwordlookup.HashWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryDataLoaderTest {

    private DictionaryDataLoader loader;

    @BeforeEach
    void setUp() {
        loader = new DictionaryDataLoader();
    }

    @Test
    void shouldLoadWordsFromInputStream() throws IOException {
        String content = """
                hello
                world
                java
                """;

        HashWordRepository repository = new HashWordRepository();

        loader.loadWords(
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                repository
        );

        assertEquals(3, repository.size());
        assertTrue(repository.contains("hello"));
        assertTrue(repository.contains("world"));
        assertTrue(repository.contains("java"));
    }

    @Test
    void shouldTrimWordsWhileLoading() throws IOException {
        String content = """
                   hello
                world   
                """;

        HashWordRepository repository = new HashWordRepository();

        loader.loadWords(
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                repository
        );

        assertTrue(repository.contains("hello"));
        assertTrue(repository.contains("world"));
        assertEquals(2, repository.size());
    }

    @Test
    void shouldIgnoreBlankLinesBecauseRepositoryRejectsBlankWords() throws IOException {
        String content = """
                hello
                
                   
                world
                """;

        HashWordRepository repository = new HashWordRepository();

        loader.loadWords(
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                repository
        );

        assertEquals(2, repository.size());
        assertTrue(repository.contains("hello"));
        assertTrue(repository.contains("world"));
    }

    @Test
    void shouldAvoidDuplicateWordsWhileLoading() throws IOException {
        String content = """
                hello
                Hello
                HELLO
                """;

        HashWordRepository repository = new HashWordRepository();

        loader.loadWords(
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                repository
        );

        assertEquals(1, repository.size());
        assertTrue(repository.contains("hello"));
    }

    @Test
    void shouldLoadBundledSmallDictionaryResource() {
        HashWordRepository repository = loader.loadFromResource("/words-small.txt");

        assertTrue(repository.size() > 0);
        assertTrue(repository.contains("hello"));
        assertTrue(repository.contains("programming"));
        assertTrue(repository.contains("dictionary"));
    }

    @Test
    void shouldRejectMissingResource() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loader.loadFromResource("/missing-dictionary.txt")
        );

        assertTrue(exception.getMessage().contains("Dictionary resource not found"));
    }

    @Test
    void shouldRejectNullInputStream() {
        HashWordRepository repository = new HashWordRepository();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loader.loadWords(null, repository)
        );

        assertTrue(exception.getMessage().contains("Input stream cannot be null"));
    }

    @Test
    void shouldRejectNullRepository() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "hello".getBytes(StandardCharsets.UTF_8)
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> loader.loadWords(inputStream, null)
        );

        assertTrue(exception.getMessage().contains("Word repository cannot be null"));
    }

    @Test
    void shouldLoadWordsIntoHashAndPrefixDictionaries() throws IOException {
        String content = """
            hello
            help
            world
            """;

        HashWordRepository wordRepository = new HashWordRepository();
        PrefixDictionary prefixDictionary = new PrefixDictionary();

        loader.loadWords(
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)),
                wordRepository,
                prefixDictionary
        );

        assertEquals(3, wordRepository.size());
        assertEquals(3, prefixDictionary.size());

        assertTrue(wordRepository.contains("hello"));
        assertTrue(prefixDictionary.contains("hello"));
        assertTrue(prefixDictionary.suggestCompletions("he", 5).contains("help"));
    }

    @Test
    void shouldLoadDictionaryBundleFromResource() {
        DictionaryBundle dictionaryBundle = loader.loadBundleFromResource("/words-small.txt");

        assertTrue(dictionaryBundle.wordRepository().contains("hello"));
        assertTrue(dictionaryBundle.prefixDictionary().contains("hello"));
        assertFalse(dictionaryBundle.prefixDictionary().suggestCompletions("pro", 5).isEmpty());
    }
}