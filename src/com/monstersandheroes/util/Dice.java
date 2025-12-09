package com.monstersandheroes.util;

import java.util.Random;

/**
 * Helper for dice-based randomness.
 */
public class Dice {
    private final Random random = new Random();

    public int roll(int maxInclusive) {
        return random.nextInt(maxInclusive) + 1;
    }

    public boolean chance(double probability) {
        return random.nextDouble() < probability;
    }
}
