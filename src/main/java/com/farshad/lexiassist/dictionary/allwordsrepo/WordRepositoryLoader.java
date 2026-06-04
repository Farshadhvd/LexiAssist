package com.farshad.lexiassist.dictionary.allwordsrepo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class WordRepositoryLoader {

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
}