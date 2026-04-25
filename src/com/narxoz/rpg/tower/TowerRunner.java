package com.narxoz.rpg.tower;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.FloorResult;
import com.narxoz.rpg.floor.TowerFloor;
import java.util.List;

/**
 * Executes a sequence of floors in order, tracking progress.
 */
public class TowerRunner {

    private final List<Hero> party;
    private final List<TowerFloor> floors;

    public TowerRunner(List<Hero> party, List<TowerFloor> floors) {
        this.party = party;
        this.floors = floors;
    }

    public TowerRunResult run() {
        int floorsCleared = 0;

        for (TowerFloor floor : floors) {
            // Stop if no heroes alive
            if (party.stream().noneMatch(Hero::isAlive)) {
                System.out.println("\n  All heroes have fallen. Tower climb ends.");
                break;
            }

            // Template Method called here — explore() is final
            FloorResult result = floor.explore(party);

            System.out.println("  [RESULT] " + result.getSummary()
                    + " | Damage taken: " + result.getDamageTaken()
                    + " | Cleared: " + result.isCleared());

            if (result.isCleared()) floorsCleared++;

            // Print party status after each floor
            System.out.println("  [PARTY STATUS]");
            for (Hero h : party) {
                String status = h.isAlive()
                        ? "HP=" + h.getHp() + "/" + h.getMaxHp() + " State=" + h.getState().getName()
                        : "DEAD";
                System.out.println("    " + h.getName() + ": " + status);
            }
        }

        int survivors = (int) party.stream().filter(Hero::isAlive).count();
        boolean reachedTop = floorsCleared == floors.size();

        return new TowerRunResult(floorsCleared, survivors, reachedTop);
    }
}
