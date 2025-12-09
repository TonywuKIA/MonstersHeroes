package com.monstersandheroes.characters;

/**
 * Defines the behavior shared by active combatants.
 */
public interface Combatant {
    String getName();

    int getLevel();

    boolean isAlive();

    void takeTurn();
}
