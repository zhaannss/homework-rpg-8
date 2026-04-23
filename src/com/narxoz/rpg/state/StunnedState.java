package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * Debuff state where canAct() returns false — hero SKIPS their turn.
 * Self-transitions back to Normal after 1 turn.
 */
public class StunnedState implements HeroState {
    @Override public String getName() { return "Stunned"; }
    @Override public int modifyOutgoingDamage(int base) { return 0; }
    @Override public int modifyIncomingDamage(int raw)  { return (int)(raw * 1.2); }
    @Override public boolean canAct() { return false; } // CRITICAL: blocks hero action

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  [" + hero.getName() + "] is STUNNED and cannot act this turn!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        System.out.println("  [" + hero.getName() + "] shakes off the stun — back to Normal!");
        hero.setState(new NormalState()); // self-transition after 1 turn
    }
}
