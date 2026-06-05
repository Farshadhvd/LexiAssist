package com.farshad.lexiassist.spelling;

import com.farshad.lexiassist.dictionary.exactwordlookup.WordRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SpellingSuggestionService {

    private static final int SEARCH_LIMIT = 1000;
    private static final char FIRST_LETTER = 'a';
    private static final char LAST_LETTER = 'z';

    private final WordRepository wordRepository;

    public SpellingSuggestionService(WordRepository wordRepository) {
        if (wordRepository == null) {
            throw new IllegalArgumentException("Word repository cannot be null");
        }

        this.wordRepository = wordRepository;
    }

    public List<String> suggest(String word, int limit) {
        List<String> suggestions = new LinkedList<>();

        if (word == null || word.isBlank() || limit <= 0) {
            return suggestions;
        }

        String normalizedWord = normalize(word);

        if (wordRepository.contains(normalizedWord)) {
            return suggestions;
        }

        LinkedList<String> wordsToExplore = new LinkedList<>();
        Set<String> visitedWords = new HashSet<>();

        wordsToExplore.add(normalizedWord);
        visitedWords.add(normalizedWord);

        while (!wordsToExplore.isEmpty()
                && suggestions.size() < limit
                && visitedWords.size() < SEARCH_LIMIT) {

            String currentWord = wordsToExplore.removeFirst();
            List<String> nearbyWords = generateOneEditCandidates(currentWord, true);

            for (String candidate : nearbyWords) {
                if (visitedWords.add(candidate)) {
                    wordsToExplore.addLast(candidate);
                    suggestions.add(candidate);

                    if (suggestions.size() == limit) {
                        break;
                    }
                }
            }
        }

        return suggestions;
    }

    public List<String> generateOneEditCandidates(String word, boolean onlyValidWords) {
        List<String> candidates = new ArrayList<>();

        if (word == null || word.isBlank()) {
            return candidates;
        }

        String normalizedWord = normalize(word);

        addDeletionCandidates(normalizedWord, candidates, onlyValidWords);
        addInsertionCandidates(normalizedWord, candidates, onlyValidWords);
        addSubstitutionCandidates(normalizedWord, candidates, onlyValidWords);
        addAdjacentSwapCandidates(normalizedWord, candidates, onlyValidWords);

        return candidates;
    }

    void addDeletionCandidates(String word, List<String> candidates, boolean onlyValidWords) {
        for (int index = 0; index < word.length(); index++) {
            StringBuilder builder = new StringBuilder(word);
            builder.deleteCharAt(index);

            addCandidate(word, builder.toString(), candidates, onlyValidWords);
        }
    }

    void addInsertionCandidates(String word, List<String> candidates, boolean onlyValidWords) {
        for (int index = 0; index <= word.length(); index++) {
            for (char letter = FIRST_LETTER; letter <= LAST_LETTER; letter++) {
                StringBuilder builder = new StringBuilder(word);
                builder.insert(index, letter);

                addCandidate(word, builder.toString(), candidates, onlyValidWords);
            }
        }
    }

    void addSubstitutionCandidates(String word, List<String> candidates, boolean onlyValidWords) {
        for (int index = 0; index < word.length(); index++) {
            for (char letter = FIRST_LETTER; letter <= LAST_LETTER; letter++) {
                StringBuilder builder = new StringBuilder(word);
                builder.setCharAt(index, letter);

                addCandidate(word, builder.toString(), candidates, onlyValidWords);
            }
        }
    }
    void addAdjacentSwapCandidates(String word, List<String> candidates, boolean onlyValidWords) {
        for (int index = 0; index < word.length() - 1; index++) {
            StringBuilder builder = new StringBuilder(word);

            char currentLetter = builder.charAt(index);
            char nextLetter = builder.charAt(index + 1);

            builder.setCharAt(index, nextLetter);
            builder.setCharAt(index + 1, currentLetter);

            addCandidate(word, builder.toString(), candidates, onlyValidWords);
        }
    }


    private void addCandidate(
            String originalWord,
            String candidate,
            List<String> candidates,
            boolean onlyValidWords
    ) {
        if (candidate.equals(originalWord)) {
            return;
        }

        if (candidates.contains(candidate)) {
            return;
        }

        if (onlyValidWords && !wordRepository.contains(candidate)) {
            return;
        }

        candidates.add(candidate);
    }

    private String normalize(String word) {
        return word.toLowerCase(Locale.ROOT);
    }
}