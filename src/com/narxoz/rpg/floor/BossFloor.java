package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;
import java.util.List;

/**
 * Template Method subclass — the final boss floor.
 * Overrides announce() with intimidating message.
 * resolveChallenge() triggers Berserk state on heroes who go low HP.
 */
public class BossFloor extends TowerFloor {

    private Monster boss;

    @Override protected String getFloorName() { return "Dragon's Lair — FINAL BOSS"; }

    /** Hook override: intimidating boss announcement. */
    @Override
    protected void announce() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║   !!  BOSS FLOOR: DRAGON'S LAIR  !!  ║");
        System.out.println("║   The ground trembles beneath you...  ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster("Ancient Dragon", 200, 35);
        System.out.println("  [SETUP] " + boss.getName()
                + " awakens! HP=" + boss.getHp() + " ATK=" + boss.getAttackPower());
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [CHALLENGE] Boss fight begins!");
        int totalDamage = 0;
        int round = 0;

        while (boss.isAlive() && party.stream().anyMatch(Hero::isAlive)) {
            round++;
            System.out.println("  -- Boss Round " + round + " --");

            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) continue;
                hero.onTurnStart();
                if (!hero.isAlive()) continue;

                if (!hero.canAct()) {
                    hero.onTurnEnd();
                    continue;
                }

                // If HP below 30% and not already Berserk — go Berserk!
                if (hero.getHp() < hero.getMaxHp() * 0.3
                        && !(hero.getState() instanceof BerserkState)) {
                    System.out.println("  " + hero.getName() + " is near death — goes BERSERK!");
                    hero.setState(new BerserkState());
                }

                int dmg = hero.effectiveDamage();
                boss.takeDamage(dmg);
                System.out.println("  " + hero.getName() + " [" + hero.getState().getName()
                        + "] hits boss for " + dmg + " (boss HP=" + boss.getHp() + ")");
                if (!boss.isAlive()) System.out.println("  " + boss.getName() + " DEFEATED!");

                hero.onTurnEnd();
            }

            if (!boss.isAlive()) break;

            for (Hero hero : party) {
                if (!hero.isAlive()) continue;
                hero.receiveAttack(boss.getAttackPower());
                totalDamage += boss.getAttackPower();
                if (!hero.isAlive()) System.out.println("  " + hero.getName() + " has fallen!");
            }
        }

        boolean cleared = !boss.isAlive();
        return new FloorResult(cleared, totalDamage,
                cleared ? "Ancient Dragon slain!" : "Party defeated by the Dragon!");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) return;
        System.out.println("  [LOOT] LEGENDARY: Dragon Heart Gem obtained! All heroes fully healed!");
        party.stream().filter(Hero::isAlive).forEach(h -> h.heal(h.getMaxHp()));
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("  [CLEANUP] The Dragon's lair crumbles. The tower is conquered.");
    }
}
