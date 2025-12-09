package com.monstersandheroes.characters.heroes;

import com.monstersandheroes.characters.Combatant;
import com.monstersandheroes.characters.LivingEntity;
import com.monstersandheroes.characters.attributes.Attributes;
import com.monstersandheroes.inventory.Inventory;

/**
 * Base abstraction for all hero archetypes.
 */
public abstract class Hero extends LivingEntity implements Combatant {
    private int mana;
    private int experience;
    private int gold;
    private final Inventory inventory = new Inventory();

    protected Hero(String name, int level, int hitPoints, int mana, Attributes attributes) {
        super(name, level, hitPoints, attributes);
        this.mana = mana;
        this.gold = 0;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience(int amount) {
        experience += amount;
        checkLevelUp();
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGold() {
        return gold;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public abstract void applyLevelUpBenefits();

    private void checkLevelUp() {
        while (experience >= getLevel() * 10) {
            experience -= getLevel() * 10;
            setLevel(getLevel() + 1);
            onLevelUp();
        }
    }

    protected void onLevelUp() {
        setHitPoints(getLevel() * 100);
        setMana((int) (getMana() * 1.1));
        applyLevelUpBenefits();
    }

    @Override
    public void takeTurn() {
        // Battle loop controls hero actions; takeTurn is unused in this version.
    }
}
