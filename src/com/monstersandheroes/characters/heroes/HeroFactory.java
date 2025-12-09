package com.monstersandheroes.characters.heroes;

import com.monstersandheroes.data.DataLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for instantiating hero archetypes from data files.
 */
public class HeroFactory {
    private static final int HERO_DATA_COLUMNS = 7;
    private final DataLoader dataLoader = new DataLoader();

    public List<Hero> loadDefaultHeroes() {
        List<Hero> heroes = new ArrayList<>();
        heroes.addAll(loadWarriors());
        heroes.addAll(loadSorcerers());
        heroes.addAll(loadPaladins());
        return heroes;
    }

    public List<Hero> loadWarriors() {
        return loadHeroes("Warriors.txt", Warrior::new);
    }

    public List<Hero> loadSorcerers() {
        return loadHeroes("Sorcerers.txt", Sorcerer::new);
    }

    public List<Hero> loadPaladins() {
        return loadHeroes("Paladins.txt", Paladin::new);
    }

    private List<Hero> loadHeroes(String fileName, HeroCreator creator) {
        List<String[]> rows = dataLoader.loadTable(fileName, HERO_DATA_COLUMNS);
        List<Hero> heroes = new ArrayList<>();
        for (String[] row : rows) {
            heroes.add(creator.create(
                row[0],
                parseInt(row[1]),
                parseInt(row[2]),
                parseInt(row[3]),
                parseInt(row[4]),
                parseInt(row[5]),
                parseInt(row[6])
            ));
        }
        return heroes;
    }

    private int parseInt(String token) {
        return Integer.parseInt(token.trim());
    }

    @FunctionalInterface
    private interface HeroCreator {
        Hero create(String name, int mana, int strength, int agility, int dexterity, int gold, int experience);
    }

    public Hero createWarrior(String name) {
        return new Warrior(name);
    }

    public Hero createSorcerer(String name) {
        return new Sorcerer(name);
    }

    public Hero createPaladin(String name) {
        return new Paladin(name);
    }
}
