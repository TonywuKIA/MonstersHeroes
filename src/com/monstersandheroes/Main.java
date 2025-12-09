package com.monstersandheroes;

import com.monstersandheroes.engine.GameEngine;

/**
 * Entry point wiring the console game to the GameEngine.
 */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        GameEngine engine = new GameEngine();
        engine.start();
    }
}
