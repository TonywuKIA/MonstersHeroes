package com.monstersandheroes.data;

/**
 * Unchecked wrapper for IO issues while reading data files.
 */
public class DataLoadingException extends RuntimeException {
    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
