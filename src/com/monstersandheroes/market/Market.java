package com.monstersandheroes.market;

import com.monstersandheroes.characters.party.HeroParty;
import com.monstersandheroes.characters.heroes.Hero;
import com.monstersandheroes.io.ConsoleRenderer;
import com.monstersandheroes.io.InputHandler;
import com.monstersandheroes.items.Item;
import com.monstersandheroes.inventory.InventoryInteraction;
import com.monstersandheroes.items.weapons.Weapon;
import com.monstersandheroes.items.armor.Armor;
import java.util.List;

/**
 * Provides buying and selling of equipment for heroes.
 */
public class Market {
    private final MarketInventory inventory = new MarketInventory();

    public Market() {
        inventory.loadDefaultStock();
    }

    public void open(HeroParty heroParty, InputHandler input, ConsoleRenderer renderer) {
        boolean stay = true;
        while (stay) {
            renderer.showMessage("Entering market... Choose a hero to manage or 0 to exit.");
            List<Hero> heroes = heroParty.getHeroes();
            for (int i = 0; i < heroes.size(); i++) {
                Hero hero = heroes.get(i);
                System.out.printf("%d) %s (Gold: %d, Level: %d)%n", i + 1, hero.getName(), hero.getGold(), hero.getLevel());
            }
            int choice = input.readInt("Hero number (0 to leave): ", 0, heroes.size());
            if (choice == 0) {
                stay = false;
                break;
            }
            Hero selected = heroes.get(choice - 1);
            heroMenu(selected, input, renderer);
        }
    }

    public MarketInventory getInventory() {
        return inventory;
    }

    private String category(Item item) {
        if (item instanceof Weapon) {
            return "Weapon";
        }
        if (item instanceof Armor) {
            return "Armor";
        }
        if (item instanceof com.monstersandheroes.items.potions.Potion) {
            return "Potion";
        }
        if (item instanceof com.monstersandheroes.items.spells.Spell) {
            return "Spell";
        }
        return "Item";
    }

    private void heroMenu(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        boolean manage = true;
        while (manage) {
            System.out.printf("Managing %s (Gold: %d, Level: %d)%n", hero.getName(), hero.getGold(), hero.getLevel());
            System.out.println("1) Buy items");
            System.out.println("2) Sell items");
            System.out.println("3) Equip gear");
            System.out.println("4) Repair gear (half price)");
            System.out.println("0) Back to hero list");
            int action = input.readInt("Choose action: ", 0, 4);
            switch (action) {
                case 1:
                    buy(hero, input, renderer);
                    break;
                case 2:
                    sell(hero, input, renderer);
                    break;
                case 3:
                    equipMenu(hero, input, renderer);
                    break;
                case 4:
                    repairMenu(hero, input, renderer);
                    break;
                default:
                    manage = false;
            }
        }
    }

    private void buy(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        List<Item> stock = inventory.getStock();
        if (stock.isEmpty()) {
            renderer.showMessage("Market is out of stock.");
            return;
        }
        List<Item> visible = stock.stream()
            .filter(item -> item.getRequiredLevel() <= hero.getLevel())
            .collect(java.util.stream.Collectors.toList());
        System.out.println("Items you can buy (filtered by your level):");
        for (int i = 0; i < visible.size(); i++) {
            Item item = visible.get(i);
            System.out.printf("%d) [%s] %s (Price: %d, Req Lv: %d)%n",
                i + 1, category(item), item.getName(), item.getPrice(), item.getRequiredLevel());
        }
        int choice = input.readInt("Choose item to buy (0 to cancel): ", 0, visible.size());
        if (choice == 0) {
            return;
        }
        Item selected = visible.get(choice - 1);
        if (hero.getGold() < selected.getPrice()) {
            renderer.showMessage("Not enough gold.");
            return;
        }
        if (hero.getLevel() < selected.getRequiredLevel()) {
            renderer.showMessage("Level too low for this item.");
            return;
        }
        hero.addGold(-selected.getPrice());
        hero.getInventory().addItem(selected);
        inventory.removeItem(selected);
        renderer.showMessage("Purchased " + selected.getName());
    }

    private void sell(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        List<Item> items = hero.getInventory().getItems();
        if (items.isEmpty()) {
            renderer.showMessage("No items to sell.");
            return;
        }
        System.out.println("Your items:");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.printf("%d) %s (Sell price: %d)%n", i + 1, item.getName(), item.getPrice() / 2);
        }
        int choice = input.readInt("Choose item to sell (0 to cancel): ", 0, items.size());
        if (choice == 0) {
            return;
        }
        Item selected = items.get(choice - 1);
        hero.addGold(selected.getPrice() / 2);
        hero.getInventory().removeItem(selected);
        inventory.addItem(selected);
        renderer.showMessage("Sold " + selected.getName());
    }

    private void equipMenu(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        boolean equipLoop = true;
        while (equipLoop) {
            System.out.println("Equip menu:");
            System.out.println("1) Equip weapon");
            System.out.println("2) Equip armor");
            System.out.println("3) Use potion");
            System.out.println("0) Back");
            int action = input.readInt("Choose: ", 0, 3);
            switch (action) {
                case 1:
                    InventoryInteraction.equipWeapon(hero, input, renderer);
                    break;
                case 2:
                    InventoryInteraction.equipArmor(hero, input, renderer);
                    break;
                case 3:
                    InventoryInteraction.usePotion(hero, input, renderer);
                    break;
                default:
                    equipLoop = false;
            }
        }
    }

    private void repairMenu(Hero hero, InputHandler input, ConsoleRenderer renderer) {
        List<Item> items = hero.getInventory().getItems();
        List<Item> repairables = new java.util.ArrayList<>();
        for (Item item : items) {
            if (item instanceof Weapon || item instanceof Armor) {
                repairables.add(item);
            }
        }
        if (repairables.isEmpty()) {
            renderer.showMessage("No gear to repair.");
            return;
        }
        System.out.println("Repairable gear:");
        for (int i = 0; i < repairables.size(); i++) {
            Item item = repairables.get(i);
            System.out.printf("%d) %s (Repair cost: %d)%n", i + 1, item.getName(), item.getPrice() / 2);
        }
        int choice = input.readInt("Choose item to repair (0 to cancel): ", 0, repairables.size());
        if (choice == 0) {
            return;
        }
        Item target = repairables.get(choice - 1);
        int cost = target.getPrice() / 2;
        if (hero.getGold() < cost) {
            renderer.showMessage("Not enough gold to repair.");
            return;
        }
        if (target instanceof Weapon) {
            ((Weapon) target).repair();
        } else if (target instanceof Armor) {
            ((Armor) target).repair();
        }
        hero.addGold(-cost);
        renderer.showMessage("Repaired: " + target.getName());
    }
}
