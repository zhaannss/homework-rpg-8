package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/** Baseline neutral state — no modifications, hero can always act. */
public class NormalState implements HeroState {
    @Override public String getName() { return "Normal"; }
    @Override public int modifyOutgoingDamage(int base) { return base; }
    @Override public int modifyIncomingDamage(int raw)  { return raw; }
    @Override public boolean canAct() { return true; }

    @Override public void onTurnStart(Hero hero) {
        System.out.println("  [" + hero.getName() + "] is in Normal state.");
    }
    @Override public void onTurnEnd(Hero hero) { /* nothing */ }
}
