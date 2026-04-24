package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import java.util.ArrayList;
import java.util.List;

/**
 * Template Method subclass — combat floor with monsters.
 * Non-trivial resolveChallenge: heroes fight monsters in rounds, states apply.
 */
public class CombatFloor extends TowerFloor {

    private final String floorName;
    private final List<Monster> monsters = new ArrayList<>();
    private final List<Monster> monsterTemplates;

    public CombatFloor(String name, List<Monster> monsters) {
        this.floorName = name;
        this.monsterTemplates = monsters;
    }

    @Override protected String getFloorName() { return floorName; }

    @Override
    protected void setup(List<Hero> party) {
        monsters.clear();
        monsters.addAll(monsterTemplates);
        System.out.println("  [SETUP] " + monsters.size() + " monster(s) appear: "
                + monsters.stream().map(Monster::getName).toList());
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [CHALLENGE] Combat begins!");
        int totalDamage = 0;
        int round = 0;

        while (monsters.stream().anyMatch(Monster::isAlive)
                && party.stream().anyMatch(Hero::isAlive)) {
            round++;
            System.out.println("  -- Combat Round " + round + " --");

            // Heroes attack
            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.onTurnStart();
                if (!hero.isAlive()) continue; // might die from poison tick

                if (!hero.canAct()) {
                    hero.onTurnEnd();
                    continue; // stunned — skip
                }

                Monster target = monsters.stream().filter(Monster::isAlive).findFirst().orElse(null);
                if (target == null) break;

                int dmg = hero.effectiveDamage();
                target.takeDamage(dmg);
                System.out.println("  " + hero.getName() + " [" + hero.getState().getName()
                        + "] attacks " + target.getName() + " for " + dmg + " (monster HP=" + target.getHp() + ")");
                if (!target.isAlive()) System.out.println("  " + target.getName() + " DEFEATED!");

                hero.onTurnEnd();
            }

            // Monsters attack
            for (Monster m : monsters) {
                if (!m.isAlive()) continue;
                for (Hero hero : party) {
                    if (!hero.isAlive()) continue;
                    int raw = m.getAttackPower();
                    hero.receiveAttack(raw);
                    totalDamage += raw;
                    if (!hero.isAlive())
                        System.out.println("  " + hero.getName() + " has fallen!");
                }
            }
        }

        boolean cleared = monsters.stream().noneMatch(Monster::isAlive);
        return new FloorResult(cleared, totalDamage,
                cleared ? "All monsters defeated!" : "Party wiped out!");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) return;
        int heal = 15;
        System.out.println("  [LOOT] Victory! Each hero heals " + heal + " HP.");
        party.forEach(h -> { if (h.isAlive()) h.heal(heal); });
    }
}
