package com.monstersandheroes.characters.monsters;

import com.monstersandheroes.data.DataLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Generates monsters scaled to the party level.
 */
public class MonsterFactory {
    private static final int MONSTER_COLUMNS = 5;

    private final Random random = new Random();
    private final List<MonsterData> dragons;
    private final List<MonsterData> exoskeletons;
    private final List<MonsterData> spirits;

    public MonsterFactory() {
        DataLoader loader = new DataLoader();
        this.dragons = toMonsterData(loader.loadTable("Dragons.txt", MONSTER_COLUMNS));
        this.exoskeletons = toMonsterData(loader.loadTable("Exoskeletons.txt", MONSTER_COLUMNS));
        this.spirits = toMonsterData(loader.loadTable("Spirits.txt", MONSTER_COLUMNS));
    }

    public List<Monster> createMonsters(int count, int level) {
        List<Monster> monsters = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            monsters.add(createRandomMonster(level));
        }
        return monsters;
    }

    private Monster createRandomMonster(int level) {
        int pick = random.nextInt(3);
        switch (pick) {
            case 0:
                return createMonsterFromTemplates(dragons, data -> new Dragon(data.name, data.level, data.damage, data.defense, data.dodgeChance), level);
            case 1:
                return createMonsterFromTemplates(exoskeletons, data -> new Exoskeleton(data.name, data.level, data.damage, data.defense, data.dodgeChance), level);
            default:
                return createMonsterFromTemplates(spirits, data -> new Spirit(data.name, data.level, data.damage, data.defense, data.dodgeChance), level);
        }
    }

    private Monster createMonsterFromTemplates(List<MonsterData> data,
                                               MonsterBuilder builder,
                                               int level) {
        List<MonsterData> pool = filterByLevel(data, level);
        MonsterData chosen = pool.get(random.nextInt(pool.size()));
        return builder.create(chosen);
    }

    /**
     * Prefer monsters whose level exactly matches the party's highest level.
     * If none exist (e.g., malformed data), fall back to those below the level; if still none, use full pool.
     */
    private List<MonsterData> filterByLevel(List<MonsterData> data, int level) {
        List<MonsterData> exact = data.stream()
            .filter(entry -> entry.level == level)
            .collect(Collectors.toList());
        if (!exact.isEmpty()) {
            return exact;
        }
        List<MonsterData> lower = data.stream()
            .filter(entry -> entry.level <= level)
            .collect(Collectors.toList());
        return lower.isEmpty() ? data : lower;
    }

    private List<MonsterData> toMonsterData(List<String[]> rows) {
        List<MonsterData> data = new ArrayList<>();
        for (String[] row : rows) {
            data.add(new MonsterData(row));
        }
        return Collections.unmodifiableList(data);
    }

    private static final class MonsterData {
        private final String name;
        private final int level;
        private final int damage;
        private final int defense;
        private final double dodgeChance;

        private MonsterData(String[] row) {
            this.name = row[0];
            this.level = parseInt(row[1]);
            this.damage = parseInt(row[2]);
            this.defense = parseInt(row[3]);
            this.dodgeChance = parseDouble(row[4]) / 100.0;
        }

        private int parseInt(String token) {
            return Integer.parseInt(token.trim());
        }

        private double parseDouble(String token) {
            return Double.parseDouble(token.trim());
        }
    }

    @FunctionalInterface
    private interface MonsterBuilder {
        Monster create(MonsterData data);
    }
}
