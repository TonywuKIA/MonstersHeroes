package com.monstersandheroes.items.spells;

import com.monstersandheroes.items.Usable;
import com.monstersandheroes.characters.monsters.Monster;
import com.monstersandheroes.characters.heroes.Hero;
import com.monstersandheroes.engine.Battle;

/**
 * Spell definition used during battles.
 */
public class Spell implements Usable {
    private final String name;
    private final int price;
    private final int requiredLevel;
    private final int baseDamage;
    private final int manaCost;
    private final SpellType spellType;
    private boolean consumed;

    public Spell(String name, int price, int requiredLevel, int baseDamage, int manaCost, SpellType spellType) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.baseDamage = baseDamage;
        this.manaCost = manaCost;
        this.spellType = spellType;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getManaCost() {
        return manaCost;
    }

    public SpellType getSpellType() {
        return spellType;
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
    public void use() {
        // This overload requires hero/target context; prefer use(Hero, Monster).
        consumed = true;
    }

    /**
     * Apply this spell's effect to a target monster, deducting mana from the casting hero.
     */
    public int use(Hero caster, Monster target) {
        if (caster.getMana() < manaCost) {
            return 0;
        }
        caster.setMana(caster.getMana() - manaCost);
        int dexterity = caster.getAttributes().getDexterity();
        int damage = (int) (baseDamage + dexterity * 0.05);
        // Additional debuffs are handled by Battle; this method only computes raw damage.
        consumed = true;
        return damage;
    }

    public void markConsumed() {
        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }
}
