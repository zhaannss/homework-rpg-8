package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * Buff/debuff state — boosts outgoing damage by 60% but also increases incoming damage by 30%.
 * Self-transitions back to Normal when hero HP drops below 20%.
 */
public class BerserkState implements HeroState {
    @Override public String getName() { return "Berserk"; }
    @Override public int modifyOutgoingDamage(int base) { return (int)(base * 1.6); }
    @Override public int modifyIncomingDamage(int raw)  { return (int)(raw * 1.3); }
    @Override public boolean canAct() { return true; }

    @Override
    public void onTurnStart(Hero hero) {
        System.out.println("  [" + hero.getName() + "] is BERSERK — damage +60%, defense -30%!");
        // Self-transition: if HP too low, berserk breaks
        if (hero.getHp() < hero.getMaxHp() * 0.2) {
            System.out.println("  [" + hero.getName() + "] is too weak to stay Berserk — returns to Normal!");
            hero.setState(new NormalState());
        }
    }

    @Override public void onTurnEnd(Hero hero) { /* no timer */ }
}
