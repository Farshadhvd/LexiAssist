package com.farshad.lexiassist.dictionary.allwordsrepo;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class HashWordRepository implements WordRepository {

    private final Set<String> words;

    public HashWordRepository() {
        this.words = new HashSet<>();
    }

    @Override
    public boolean add(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }

        return words.add(normalize(word));
    }

    @Override
    public boolean contains(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }

        return words.contains(normalize(word));
    }

    @Override
    public int size() {
        return words.size();
    }

    private String normalize(String word) {
        return word.toLowerCase(Locale.ROOT);
    }
}