package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * Self-transitioning debuff state.
 * - Reduces outgoing damage by 30%
 * - Applies 8 poison damage at turn start
 * - Wears off after 3 turns, transitioning back to Normal automatically.
 */
public class PoisonedState implements HeroState {
    private int turnsLeft;
    private static final int POISON_TICK = 8;

    public PoisonedState(int turns) { this.turnsLeft = turns; }

    @Override public String getName() { return "Poisoned(" + turnsLeft + ")"; }
    @Override public int modifyOutgoingDamage(int base) { return (int)(base * 0.7); }
    @Override public int modifyIncomingDamage(int raw)  { return raw; }
    @Override public boolean canAct() { return true; }

    @Override
    public void onTurnStart(Hero hero) {
        hero.takeDamage(POISON_TICK);
        System.out.println("  [" + hero.getName() + "] is POISONED — takes " + POISON_TICK
                + " poison damage! (HP=" + hero.getHp() + ", turns left=" + turnsLeft + ")");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsLeft--;
        if (turnsLeft <= 0) {
            System.out.println("  [" + hero.getName() + "] poison wears off — back to Normal!");
            hero.setState(new NormalState()); // self-transition
        }
    }
}
