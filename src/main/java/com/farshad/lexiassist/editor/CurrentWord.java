package com.farshad.lexiassist.editor;

public class CurrentWord {

    private final String text;
    private final int caretPosition;

    public CurrentWord(String text, int caretPosition) {
        this.text = text == null ? "" : text;
        this.caretPosition = normalizeCaretPosition(caretPosition, this.text.length());
    }

    public String value() {
        if (text.isEmpty() || caretPosition == 0) {
            return "";
        }

        return text.substring(startIndex(), caretPosition);
    }

    public int startIndex() {
        if (text.isEmpty() || caretPosition == 0) {
            return caretPosition;
        }

        int start = caretPosition - 1;

        while (start >= 0 && Character.isLetter(text.charAt(start))) {
            start--;
        }

        return start + 1;
    }

    public int endIndex() {
        int end = caretPosition;

        while (end < text.length() && Character.isLetter(text.charAt(end))) {
            end++;
        }

        return end;
    }

    public String textFromLineStartToCaret() {
        if (text.isEmpty() || caretPosition == 0) {
            return "";
        }

        int lineStart = caretPosition - 1;

        while (lineStart >= 0 && text.charAt(lineStart) != '\n') {
            lineStart--;
        }

        return text.substring(lineStart + 1, caretPosition);
    }

    private int normalizeCaretPosition(int requestedCaretPosition, int textLength) {
        if (requestedCaretPosition < 0) {
            return 0;
        }

        return Math.min(requestedCaretPosition, textLength);
    }
}