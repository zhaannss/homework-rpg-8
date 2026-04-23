package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

/**
 * Buff state — heals 12 HP at turn start.
 * Self-transitions back to Normal after 3 turns.
 */
public class RegenerationState implements HeroState {
    private int turnsLeft;
    private static final int HEAL_TICK = 12;

    public RegenerationState(int turns) { this.turnsLeft = turns; }

    @Override public String getName() { return "Regeneration(" + turnsLeft + ")"; }
    @Override public int modifyOutgoingDamage(int base) { return base; }
    @Override public int modifyIncomingDamage(int raw)  { return (int)(raw * 0.9); }
    @Override public boolean canAct() { return true; }

    @Override
    public void onTurnStart(Hero hero) {
        hero.heal(HEAL_TICK);
        System.out.println("  [" + hero.getName() + "] REGENERATES " + HEAL_TICK
                + " HP! (HP=" + hero.getHp() + ", turns left=" + turnsLeft + ")");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsLeft--;
        if (turnsLeft <= 0) {
            System.out.println("  [" + hero.getName() + "] regeneration fades — back to Normal!");
            hero.setState(new NormalState());
        }
    }
}
