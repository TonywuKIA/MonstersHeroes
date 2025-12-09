package com.monstersandheroes.market;

import com.monstersandheroes.data.DataLoader;
import com.monstersandheroes.items.Item;
import com.monstersandheroes.items.armor.Armor;
import com.monstersandheroes.items.potions.Potion;
import com.monstersandheroes.items.potions.PotionType;
import com.monstersandheroes.items.spells.Spell;
import com.monstersandheroes.items.spells.SpellType;
import com.monstersandheroes.items.weapons.Weapon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Collection of items that a market sells.
 */
public class MarketInventory {
    private static final int WEAPON_COLUMNS = 5;
    private static final int ARMOR_COLUMNS = 4;
    private static final int POTION_COLUMNS = 5;
    private static final int SPELL_COLUMNS = 5;

    private final List<Item> stock = new ArrayList<>();
    private final DataLoader dataLoader = new DataLoader();

    public void addItem(Item item) {
        stock.add(item);
    }

    public void removeItem(Item item) {
        stock.remove(item);
    }

    public List<Item> getStock() {
        return Collections.unmodifiableList(stock);
    }

    public void loadDefaultStock() {
        stock.clear();
        loadWeapons();
        loadArmor();
        loadPotions();
        loadSpells("IceSpells.txt", SpellType.ICE);
        loadSpells("FireSpells.txt", SpellType.FIRE);
        loadSpells("LightningSpells.txt", SpellType.LIGHTNING);
    }

    private void loadWeapons() {
        for (String[] row : dataLoader.loadTable("Weaponry.txt", WEAPON_COLUMNS)) {
            addItem(new Weapon(
                row[0],
                parseInt(row[1]),
                parseInt(row[2]),
                parseInt(row[3]),
                parseInt(row[4])
            ));
        }
    }

    private void loadArmor() {
        for (String[] row : dataLoader.loadTable("Armory.txt", ARMOR_COLUMNS)) {
            addItem(new Armor(
                row[0],
                parseInt(row[1]),
                parseInt(row[2]),
                parseInt(row[3])
            ));
        }
    }

    private void loadPotions() {
        for (String[] row : dataLoader.loadTable("Potions.txt", POTION_COLUMNS)) {
            EnumSet<PotionType> types = parsePotionTypes(row[4]);
            addItem(new Potion(
                row[0],
                parseInt(row[1]),
                parseInt(row[2]),
                parseInt(row[3]),
                types
            ));
        }
    }

    private void loadSpells(String fileName, SpellType type) {
        for (String[] row : dataLoader.loadTable(fileName, SPELL_COLUMNS)) {
            addItem(new Spell(
                row[0],
                parseInt(row[1]),
                parseInt(row[2]),
                parseInt(row[3]),
                parseInt(row[4]),
                type
            ));
        }
    }

    private EnumSet<PotionType> parsePotionTypes(String descriptor) {
        EnumSet<PotionType> set = EnumSet.noneOf(PotionType.class);
        String normalized = descriptor.replace("All", "").trim();
        String[] fragments = normalized.split("[/\\s]+");
        for (String fragment : fragments) {
            if (fragment.isEmpty()) {
                continue;
            }
            try {
                set.add(PotionType.valueOf(fragment.toUpperCase()));
            } catch (IllegalArgumentException ignored) {
                // Not a recognized stat name; skip.
            }
        }
        if (set.isEmpty()) {
            set.add(PotionType.HEALTH);
        }
        return set;
    }

    private int parseInt(String token) {
        return Integer.parseInt(token.trim());
    }
}
