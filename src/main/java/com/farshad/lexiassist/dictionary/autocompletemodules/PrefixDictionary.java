package com.farshad.lexiassist.dictionary.autocompletemodules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class PrefixDictionary {

    private final PrefixNode root;
    private int wordCount;

    public PrefixDictionary() {
        this.root = new PrefixNode("");
        this.wordCount = 0;
    }

    public boolean add(String word) {
        if (word == null || word.isBlank()) {
            return false;
        }

        String normalizedWord = normalize(word);
        PrefixNode currentNode = root;

        for (char character : normalizedWord.toCharArray()) {
            String childWord = currentNode.getWord() + character;
            currentNode = currentNode.addChild(character, childWord);
        }

        if (currentNode.isCompleteWord()) {
            return false;
        }

        currentNode.markAsCompleteWord();
        wordCount++;
        return true;
    }

    public boolean contains(String word) {
        PrefixNode node = findNode(word);
        return node != null && node.isCompleteWord();
    }

    public List<String> suggestCompletions(String prefix, int limit) {
        List<String> completions = new ArrayList<>();

        if (prefix == null || prefix.isBlank() || limit <= 0) {
            return completions;
        }

        PrefixNode prefixNode = findNode(prefix);

        if (prefixNode == null) {
            return completions;
        }

        Queue<PrefixNode> queue = new LinkedList<>();
        queue.add(prefixNode);

        while (!queue.isEmpty() && completions.size() < limit) {
            PrefixNode currentNode = queue.remove();

            if (currentNode.isCompleteWord()) {
                completions.add(currentNode.getWord());
            }

            queue.addAll(currentNode.getChildren().values());
        }

        return completions;
    }

    public int size() {
        return wordCount;
    }

    private PrefixNode findNode(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        String normalizedText = normalize(text);
        PrefixNode currentNode = root;

        for (char character : normalizedText.toCharArray()) {
            currentNode = currentNode.getChild(character);

            if (currentNode == null) {
                return null;
            }
        }

        return currentNode;
    }

    private String normalize(String word) {
        return word.toLowerCase(Locale.ROOT);
    }
}