package com.monstersandheroes.inventory;

import com.monstersandheroes.characters.heroes.Hero;
import com.monstersandheroes.io.ConsoleRenderer;
import com.monstersandheroes.io.InputHandler;
import com.monstersandheroes.items.armor.Armor;
import com.monstersandheroes.items.potions.Potion;
import com.monstersandheroes.items.weapons.Weapon;
import java.util.List;

/**
 * Shared routines for equipping items and consuming potions.
 */
public final class InventoryInteraction {
    private InventoryInteraction() {
    }

    public static void equipWeapon(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        List<Weapon> weapons = hero.getInventory().getWeapons();
        if (weapons.isEmpty()) {
            renderer.showMessage("No weapons to equip.");
            return;
        }
        System.out.println("Weapons:");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon weapon = weapons.get(i);
            System.out.printf("%d) %s (Damage:%d, Hands:%d, Req Lv:%d)%n",
                i + 1, weapon.getName(), weapon.getDamage(), weapon.getHandsRequired(), weapon.getRequiredLevel());
        }
        int choice = input.readInt("Choose weapon (0 to cancel): ", 0, weapons.size());
        if (choice == 0) {
            return;
        }
        Weapon selected = weapons.get(choice - 1);
        if (hero.getLevel() < selected.getRequiredLevel()) {
            renderer.showMessage("Level too low for this weapon.");
            return;
        }
        // Auto-slot: two-handed replaces both; otherwise main if free, else off-hand.
        boolean mainWasEmpty = hero.getInventory().getEquipmentSet().getMainHand() == null;
        hero.getInventory().equipWeapon(selected);
        if (selected.getHandsRequired() == 2) {
            renderer.showMessage("Equipped two-handed weapon: " + selected.getName());
        } else if (mainWasEmpty) {
            renderer.showMessage("Equipped to main hand: " + selected.getName());
        } else {
            renderer.showMessage("Equipped to off hand: " + selected.getName());
        }
    }

    public static void equipArmor(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        List<Armor> armors = hero.getInventory().getArmors();
        if (armors.isEmpty()) {
            renderer.showMessage("No armor to equip.");
            return;
        }
        System.out.println("Armor:");
        for (int i = 0; i < armors.size(); i++) {
            Armor armor = armors.get(i);
            System.out.printf("%d) %s (Reduction:%d, Req Lv:%d)%n",
                i + 1, armor.getName(), armor.getDamageReduction(), armor.getRequiredLevel());
        }
        int choice = input.readInt("Choose armor (0 to cancel): ", 0, armors.size());
        if (choice == 0) {
            return;
        }
        Armor selected = armors.get(choice - 1);
        if (hero.getLevel() < selected.getRequiredLevel()) {
            renderer.showMessage("Level too low for this armor.");
            return;
        }
        hero.getInventory().equipArmor(selected);
        renderer.showMessage("Equipped armor: " + selected.getName());
    }

    public static void usePotion(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        List<Potion> potions = hero.getInventory().getPotions();
        if (potions.isEmpty()) {
            renderer.showMessage("No potions to use.");
            return;
        }
        System.out.println("Potions:");
        for (int i = 0; i < potions.size(); i++) {
            Potion potion = potions.get(i);
            System.out.printf("%d) %s (Effect:+%d %s, Req Lv:%d)%n",
                i + 1,
                potion.getName(),
                potion.getEffectAmount(),
                potion.getAffectedTypes(),
                potion.getRequiredLevel());
        }
        int choice = input.readInt("Choose potion (0 to cancel): ", 0, potions.size());
        if (choice == 0) {
            return;
        }
        Potion selected = potions.get(choice - 1);
        if (hero.getLevel() < selected.getRequiredLevel()) {
            renderer.showMessage("Level too low for this potion.");
            return;
        }
        selected.use(hero);
        hero.getInventory().removeIfConsumed();
        renderer.showMessage("Used potion: " + selected.getName());
    }
}
