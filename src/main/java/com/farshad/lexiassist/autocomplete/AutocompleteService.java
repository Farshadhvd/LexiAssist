package com.farshad.lexiassist.autocomplete;

import com.farshad.lexiassist.dictionary.autocompletemodules.PrefixDictionary;

import java.util.Collections;
import java.util.List;

public class AutocompleteService {

    private final PrefixDictionary prefixDictionary;
    private final int defaultSuggestionLimit;

    public AutocompleteService(PrefixDictionary prefixDictionary, int defaultSuggestionLimit) {
        if (prefixDictionary == null) {
            throw new IllegalArgumentException("Prefix dictionary cannot be null");
        }

        if (defaultSuggestionLimit <= 0) {
            throw new IllegalArgumentException("Default suggestion limit must be positive");
        }

        this.prefixDictionary = prefixDictionary;
        this.defaultSuggestionLimit = defaultSuggestionLimit;
    }

    public List<String> suggest(String prefix) {
        return suggest(prefix, defaultSuggestionLimit);
    }

    public List<String> suggest(String prefix, int limit) {
        if (prefix == null || prefix.isBlank() || limit <= 0) {
            return Collections.emptyList();
        }

        return prefixDictionary.suggestCompletions(prefix.trim(), limit);
    }
}