package com.monstersandheroes.characters.heroes;

import com.monstersandheroes.characters.attributes.Attributes;

/**
 * Specialization that favors dexterity and agility.
 */
public class Sorcerer extends Hero {
    public Sorcerer(String name) {
        this(name, 120, 10, 12, 18, 0, 0);
    }

    public Sorcerer(String name, int mana, int strength, int agility, int dexterity, int gold, int experience) {
        super(name, 1, 100, mana, new Attributes(strength, dexterity, agility));
        setGold(gold);
        setExperience(experience);
    }

    @Override
    public void applyLevelUpBenefits() {
        // All stats +5%, favored (dexterity, agility) +extra 5%.
        int strength = getAttributes().getStrength();
        int dexterity = getAttributes().getDexterity();
        int agility = getAttributes().getAgility();

        strength = (int) Math.round(strength * 1.05);
        dexterity = (int) Math.round(dexterity * 1.05);
        agility = (int) Math.round(agility * 1.05);

        dexterity = (int) Math.round(dexterity * 1.05);
        agility = (int) Math.round(agility * 1.05);

        getAttributes().setStrength(strength);
        getAttributes().setDexterity(dexterity);
        getAttributes().setAgility(agility);
    }
}
