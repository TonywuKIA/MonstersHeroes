package com.monstersandheroes.characters.monsters;

/**
 * Monster specialization with increased base damage.
 */
public class Dragon extends Monster {
    public Dragon(String name, int level, int baseDamage, int defense, double dodgeChance) {
        super(name, level, level * 100, baseDamage, defense, dodgeChance);
    }
}
