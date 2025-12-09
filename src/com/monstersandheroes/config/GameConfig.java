package com.monstersandheroes.config;

/**
 * Central location for tunable configuration values.
 */
public final class GameConfig {
    public static final int DEFAULT_MAP_SIZE = 8;
    public static final double DEFAULT_INACCESSIBLE_RATIO = 0.2;
    public static final double DEFAULT_MARKET_RATIO = 0.3;
    public static final int MAX_PARTY_SIZE = 3;
    public static final int MIN_PARTY_SIZE = 1;

    private GameConfig() {
    }
}
