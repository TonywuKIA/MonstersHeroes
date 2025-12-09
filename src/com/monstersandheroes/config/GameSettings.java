package com.monstersandheroes.config;

/**
 * Mutable settings for a single game session.
 */
public class GameSettings {
    private int mapSize = GameConfig.DEFAULT_MAP_SIZE;
    private int partySize = GameConfig.MIN_PARTY_SIZE;

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }
}
