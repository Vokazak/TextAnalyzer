package ru.vokazak.exceptions;

public class TextAnalyzerException extends Exception {
    public TextAnalyzerException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public TextAnalyzerException(String message) {
        super(message);
    }

}
