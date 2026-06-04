package com.farshad.lexiassist.dictionary.allwordsrepo;

public interface WordRepository {

    boolean add(String word);

    boolean contains(String word);

    int size();
}