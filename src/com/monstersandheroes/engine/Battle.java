package com.monstersandheroes.engine;

import com.monstersandheroes.characters.heroes.Hero;
import com.monstersandheroes.characters.monsters.Monster;
import com.monstersandheroes.characters.party.HeroParty;
import com.monstersandheroes.characters.party.MonsterGroup;
import com.monstersandheroes.inventory.EquipmentSet;
import com.monstersandheroes.inventory.Inventory;
import com.monstersandheroes.io.ConsoleRenderer;
import com.monstersandheroes.io.InputHandler;
import com.monstersandheroes.items.armor.Armor;
import com.monstersandheroes.items.potions.Potion;
import com.monstersandheroes.items.potions.PotionType;
import com.monstersandheroes.items.spells.Spell;
import com.monstersandheroes.items.spells.SpellType;
import com.monstersandheroes.items.weapons.Weapon;
import com.monstersandheroes.util.Dice;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Coordinates the battle loop between heroes and monsters.
 */
public class Battle {
    private final HeroParty heroParty;
    private final MonsterGroup monsterGroup;
    private ConsoleRenderer renderer;
    private final InputHandler input = new InputHandler();
    private final Dice dice = new Dice();
    private final Random random = new Random();
    private final Map<Monster, MonsterStatus> monsterStatusMap = new HashMap<>();

    public Battle(HeroParty heroParty, MonsterGroup monsterGroup) {
        this.heroParty = heroParty;
        this.monsterGroup = monsterGroup;
    }

    public void setRenderer(ConsoleRenderer renderer) {
        this.renderer = renderer;
    }

    public BattleResult execute() {
        prepare();
        while (!heroParty.isDefeated() && !monsterGroup.isDefeated()) {
            BattleResult heroResult = performHeroRound();
            if (heroResult == BattleResult.RUN_AWAY) {
                return BattleResult.RUN_AWAY;
            }
            if (monsterGroup.isDefeated()) {
                break;
            }
            performMonsterRound();
            recoverHeroes();
        }
        BattleResult result = heroParty.isDefeated() ? BattleResult.MONSTER_VICTORY : BattleResult.HERO_VICTORY;
        if (result == BattleResult.HERO_VICTORY) {
            distributeRewards();
        }
        return result;
    }

    private void prepare() {
        if (renderer != null) {
            renderer.showMessage("Battle starts!");
        }
        monsterGroup.getMonsters().forEach(monster -> monsterStatusMap.put(monster, new MonsterStatus()));
    }

    private BattleResult performHeroRound() {
        for (Hero hero : heroParty.getHeroes()) {
            if (!hero.isAlive()) {
                continue;
            }
            if (renderer != null) {
                renderer.showMessage("Action for " + hero.getName());
            }
            boolean acted = false;
            while (!acted) {
                System.out.println("1) Attack  2) Cast spell  3) Use potion  E) Escape  0) Skip  i) Info");
                String choice = input.readCommand();
                switch (choice.toUpperCase()) {
                    case "1":
                        acted = heroAttack(hero);
                        break;
                    case "2":
                        acted = heroCastSpell(hero);
                        break;
                    case "3":
                        acted = heroUsePotion(hero);
                        break;
                    case "E":
                        renderer.showMessage("You fled the battle!");
                        return BattleResult.RUN_AWAY;
                    case "I":
                        renderer.showBattleInfo(heroParty, monsterGroup);
                        acted = false;
                        break;
                    case "0":
                        acted = true;
                        break;
                    default:
                        renderer.showMessage("Please enter a valid choice.");
                }
            }
            if (monsterGroup.isDefeated()) {
                break;
            }
        }
        // Clean up consumed items after hero turn
        heroParty.getHeroes().forEach(h -> h.getInventory().removeIfConsumed());
        return null;
    }

    private boolean heroAttack(Hero hero) {
        Monster target = selectMonsterTarget();
        if (target == null) {
            return false;
        }
        Inventory inv = hero.getInventory();
        EquipmentSet gear = inv.getEquipmentSet();
        Weapon weapon = gear.getMainHand();
        int weaponDamage = (weapon != null && !weapon.isBroken()) ? weapon.getDamage() : 0;
        Weapon offHand = gear.getOffHand();
        if (offHand != null && !offHand.isBroken()) {
            weaponDamage += offHand.getDamage();
        }
        int baseStrength = hero.getAttributes().getStrength();
        double rawDamage = (baseStrength + weaponDamage) * 0.05;
        MonsterStatus status = getStatus(target);
        double effectiveDefense = target.getDefense() * status.defenseModifier * 0.1; // soften defense impact
        int damage = (int) Math.max(1, Math.round(rawDamage - effectiveDefense));
        double dodgeChance = target.getDodgeChance() * status.dodgeModifier;
        if (dice.chance(dodgeChance * 0.5)) { // soften dodge impact
            renderer.showMessage(target.getName() + " dodged the attack!");
            return true;
        }
        if (weapon != null && !weapon.isBroken()) {
            if (weapon.reduceUse()) {
                renderer.showMessage(hero.getName() + "'s " + weapon.getName() + " broke!");
            }
        }
        if (offHand != null && !offHand.isBroken()) {
            if (offHand.reduceUse()) {
                renderer.showMessage(hero.getName() + "'s " + offHand.getName() + " broke!");
            }
        }
        target.receiveDamage(damage);
        renderer.showMessage(hero.getName() + " attacked " + target.getName() + " for " + damage + " damage.");
        if (!target.isAlive()) {
            renderer.showMessage(target.getName() + " is defeated.");
        }
        return true;
    }

    private boolean heroCastSpell(Hero hero) {
        List<Spell> spells = hero.getInventory().getSpells();
        if (spells.isEmpty()) {
            renderer.showMessage("No spells to cast.");
            return false;
        }
        System.out.println("Spells:");
        for (int i = 0; i < spells.size(); i++) {
            Spell spell = spells.get(i);
            System.out.printf("%d) %s (Damage: %d, Mana: %d, Type: %s)%n",
                i + 1, spell.getName(), spell.getBaseDamage(), spell.getManaCost(), spell.getSpellType());
        }
        int choice = input.readInt("Choose spell (0 to cancel): ", 0, spells.size());
        if (choice == 0) {
            return false;
        }
        Spell selected = spells.get(choice - 1);
        if (hero.getMana() < selected.getManaCost()) {
            renderer.showMessage("Not enough mana.");
            return false;
        }
        Monster target = selectMonsterTarget();
        if (target == null) {
            return false;
        }
        hero.setMana(hero.getMana() - selected.getManaCost());
        int dexterity = hero.getAttributes().getDexterity();
        int damage = (int) (selected.getBaseDamage() + dexterity * 0.05);
        MonsterStatus status = getStatus(target);
        double dodgeChance = target.getDodgeChance() * status.dodgeModifier;
        if (dice.chance(dodgeChance * 0.5)) { // soften dodge impact
            renderer.showMessage(target.getName() + " dodged the spell!");
            return true;
        }
        applySpellEffect(selected.getSpellType(), target);
        target.receiveDamage(damage);
        selected.markConsumed();
        renderer.showMessage(hero.getName() + " cast " + selected.getName() + " on " + target.getName() + " for " + damage + " damage.");
        if (!target.isAlive()) {
            renderer.showMessage(target.getName() + " is defeated.");
        }
        return true;
    }

    private boolean heroUsePotion(Hero hero) {
        List<Potion> potions = hero.getInventory().getPotions();
        if (potions.isEmpty()) {
            renderer.showMessage("No potions to use.");
            return false;
        }
        System.out.println("Potions:");
        for (int i = 0; i < potions.size(); i++) {
            Potion potion = potions.get(i);
            System.out.printf("%d) %s (Effect: +%d %s)%n",
                i + 1,
                potion.getName(),
                potion.getEffectAmount(),
                potion.getAffectedTypes());
        }
        int choice = input.readInt("Choose potion (0 to cancel): ", 0, potions.size());
        if (choice == 0) {
            return false;
        }
        Potion selected = potions.get(choice - 1);
        applyPotion(hero, selected);
        selected.markConsumed();
        renderer.showMessage(hero.getName() + " used " + selected.getName());
        return true;
    }

    private void applySpellEffect(SpellType type, Monster target) {
        switch (type) {
            case FIRE:
                MonsterStatus fire = getStatus(target);
                fire.defenseModifier = Math.max(0.5, fire.defenseModifier * 0.9);
                break;
            case ICE:
                MonsterStatus ice = getStatus(target);
                ice.damageModifier = Math.max(0.5, ice.damageModifier * 0.9);
                break;
            case LIGHTNING:
                MonsterStatus lightning = getStatus(target);
                lightning.dodgeModifier = Math.max(0.5, lightning.dodgeModifier * 0.9);
                break;
            default:
                break;
        }
    }

    private void applyPotion(Hero hero, Potion potion) {
        for (PotionType type : potion.getAffectedTypes()) {
            switch (type) {
                case HEALTH:
                    hero.setHitPoints(hero.getHitPoints() + potion.getEffectAmount());
                    break;
                case MANA:
                    hero.setMana(hero.getMana() + potion.getEffectAmount());
                    break;
                case STRENGTH:
                    hero.getAttributes().setStrength(hero.getAttributes().getStrength() + potion.getEffectAmount());
                    break;
                case DEXTERITY:
                    hero.getAttributes().setDexterity(hero.getAttributes().getDexterity() + potion.getEffectAmount());
                    break;
                case AGILITY:
                    hero.getAttributes().setAgility(hero.getAttributes().getAgility() + potion.getEffectAmount());
                    break;
                default:
                    break;
            }
        }
    }

    private Monster selectMonsterTarget() {
        List<Monster> monsters = monsterGroup.getMonsters();
        List<Monster> alive = monsters.stream().filter(Monster::isAlive).collect(Collectors.toList());
        if (alive.isEmpty()) {
            return null;
        }
        if (alive.size() == 1) {
            return alive.get(0);
        }
        System.out.println("Choose target:");
        for (int i = 0; i < alive.size(); i++) {
            Monster m = alive.get(i);
            System.out.printf("%d) %s (HP: %d, LV: %d)%n", i + 1, m.getName(), m.getHitPoints(), m.getLevel());
        }
        int choice = input.readInt("Target: ", 1, alive.size());
        return alive.get(choice - 1);
    }

    private Hero selectHeroTarget() {
        List<Hero> heroes = heroParty.getHeroes();
        List<Hero> alive = heroes.stream().filter(Hero::isAlive).collect(Collectors.toList());
        if (alive.isEmpty()) {
            return null;
        }
        return alive.get(random.nextInt(alive.size()));
    }

    private void performMonsterRound() {
        for (Monster monster : monsterGroup.getMonsters()) {
            if (!monster.isAlive()) {
                continue;
            }
            Hero target = selectHeroTarget();
            if (target == null) {
                return;
            }
            double dodgeChance = Math.min(0.8, target.getAttributes().getAgility() * 0.001);
            if (dice.chance(dodgeChance)) {
                renderer.showMessage(target.getName() + " dodged " + monster.getName() + "'s attack.");
                continue;
            }
            int armorReduction = 0;
            Armor armor = target.getInventory().getEquipmentSet().getArmor();
            if (armor != null && !armor.isBroken()) {
                armorReduction = armor.getDamageReduction();
                if (armor.reduceUse()) {
                    renderer.showMessage(target.getName() + "'s " + armor.getName() + " broke!");
                }
            }
            MonsterStatus status = getStatus(monster);
            int damage = (int) (monster.getBaseDamage() * status.damageModifier * 0.5);
            damage = Math.max(0, damage - armorReduction);
            target.receiveDamage(damage);
            renderer.showMessage(monster.getName() + " attacked " + target.getName() + " for " + damage + " damage.");
            if (!target.isAlive()) {
                renderer.showMessage(target.getName() + " has fallen.");
            }
        }
    }

    private void recoverHeroes() {
        for (Hero hero : heroParty.getHeroes()) {
            if (!hero.isAlive()) {
                continue;
            }
            int maxHp = hero.getLevel() * 100;
            int maxMp = hero.getLevel() * 100;
            int newHp = (int) Math.min(maxHp, hero.getHitPoints() * 1.2);
            int newMana = (int) Math.min(maxMp, hero.getMana() * 1.2);
            hero.setHitPoints(newHp);
            hero.setMana(newMana);
        }
    }

    private void distributeRewards() {
        int monstersCount = monsterGroup.getMonsters().size();
        int highestLevel = monsterGroup.getMonsters().stream().mapToInt(Monster::getLevel).max().orElse(1);
        for (Hero hero : heroParty.getHeroes()) {
            if (hero.isAlive()) {
                hero.addGold(highestLevel * 100);
                hero.addExperience(monstersCount * 2);
                renderer.showMessage(hero.getName() + " gains " + (monstersCount * 2) + " XP and " + (highestLevel * 100) + " gold.");
            } else {
                // Revive at half HP and mana, no rewards.
                hero.setHitPoints((hero.getLevel() * 100) / 2);
                hero.setMana(Math.max(1, hero.getMana() / 2));
                renderer.showMessage(hero.getName() + " is revived with half HP and mana.");
            }
        }
    }

    private MonsterStatus getStatus(Monster monster) {
        return monsterStatusMap.computeIfAbsent(monster, m -> new MonsterStatus());
    }

    private static final class MonsterStatus {
        double damageModifier = 1.0;
        double defenseModifier = 1.0;
        double dodgeModifier = 1.0;
    }
}
