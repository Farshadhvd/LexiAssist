package com.farshad.lexiassist.dictionary;

import com.farshad.lexiassist.dictionary.autocompletelookup.PrefixDictionary;
import com.farshad.lexiassist.dictionary.exactwordlookup.HashWordRepository;
import com.farshad.lexiassist.dictionary.exactwordlookup.WordRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DictionaryDataLoader {

    public HashWordRepository loadFromResource(String resourcePath) {
        HashWordRepository repository = new HashWordRepository();

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Dictionary resource not found: " + resourcePath);
            }

            loadWords(inputStream, repository);
            return repository;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load dictionary resource: " + resourcePath, e);
        }
    }

    public DictionaryBundle loadBundleFromResource(String resourcePath) {
        HashWordRepository wordRepository = new HashWordRepository();
        PrefixDictionary prefixDictionary = new PrefixDictionary();

        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Dictionary resource not found: " + resourcePath);
            }

            loadWords(inputStream, wordRepository, prefixDictionary);
            return new DictionaryBundle(wordRepository, prefixDictionary);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load dictionary resource: " + resourcePath, e);
        }
    }

    public void loadWords(InputStream inputStream, WordRepository repository) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }

        if (repository == null) {
            throw new IllegalArgumentException("Word repository cannot be null");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            String line;

            while ((line = reader.readLine()) != null) {
                repository.add(line.trim());
            }
        }
    }

    public void loadWords(
            InputStream inputStream,
            WordRepository wordRepository,
            PrefixDictionary prefixDictionary
    ) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }

        if (wordRepository == null) {
            throw new IllegalArgumentException("Word repository cannot be null");
        }

        if (prefixDictionary == null) {
            throw new IllegalArgumentException("Prefix dictionary cannot be null");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        )) {
            String line;

            while ((line = reader.readLine()) != null) {
                String word = line.trim();
                wordRepository.add(word);
                prefixDictionary.add(word);
            }
        }
    }
}