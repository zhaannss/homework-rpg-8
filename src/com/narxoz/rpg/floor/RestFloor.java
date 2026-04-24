package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.NormalState;
import java.util.List;

/**
 * Template Method subclass — a peaceful rest area.
 * Overrides announce() for custom message.
 * Overrides shouldAwardLoot() returning false (no loot on rest).
 * Overrides cleanup() to heal heroes and clear states.
 */
public class RestFloor extends TowerFloor {

    @Override protected String getFloorName() { return "Sanctuary of Rest"; }

    /** Hook override: custom announcement. */
    @Override
    protected void announce() {
        System.out.println("\n~~~ You find a peaceful sanctuary. The air feels restorative. ~~~");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("  [SETUP] The party prepares to rest...");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("  [CHALLENGE] The party rests safely. No enemies here.");
        int totalHeal = 30;
        party.stream().filter(Hero::isAlive).forEach(h -> {
            h.heal(totalHeal);
            System.out.println("  " + h.getName() + " rests and heals " + totalHeal + " HP. (HP=" + h.getHp() + ")");
        });
        return new FloorResult(true, 0, "Rested and recovered.");
    }

    /** Hook override: no loot on rest floors. */
    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        System.out.println("  [HOOK] shouldAwardLoot=false: rest floors give no loot.");
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        // Never called because shouldAwardLoot() returns false
    }

    /** Hook override: clear all states after rest. */
    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("  [CLEANUP] Rest clears all negative states.");
        party.stream().filter(Hero::isAlive).forEach(h -> {
            if (!(h.getState() instanceof NormalState)) {
                h.setState(new NormalState());
            }
        });
    }
}
