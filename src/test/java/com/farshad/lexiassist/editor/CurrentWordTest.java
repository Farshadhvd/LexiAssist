package com.farshad.lexiassist.editor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrentWordTest {

    @Test
    void shouldReturnEmptyValueForEmptyText() {
        CurrentWord currentWord = new CurrentWord("", 0);

        assertEquals("", currentWord.value());
        assertEquals(0, currentWord.startIndex());
        assertEquals(0, currentWord.endIndex());
    }

    @Test
    void shouldReturnEmptyValueWhenCaretIsAtBeginning() {
        CurrentWord currentWord = new CurrentWord("hello", 0);

        assertEquals("", currentWord.value());
        assertEquals(0, currentWord.startIndex());
        assertEquals(5, currentWord.endIndex());
    }

    @Test
    void shouldFindCurrentWordAtEndOfText() {
        CurrentWord currentWord = new CurrentWord("hello world", 11);

        assertEquals("world", currentWord.value());
        assertEquals(6, currentWord.startIndex());
        assertEquals(11, currentWord.endIndex());
    }

    @Test
    void shouldFindCurrentWordAfterSpace() {
        CurrentWord currentWord = new CurrentWord("hello wor", 9);

        assertEquals("wor", currentWord.value());
        assertEquals(6, currentWord.startIndex());
        assertEquals(9, currentWord.endIndex());
    }

    @Test
    void shouldFindCurrentWordAfterPunctuation() {
        CurrentWord currentWord = new CurrentWord("hello, wor", 10);

        assertEquals("wor", currentWord.value());
        assertEquals(7, currentWord.startIndex());
        assertEquals(10, currentWord.endIndex());
    }

    @Test
    void shouldFindCurrentWordWhenCaretIsInsideWord() {
        CurrentWord currentWord = new CurrentWord("hello programming world", 9);

        assertEquals("pro", currentWord.value());
        assertEquals(6, currentWord.startIndex());
        assertEquals(17, currentWord.endIndex());
    }

    @Test
    void shouldFindCurrentWordStartAfterNewLine() {
        CurrentWord currentWord = new CurrentWord("hello\nprogram", 13);

        assertEquals("program", currentWord.value());
        assertEquals(6, currentWord.startIndex());
        assertEquals(13, currentWord.endIndex());
    }

    @Test
    void shouldReturnTextFromLineStartToCaret() {
        CurrentWord currentWord = new CurrentWord("hello\nI am learning pro", 23);

        assertEquals("I am learning pro", currentWord.textFromLineStartToCaret());
    }

    @Test
    void shouldNormalizeNegativeCaretPosition() {
        CurrentWord currentWord = new CurrentWord("hello", -5);

        assertEquals("", currentWord.value());
        assertEquals(0, currentWord.startIndex());
        assertEquals(5, currentWord.endIndex());
    }

    @Test
    void shouldNormalizeCaretPositionBeyondTextLength() {
        CurrentWord currentWord = new CurrentWord("hello", 50);

        assertEquals("hello", currentWord.value());
        assertEquals(0, currentWord.startIndex());
        assertEquals(5, currentWord.endIndex());
    }

    @Test
    void shouldHandleNullTextAsEmptyText() {
        CurrentWord currentWord = new CurrentWord(null, 3);

        assertEquals("", currentWord.value());
        assertEquals(0, currentWord.startIndex());
        assertEquals(0, currentWord.endIndex());
    }
}