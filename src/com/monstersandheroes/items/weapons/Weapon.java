package com.monstersandheroes.items.weapons;

import com.monstersandheroes.items.Equipable;

/**
 * Weapon data used to resolve hero attacks.
 */
public class Weapon implements Equipable {
    private final String name;
    private final int price;
    private final int requiredLevel;
    private final int damage;
    private final int handsRequired;
    private final int maxUses;
    private int usesLeft;

    public Weapon(String name, int price, int requiredLevel, int damage, int handsRequired) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.damage = damage;
        this.handsRequired = handsRequired;
        this.maxUses = 3;
        this.usesLeft = maxUses;
    }

    public int getDamage() {
        return damage;
    }

    public int getHandsRequired() {
        return handsRequired;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public int getRequiredLevel() {
        return requiredLevel;
    }

    @Override
    public void equip() {
        // No direct state change; damage contribution is read when resolving attacks.
    }

    @Override
    public void unequip() {
        // No direct state change; removal is reflected by clearing the equipped weapon slot.
    }

    public boolean isBroken() {
        return usesLeft <= 0;
    }

    public boolean reduceUse() {
        if (usesLeft > 0) {
            usesLeft--;
        }
        return usesLeft <= 0;
    }

    public void repair() {
        usesLeft = maxUses;
    }

    public int getUsesLeft() {
        return usesLeft;
    }
}
