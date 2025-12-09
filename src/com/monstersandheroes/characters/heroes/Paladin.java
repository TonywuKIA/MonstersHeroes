package com.monstersandheroes.characters.heroes;

import com.monstersandheroes.characters.attributes.Attributes;

/**
 * Specialization that favors strength and dexterity.
 */
public class Paladin extends Hero {
    public Paladin(String name) {
        this(name, 110, 14, 10, 14, 0, 0);
    }

    public Paladin(String name, int mana, int strength, int agility, int dexterity, int gold, int experience) {
        super(name, 1, 100, mana, new Attributes(strength, dexterity, agility));
        setGold(gold);
        setExperience(experience);
    }

    @Override
    public void applyLevelUpBenefits() {
        // All stats +5%, favored (strength, dexterity) +extra 5%.
        int strength = getAttributes().getStrength();
        int dexterity = getAttributes().getDexterity();
        int agility = getAttributes().getAgility();

        strength = (int) Math.round(strength * 1.05);
        dexterity = (int) Math.round(dexterity * 1.05);
        agility = (int) Math.round(agility * 1.05);

        strength = (int) Math.round(strength * 1.05);
        dexterity = (int) Math.round(dexterity * 1.05);

        getAttributes().setStrength(strength);
        getAttributes().setDexterity(dexterity);
        getAttributes().setAgility(agility);
    }
}
