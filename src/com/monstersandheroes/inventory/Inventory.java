package com.monstersandheroes.inventory;

import com.monstersandheroes.items.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents everything a hero owns.
 */
public class Inventory {
    private final List<Item> items = new ArrayList<>();
    private final EquipmentSet equipmentSet = new EquipmentSet();

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public EquipmentSet getEquipmentSet() {
        return equipmentSet;
    }

    public List<com.monstersandheroes.items.weapons.Weapon> getWeapons() {
        List<com.monstersandheroes.items.weapons.Weapon> weapons = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof com.monstersandheroes.items.weapons.Weapon) {
                weapons.add((com.monstersandheroes.items.weapons.Weapon) item);
            }
        }
        return weapons;
    }

    public List<com.monstersandheroes.items.armor.Armor> getArmors() {
        List<com.monstersandheroes.items.armor.Armor> armors = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof com.monstersandheroes.items.armor.Armor) {
                armors.add((com.monstersandheroes.items.armor.Armor) item);
            }
        }
        return armors;
    }

    public List<com.monstersandheroes.items.potions.Potion> getPotions() {
        List<com.monstersandheroes.items.potions.Potion> potions = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof com.monstersandheroes.items.potions.Potion) {
                potions.add((com.monstersandheroes.items.potions.Potion) item);
            }
        }
        return potions;
    }

    public List<com.monstersandheroes.items.spells.Spell> getSpells() {
        List<com.monstersandheroes.items.spells.Spell> spells = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof com.monstersandheroes.items.spells.Spell) {
                spells.add((com.monstersandheroes.items.spells.Spell) item);
            }
        }
        return spells;
    }

    public void equipWeapon(com.monstersandheroes.items.weapons.Weapon weapon) {
        if (weapon.getHandsRequired() == 2) {
            equipmentSet.setMainHand(weapon);
            equipmentSet.setOffHand(null);
        } else {
            if (equipmentSet.getMainHand() == null) {
                equipmentSet.setMainHand(weapon);
            } else {
                equipmentSet.setOffHand(weapon);
            }
        }
    }

    public boolean equipMainHand(com.monstersandheroes.items.weapons.Weapon weapon) {
        if (weapon.getHandsRequired() == 2) {
            equipmentSet.setMainHand(weapon);
            equipmentSet.setOffHand(null);
            return true;
        }
        equipmentSet.setMainHand(weapon);
        return true;
    }

    public boolean equipOffHand(com.monstersandheroes.items.weapons.Weapon weapon) {
        if (weapon.getHandsRequired() == 2) {
            return false;
        }
        if (equipmentSet.getMainHand() != null && equipmentSet.getMainHand().getHandsRequired() == 2) {
            return false;
        }
        equipmentSet.setOffHand(weapon);
        return true;
    }

    public void clearOffHand() {
        equipmentSet.setOffHand(null);
    }

    public void equipArmor(com.monstersandheroes.items.armor.Armor armor) {
        equipmentSet.setArmor(armor);
    }

    public void removeIfConsumed() {
        items.removeIf(item -> (item instanceof com.monstersandheroes.items.spells.Spell && ((com.monstersandheroes.items.spells.Spell) item).isConsumed())
            || (item instanceof com.monstersandheroes.items.potions.Potion && ((com.monstersandheroes.items.potions.Potion) item).isConsumed()));
    }
}
