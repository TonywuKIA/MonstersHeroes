package com.monstersandheroes.characters.monsters;

/**
 * Monster specialization with increased dodge.
 */
public class Spirit extends Monster {
    public Spirit(String name, int level, int baseDamage, int defense, double dodgeChance) {
        super(name, level, level * 100, baseDamage, defense, dodgeChance);
    }
}
