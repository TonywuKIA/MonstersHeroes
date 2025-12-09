package com.monstersandheroes.characters.attributes;

/**
 * Value object for storing hero or monster core attributes.
 */
public class Attributes {
    private int strength;
    private int dexterity;
    private int agility;
    private int defense;

    public Attributes(int strength, int dexterity, int agility) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.defense = 0;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
}
