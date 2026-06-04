package com.farshad.lexiassist.dictionary;

import com.farshad.lexiassist.dictionary.autocompletelookup.PrefixDictionary;
import com.farshad.lexiassist.dictionary.exactwordlookup.HashWordRepository;

public record DictionaryBundle(HashWordRepository wordRepository, PrefixDictionary prefixDictionary) {

    public DictionaryBundle {
        if (wordRepository == null) {
            throw new IllegalArgumentException("Word repository cannot be null");
        }

        if (prefixDictionary == null) {
            throw new IllegalArgumentException("Prefix dictionary cannot be null");
        }

    }
}