package com.farshad.lexiassist.dictionary.autocompletemodules;

import java.util.HashMap;
import java.util.Map;

class PrefixNode {

    private final Map<Character, PrefixNode> children;
    private String word;
    private boolean completeWord;

    PrefixNode(String word) {
        this.children = new HashMap<>();
        this.word = word;
        this.completeWord = false;
    }

    Map<Character, PrefixNode> getChildren() {
        return children;
    }

    PrefixNode getChild(char character) {
        return children.get(character);
    }

    PrefixNode addChild(char character, String childWord) {
        PrefixNode childNode = children.get(character);

        if (childNode == null) {
            childNode = new PrefixNode(childWord);
            children.put(character, childNode);
        }

        return childNode;
    }

    String getWord() {
        return word;
    }

    boolean isCompleteWord() {
        return completeWord;
    }

    void markAsCompleteWord() {
        completeWord = true;
    }
}