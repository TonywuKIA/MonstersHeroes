package com.monstersandheroes.items;

/**
 * Represents single or multi use items triggered by the player.
 */
public interface Usable extends Item {
    void use();

    boolean isConsumed();
}
