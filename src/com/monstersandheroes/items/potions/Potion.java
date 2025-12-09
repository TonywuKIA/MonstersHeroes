package com.monstersandheroes.items.potions;

import com.monstersandheroes.characters.heroes.Hero;
import com.monstersandheroes.items.Usable;
import java.util.EnumSet;

/**
 * Consumable potion that boosts a specific statistic.
 */
public class Potion implements Usable {
    private final String name;
    private final int price;
    private final int requiredLevel;
    private final int effectAmount;
    private final EnumSet<PotionType> affectedTypes;
    private boolean consumed;

    public Potion(String name, int price, int requiredLevel, int effectAmount, EnumSet<PotionType> affectedTypes) {
        this.name = name;
        this.price = price;
        this.requiredLevel = requiredLevel;
        this.effectAmount = effectAmount;
        this.affectedTypes = EnumSet.copyOf(affectedTypes);
    }

    public int getEffectAmount() {
        return effectAmount;
    }

    public EnumSet<PotionType> getAffectedTypes() {
        return EnumSet.copyOf(affectedTypes);
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
        consumed = true;
    }

    /**
     * Apply this potion to the given hero and mark as consumed.
     */
    public void use(Hero hero) {
        for (PotionType type : affectedTypes) {
            switch (type) {
                case HEALTH:
                    hero.setHitPoints(hero.getHitPoints() + effectAmount);
                    break;
                case MANA:
                    hero.setMana(hero.getMana() + effectAmount);
                    break;
                case STRENGTH:
                    hero.getAttributes().setStrength(hero.getAttributes().getStrength() + effectAmount);
                    break;
                case DEXTERITY:
                    hero.getAttributes().setDexterity(hero.getAttributes().getDexterity() + effectAmount);
                    break;
                case AGILITY:
                    hero.getAttributes().setAgility(hero.getAttributes().getAgility() + effectAmount);
                    break;
                case DEFENSE:
                    // No explicit defense stat; could map to agility or ignore. Here we skip.
                    break;
                default:
                    break;
            }
        }
        consumed = true;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }

    public void markConsumed() {
        consumed = true;
    }
}
