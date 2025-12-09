package com.monstersandheroes.characters.monsters;

import com.monstersandheroes.characters.Combatant;
import com.monstersandheroes.characters.LivingEntity;
import com.monstersandheroes.characters.attributes.Attributes;

/**
 * Base type for all monsters encountered in the world.
 */
public abstract class Monster extends LivingEntity implements Combatant {
    private final int baseDamage;
    private final int defense;
    private final double dodgeChance;

    protected Monster(String name, int level, int hitPoints, int baseDamage, int defense, double dodgeChance) {
        super(name, level, hitPoints, new Attributes(0, 0, 0));
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getDefense() {
        return defense;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

    @Override
    public void takeTurn() {
        // Battle loop controls monster actions; takeTurn is unused in this version.
    }
}
