package com.monstersandheroes.characters;

import com.monstersandheroes.characters.attributes.Attributes;

/**
 * Shared state for any battle participant.
 */
public abstract class LivingEntity {
    private final String name;
    private int level;
    private int hitPoints;
    private final Attributes attributes;

    protected LivingEntity(String name, int level, int hitPoints, Attributes attributes) {
        this.name = name;
        this.level = level;
        this.hitPoints = hitPoints;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public boolean isAlive() {
        return hitPoints > 0;
    }

    public void receiveDamage(int amount) {
        hitPoints = Math.max(0, hitPoints - amount);
    }
}
