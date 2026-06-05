package com.farshad.lexiassist.spelling;

import com.farshad.lexiassist.dictionary.exactwordlookup.HashWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpellingSuggestionServiceTest {

    private HashWordRepository wordRepository;
    private SpellingSuggestionService spellingSuggestionService;

    @BeforeEach
    void setUp() {
        wordRepository = new HashWordRepository();

        wordRepository.add("hello");
        wordRepository.add("help");
        wordRepository.add("hell");
        wordRepository.add("held");
        wordRepository.add("word");
        wordRepository.add("world");
        wordRepository.add("receive");
        wordRepository.add("recipe");
        wordRepository.add("recite");
        wordRepository.add("program");
        wordRepository.add("progress");
        wordRepository.add("receive");

        spellingSuggestionService = new SpellingSuggestionService(wordRepository);
    }

    @Test
    void shouldGenerateDeletionCandidates() {
        List<String> candidates = spellingSuggestionService.generateOneEditCandidates("hellp", true);

        assertTrue(candidates.contains("hell"));
        assertTrue(candidates.contains("help"));
    }

    @Test
    void shouldGenerateInsertionCandidates() {
        List<String> candidates = spellingSuggestionService.generateOneEditCandidates("helo", true);

        assertTrue(candidates.contains("hello"));
    }

    @Test
    void shouldGenerateSubstitutionCandidates() {
        List<String> candidates = spellingSuggestionService.generateOneEditCandidates("wurld", true);

        assertTrue(candidates.contains("world"));
    }

    @Test
    void shouldGenerateAdjacentSwapCandidates() {
        List<String> candidates = spellingSuggestionService.generateOneEditCandidates("recieve", true);

        assertTrue(candidates.contains("receive"));
    }

    @Test
    void shouldReturnOnlyDictionaryWordsWhenRequested() {
        List<String> candidates = spellingSuggestionService.generateOneEditCandidates("helo", true);

        assertTrue(candidates.contains("hello"));
        assertFalse(candidates.contains("ahelo"));
    }

    @Test
    void shouldGenerateNonDictionaryCandidatesWhenValidWordsAreNotRequired() {
        List<String> candidates = spellingSuggestionService.generateOneEditCandidates("hi", false);

        assertFalse(candidates.isEmpty());
        assertTrue(candidates.contains("h"));
        assertTrue(candidates.contains("ahi"));
    }

    @Test
    void shouldSuggestCorrectionsForMisspelledWord() {
        List<String> suggestions = spellingSuggestionService.suggest("wurld", 5);

        assertTrue(suggestions.contains("world"));
    }

    @Test
    void shouldRespectSuggestionLimit() {
        List<String> suggestions = spellingSuggestionService.suggest("hel", 2);

        assertTrue(suggestions.size() <= 2);
    }

    @Test
    void shouldReturnEmptyListForCorrectWord() {
        List<String> suggestions = spellingSuggestionService.suggest("hello", 5);

        assertTrue(suggestions.isEmpty());
    }

    @Test
    void shouldSuggestCorrectionForAdjacentSwapTypo() {
        List<String> suggestions = spellingSuggestionService.suggest("recieve", 5);

        assertTrue(suggestions.contains("receive"));
    }

    @Test
    void shouldReturnEmptyListForInvalidInput() {
        assertTrue(spellingSuggestionService.suggest(null, 5).isEmpty());
        assertTrue(spellingSuggestionService.suggest("", 5).isEmpty());
        assertTrue(spellingSuggestionService.suggest("   ", 5).isEmpty());
    }

    @Test
    void shouldReturnEmptyListForInvalidLimit() {
        assertTrue(spellingSuggestionService.suggest("wurld", 0).isEmpty());
        assertTrue(spellingSuggestionService.suggest("wurld", -1).isEmpty());
    }

    @Test
    void shouldHandleCaseInsensitiveInput() {
        List<String> suggestions = spellingSuggestionService.suggest("WURLD", 5);

        assertTrue(suggestions.contains("world"));
    }

    @Test
    void shouldRejectNullWordRepository() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new SpellingSuggestionService(null)
        );

        assertTrue(exception.getMessage().contains("Word repository cannot be null"));
    }
}