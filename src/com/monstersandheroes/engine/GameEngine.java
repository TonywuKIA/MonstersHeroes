package com.monstersandheroes.engine;

import com.monstersandheroes.characters.party.HeroParty;
import com.monstersandheroes.characters.party.MonsterGroup;
import com.monstersandheroes.config.GameSettings;
import com.monstersandheroes.config.GameConfig;
import com.monstersandheroes.characters.heroes.Hero;
import com.monstersandheroes.characters.heroes.HeroFactory;
import com.monstersandheroes.io.ConsoleRenderer;
import com.monstersandheroes.io.InputHandler;
import com.monstersandheroes.market.Market;
import com.monstersandheroes.util.Dice;
import java.util.ArrayList;
import java.util.List;

/**
 * High level coordinator responsible for the runtime game loop.
 */
public class GameEngine {
    private final GameSettings settings = new GameSettings();
    private final InputHandler inputHandler = new InputHandler();
    private final ConsoleRenderer renderer = new ConsoleRenderer();
    private final Dice dice = new Dice();

    private WorldMap worldMap;
    private HeroParty heroParty;

    public void start() {
        renderer.printWelcome();
        setupGame();
        gameLoop();
    }

    private void setupGame() {
        HeroFactory heroFactory = new HeroFactory();
        boolean partyReady = false;
        while (!partyReady) {
            List<Hero> warriors = new ArrayList<>(heroFactory.loadWarriors());
            List<Hero> sorcerers = new ArrayList<>(heroFactory.loadSorcerers());
            List<Hero> paladins = new ArrayList<>(heroFactory.loadPaladins());

            int maxSize = GameConfig.MAX_PARTY_SIZE;
            int minSize = GameConfig.MIN_PARTY_SIZE;
            int partySize = inputHandler.readInt(String.format("Choose party size (%d-%d): ", minSize, maxSize), minSize, maxSize);

            heroParty = new HeroParty();
            boolean canceled = false;
            for (int i = 0; i < partySize; i++) {
                boolean picked = false;
                while (!picked) {
                    System.out.println("Select class: 1) Warrior  2) Sorcerer  3) Paladin  (0 to restart)");
                    int clazz = inputHandler.readInt("Class: ", 0, 3);
                    if (clazz == 0) {
                        canceled = true;
                        break;
                    }
                    List<Hero> pool;
                    switch (clazz) {
                        case 1:
                            pool = warriors;
                            break;
                        case 2:
                            pool = sorcerers;
                            break;
                        default:
                            pool = paladins;
                    }
                    if (pool.isEmpty()) {
                        renderer.showMessage("No heroes left in this class. Pick another class.");
                        continue;
                    }
                    renderer.showHeroOptions(pool);
                    int choice = inputHandler.readInt("Pick hero number (0 to reselect class): ", 0, pool.size());
                    if (choice == 0) {
                        continue;
                    }
                    Hero chosen = pool.get(choice - 1);
                    heroParty.addHero(chosen);
                    pool.remove(chosen);
                    picked = true;
                }
                if (canceled) {
                    break;
                }
            }
            partyReady = !canceled;
        }

        MapGenerator generator = new MapGenerator();
        worldMap = generator.generate(settings.getMapSize());
    }

    private void gameLoop() {
        boolean running = true;
        while (running) {
            renderer.renderMap(worldMap);
            renderer.showMessage("Commands: WASD move | M market | B backpack | I info | Q quit");
            String command = inputHandler.readCommand();
            running = handleCommand(command);
        }
    }

    private boolean handleCommand(String command) {
        switch (command.toUpperCase()) {
            case "Q":
                return false;
            case "W":
            case "A":
            case "S":
            case "D":
                handleMovement(command);
                break;
            case "I":
                renderer.showParty(heroParty);
                break;
            case "R":
                renderer.renderMap(worldMap);
                break;
            case "B":
                openBackpack();
                break;
            case "M":
                enterMarket();
                break;
            default:
                renderer.unknownCommand(command);
        }
        return true;
    }

    private void handleMovement(String command) {
        int deltaRow = 0;
        int deltaColumn = 0;
        switch (command.toUpperCase()) {
            case "W":
                deltaRow = -1;
                break;
            case "S":
                deltaRow = 1;
                break;
            case "A":
                deltaColumn = -1;
                break;
            case "D":
                deltaColumn = 1;
                break;
            default:
                return;
        }

        if (!worldMap.canMove(deltaRow, deltaColumn)) {
            renderer.showMessage("Blocked: out of bounds or inaccessible tile.");
            return;
        }

        worldMap.move(deltaRow, deltaColumn);
        renderer.renderMap(worldMap);

        MapTile tile = worldMap.currentTile();
        if (tile.getTileType() == TileType.COMMON) {
            attemptEncounter();
        } else if (tile.getTileType() == TileType.MARKET) {
            renderer.showMessage("Market here. Press M to enter.");
        }
    }

    private void enterMarket() {
        MapTile current = worldMap.currentTile();
        if (current.getTileType() != TileType.MARKET) {
            renderer.showMessage("No market on this tile.");
            return;
        }
        Market market = new Market();
        market.open(heroParty, inputHandler, renderer);
    }

    private void openBackpack() {
        renderer.showMessage("Backpack: select a hero to manage (0 to exit).");
        List<Hero> heroes = heroParty.getHeroes();
        for (int i = 0; i < heroes.size(); i++) {
            Hero hero = heroes.get(i);
            System.out.printf("%d) %s (Lv.%d, HP:%d, MP:%d)%n", i + 1, hero.getName(), hero.getLevel(), hero.getHitPoints(), hero.getMana());
        }
        int choice = inputHandler.readInt("Hero number: ", 0, heroes.size());
        if (choice == 0) {
            return;
        }
        Hero selected = heroes.get(choice - 1);
        boolean managing = true;
        while (managing) {
            System.out.println("1) Equip weapon  2) Equip armor  3) Use potion  0) Back");
            int action = inputHandler.readInt("Choose: ", 0, 3);
            switch (action) {
                case 1:
                    com.monstersandheroes.inventory.InventoryInteraction.equipWeapon(selected, inputHandler, renderer);
                    break;
                case 2:
                    com.monstersandheroes.inventory.InventoryInteraction.equipArmor(selected, inputHandler, renderer);
                    break;
                case 3:
                    com.monstersandheroes.inventory.InventoryInteraction.usePotion(selected, inputHandler, renderer);
                    break;
                default:
                    managing = false;
            }
        }
    }

    private void attemptEncounter() {
        // Roll a 5-sided die; on a 1, trigger battle (true dice feel).
        if (dice.roll(5) == 1) {
            renderer.showMessage("Monsters appear! Battle begins.");
            triggerBattle();
        }
    }

    private void triggerBattle() {
        MonsterGroup monsters = MonsterGroup.fromParty(heroParty);
        Battle battle = new Battle(heroParty, monsters);
        battle.setRenderer(renderer);
        BattleResult result = battle.execute();
        if (result == BattleResult.MONSTER_VICTORY) {
            renderer.showMessage("All heroes have fallen. Game over!");
            System.exit(0);
        } else if (result == BattleResult.RUN_AWAY) {
            renderer.showMessage("You escaped the battle.");
        }
    }
}
