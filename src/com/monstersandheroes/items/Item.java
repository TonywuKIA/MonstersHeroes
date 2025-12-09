package com.monstersandheroes.items;

/**
 * Base contract for any marketable item.
 */
public interface Item {
    String getName();

    int getPrice();

    int getRequiredLevel();
}
