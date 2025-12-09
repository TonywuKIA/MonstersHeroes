package com.monstersandheroes.characters.party;

import com.monstersandheroes.characters.monsters.Monster;
import com.monstersandheroes.characters.monsters.MonsterFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for on-going battle monsters.
 */
public class MonsterGroup {
    private final List<Monster> monsters = new ArrayList<>();

    public static MonsterGroup fromParty(HeroParty heroParty) {
        MonsterGroup group = new MonsterGroup();
        MonsterFactory factory = new MonsterFactory();
        int count = heroParty.getHeroes().size();
        int level = heroParty.getHighestLevel();
        group.monsters.addAll(factory.createMonsters(count, level));
        return group;
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public boolean isDefeated() {
        return monsters.stream().noneMatch(Monster::isAlive);
    }
}
