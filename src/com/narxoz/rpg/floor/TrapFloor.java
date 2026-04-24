package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;
import java.util.List;

/**
 * Template Method subclass — a floor full of traps.
 * Causes state transitions: first hero gets Poisoned, second gets Stunned.
 */
public class TrapFloor extends TowerFloor {

    @Override protected String getFloorName() { return "Trap Corridor"; }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  [SETUP] The corridor is riddled with traps...");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [CHALLENGE] Traps spring from the walls!");
        int dmg = 0;
        List<Hero> alive = party.stream().filter(Hero::isAlive).toList();

        if (!alive.isEmpty()) {
            Hero h1 = alive.get(0);
            h1.takeDamage(10);
            dmg += 10;
            System.out.println("  Poison dart hits " + h1.getName() + "! (-10 HP, now Poisoned for 3 turns)");
            h1.setState(new PoisonedState(3)); // state transition triggered by game event
        }
        if (alive.size() >= 2) {
            Hero h2 = alive.get(1);
            h2.takeDamage(5);
            dmg += 5;
            System.out.println("  Shock trap hits " + h2.getName() + "! (-5 HP, now Stunned for 1 turn)");
            h2.setState(new StunnedState()); // state transition triggered by game event
        }

        return new FloorResult(true, dmg, "Survived the traps with injuries.");
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        System.out.println("  [LOOT] Found a small healing potion hidden behind a false wall.");
        party.stream().filter(Hero::isAlive).findFirst()
                .ifPresent(h -> { h.heal(10); System.out.println("  " + h.getName() + " heals 10 HP."); });
    }
}
