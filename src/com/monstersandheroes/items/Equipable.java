package com.monstersandheroes.items;

/**
 * Represents equippable gear such as weapons and armor.
 */
public interface Equipable extends Item {
    void equip();

    void unequip();
}
