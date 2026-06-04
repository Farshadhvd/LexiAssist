package com.farshad.lexiassist.dictionary;

public interface WordRepository {

    boolean add(String word);

    boolean contains(String word);

    int size();
}