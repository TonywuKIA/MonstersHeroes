package com.monstersandheroes.inventory;

import com.monstersandheroes.items.armor.Armor;
import com.monstersandheroes.items.weapons.Weapon;

/**
 * Tracks currently equipped gear for a hero.
 */
public class EquipmentSet {
    private Weapon mainHand;
    private Weapon offHand;
    private Armor armor;

    public Weapon getMainHand() {
        return mainHand;
    }

    public void setMainHand(Weapon mainHand) {
        this.mainHand = mainHand;
    }

    public Weapon getOffHand() {
        return offHand;
    }

    public void setOffHand(Weapon offHand) {
        this.offHand = offHand;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }
}
