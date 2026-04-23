package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;
import com.narxoz.rpg.state.NormalState;

/**
 * Represents a player-controlled hero participating in the tower climb.
 * Extended with HeroState support for the State pattern.
 */
public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;
    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = new NormalState();
    }

    public String getName()        { return name; }
    public int getHp()             { return hp; }
    public int getMaxHp()          { return maxHp; }
    public int getAttackPower()    { return attackPower; }
    public int getDefense()        { return defense; }
    public boolean isAlive()       { return hp > 0; }
    public HeroState getState()    { return state; }

    /** Transition to a new state at runtime. */
    public void setState(HeroState newState) {
        System.out.println("  [" + name + "] state: " + state.getName() + " → " + newState.getName());
        this.state = newState;
    }

    /**
     * Reduces HP by amount after state modification.
     * Applies modifyIncomingDamage from current state.
     */
    public void takeDamage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    /**
     * Apply incoming damage through state modifier (used by combat).
     */
    public void receiveAttack(int rawDamage) {
        int actual = Math.max(1, state.modifyIncomingDamage(rawDamage) - defense);
        hp = Math.max(0, hp - actual);
        System.out.println("  [" + name + "] takes " + actual + " damage (HP=" + hp + ")");
    }

    /** Effective outgoing damage through state modifier. */
    public int effectiveDamage() {
        return state.modifyOutgoingDamage(attackPower);
    }

    public void heal(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    /** Called at start of hero's turn — delegates to state. */
    public void onTurnStart() {
        state.onTurnStart(this);
    }

    /** Called at end of hero's turn — delegates to state. */
    public void onTurnEnd() {
        state.onTurnEnd(this);
    }

    /** Whether hero can act this turn — delegates to state. */
    public boolean canAct() {
        return state.canAct();
    }
}
