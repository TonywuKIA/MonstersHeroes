package com.monstersandheroes.characters.party;

import com.monstersandheroes.characters.heroes.Hero;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the collection of heroes currently controlled by the player.
 */
public class HeroParty {
    private final List<Hero> heroes = new ArrayList<>();

    public void addHero(Hero hero) {
        heroes.add(hero);
    }

    public List<Hero> getHeroes() {
        return Collections.unmodifiableList(heroes);
    }

    public boolean isDefeated() {
        return heroes.stream().noneMatch(Hero::isAlive);
    }

    public int getHighestLevel() {
        return heroes.stream().mapToInt(Hero::getLevel).max().orElse(1);
    }
}
