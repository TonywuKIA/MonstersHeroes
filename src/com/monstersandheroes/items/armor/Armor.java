package com.monstersandheroes.items.armor;

import com.monstersandheroes.items.Equipable;

/**
 * Defensive equipment that reduces incoming damage.
 */
public class Armor implements Equipable {
    private final String name;
    private final int price;
    private final int requiredLevel;
    private final int damageReduction;
    private final int maxUses;
    private int usesLeft;

    public Armor(String name, int price, int requiredLevel, int damageReduction) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.damageReduction = damageReduction;
        this.maxUses = 3;
        this.usesLeft = maxUses;
    }

    public int getDamageReduction() {
        return damageReduction;
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
        // No direct state change; removal/add handled by inventory interactions.
    }

    @Override
    public void unequip() {
        // No direct state change; removal/add handled by inventory interactions.
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
