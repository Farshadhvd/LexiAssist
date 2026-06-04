package com.farshad.lexiassist.dictionary.exactwordlookup;

public interface WordRepository {

    boolean add(String word);

    boolean contains(String word);

    int size();
}