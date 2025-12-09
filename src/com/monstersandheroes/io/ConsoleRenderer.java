package com.monstersandheroes.io;

import com.monstersandheroes.characters.party.HeroParty;
import com.monstersandheroes.engine.MapTile;
import com.monstersandheroes.engine.TileType;
import com.monstersandheroes.engine.WorldMap;

/**
 * Handles user-facing console output.
 */
public class ConsoleRenderer {
    public void printWelcome() {
        System.out.println("Welcome to Monsters and Heroes!");
        System.out.println("Controls: WASD move | M market | R redraw map | B backpack | I info | Q quit");
    }

    public void showHeroOptions(java.util.List<com.monstersandheroes.characters.heroes.Hero> heroes) {
        System.out.println("Available heroes:");
        for (int i = 0; i < heroes.size(); i++) {
            com.monstersandheroes.characters.heroes.Hero hero = heroes.get(i);
            String type = hero.getClass().getSimpleName();
            System.out.printf("%d) [%s] %s  Lv.%d  HP:%d  MP:%d  STR:%d  DEX:%d  AGI:%d  DEF:%d  Gold:%d  XP:%d%n",
                i + 1,
                type,
                hero.getName(),
                hero.getLevel(),
                hero.getHitPoints(),
                hero.getMana(),
                hero.getAttributes().getStrength(),
                hero.getAttributes().getDexterity(),
                hero.getAttributes().getAgility(),
                hero.getAttributes().getDefense(),
                hero.getGold(),
                hero.getExperience());
        }
    }

    public void renderMap(WorldMap map) {
        System.out.println("Current world map:");
        System.out.println("Legend: P=Party M=Market X=Blocked ·=Common");
        for (int row = 0; row < map.getSize(); row++) {
            for (int column = 0; column < map.getSize(); column++) {
                MapTile tile = map.getTile(row, column);
                char symbol = map.isPartyAt(row, column) ? 'P' : resolveSymbol(tile.getTileType());
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
    }

    private char resolveSymbol(TileType type) {
        switch (type) {
            case INACCESSIBLE:
                return 'X';
            case MARKET:
                return 'M';
            default:
                return '·';
        }
    }

    public void showParty(HeroParty party) {
        System.out.println("Party overview:");
        for (int i = 0; i < party.getHeroes().size(); i++) {
            com.monstersandheroes.characters.heroes.Hero hero = party.getHeroes().get(i);
            String type = hero.getClass().getSimpleName();
            com.monstersandheroes.inventory.EquipmentSet gear = hero.getInventory().getEquipmentSet();
            String main = gear.getMainHand() != null ? gear.getMainHand().getName() : "None";
            String armor = gear.getArmor() != null ? gear.getArmor().getName() : "None";
            if (gear.getMainHand() != null && gear.getMainHand().isBroken()) {
                main += " (broken)";
            }
            if (gear.getArmor() != null && gear.getArmor().isBroken()) {
                armor += " (broken)";
            }
            System.out.printf("%d) [%s] %s (Lv.%d HP:%d MP:%d Gold:%d | Main:%s Armor:%s)%n",
                i + 1,
                type,
                hero.getName(),
                hero.getLevel(),
                hero.getHitPoints(),
                hero.getMana(),
                hero.getGold(),
                main,
                armor);
        }
    }

    public void unknownCommand(String command) {
        System.out.println("Unknown command: " + command);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showBattleInfo(com.monstersandheroes.characters.party.HeroParty heroes,
                               com.monstersandheroes.characters.party.MonsterGroup monsters) {
        System.out.println("=== Heroes ===");
        heroes.getHeroes().forEach(hero -> {
            com.monstersandheroes.inventory.EquipmentSet gear = hero.getInventory().getEquipmentSet();
            String main = gear.getMainHand() != null ? gear.getMainHand().getName() : "None";
            String off = gear.getOffHand() != null ? gear.getOffHand().getName() : "None";
            String armor = gear.getArmor() != null ? gear.getArmor().getName() : "None";
            if (gear.getMainHand() != null && gear.getMainHand().isBroken()) {
                main += " (broken)";
            }
            if (gear.getOffHand() != null && gear.getOffHand().isBroken()) {
                off += " (broken)";
            }
            if (gear.getArmor() != null && gear.getArmor().isBroken()) {
                armor += " (broken)";
            }
            System.out.printf("%s (Lv.%d) HP:%d MP:%d XP:%d Gold:%d | STR:%d DEX:%d AGI:%d DEF:%d | Main:%s Off:%s Armor:%s%n",
                hero.getName(),
                hero.getLevel(),
                hero.getHitPoints(),
                hero.getMana(),
                hero.getExperience(),
                hero.getGold(),
                hero.getAttributes().getStrength(),
                hero.getAttributes().getDexterity(),
                hero.getAttributes().getAgility(),
                hero.getAttributes().getDefense(),
                main,
                off,
                armor);
        });
        System.out.println("=== Monsters ===");
        monsters.getMonsters().forEach(monster -> {
            System.out.printf("%s (Lv.%d) HP:%d DMG:%d DEF:%d Dodge:%.2f%n",
                monster.getName(),
                monster.getLevel(),
                monster.getHitPoints(),
                monster.getBaseDamage(),
                monster.getDefense(),
                monster.getDodgeChance());
        });
    }
}
