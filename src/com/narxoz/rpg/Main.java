package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.floor.*;
import com.narxoz.rpg.state.*;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   THE HAUNTED TOWER — ASCENDING THE FLOORS ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // ── 1. Create 2 heroes with different starting states ─────────────
        Hero aragorn = new Hero("Aragorn", 150, 40, 10);
        aragorn.setState(new BerserkState());          // starts Berserk

        Hero legolas = new Hero("Legolas", 120, 35, 8);
        legolas.setState(new RegenerationState(3));    // starts Regenerating

        List<Hero> party = List.of(aragorn, legolas);

        System.out.println("=== PARTY ===");
        for (Hero h : party) {
            System.out.println("  " + h.getName()
                    + " HP=" + h.getHp()
                    + " ATK=" + h.getAttackPower()
                    + " State=" + h.getState().getName());
        }

        // ── 2. Create ≥ 4 floors using ≥ 3 distinct subclass types ────────
        List<TowerFloor> floors = List.of(
                // Floor 1: CombatFloor — non-trivial resolveChallenge
                new CombatFloor("Skeleton Crypt", List.of(
                        new Monster("Skeleton Warrior", 60, 20),
                        new Monster("Bone Archer", 40, 15)
                )),
                // Floor 2: TrapFloor — triggers Poisoned + Stunned state transitions
                new TrapFloor(),
                // Floor 3: RestFloor — overrides announce(), shouldAwardLoot(), cleanup()
                new RestFloor(),
                // Floor 4: CombatFloor again (different monsters)
                new CombatFloor("Undead Barracks", List.of(
                        new Monster("Zombie Knight", 80, 25)
                )),
                // Floor 5: BossFloor — overrides announce(), triggers Berserk
                new BossFloor()
        );

        // ── 3. Run the tower ───────────────────────────────────────────────
        TowerRunner runner = new TowerRunner(party, floors);
        TowerRunResult result = runner.run();

        // ── 4. Print final TowerRunResult ──────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║           TOWER RUN COMPLETE              ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Floors cleared:   " + result.getFloorsCleared() + " / " + floors.size());
        System.out.println("  Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("  Tower conquered:  " + result.isReachedTop());
        for (Hero h : party) {
            String status = h.isAlive()
                    ? "Survived (HP=" + h.getHp() + ")"
                    : "Fell in battle";
            System.out.println("  " + h.getName() + ": " + status);
        }
    }
}
