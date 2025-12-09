package com.monstersandheroes.util;

/**
 * Placeholder for structured logging of in-game events.
 */
public final class GameLogger {
    private GameLogger() {
    }

    public static void log(String message) {
        System.out.println(message);
    }
}
