package org.poo.cards;


import org.poo.main.Card;

public class Minion extends Card {
    public Minion(final Card card) {
        super(card.getCardInput(), card.getFrozen(), card.isUsedAttack(), card.isUsedAbility());
    }

    public Minion() {

    }

    /**
     * Selects the ability of an minion
     * @param attacker the minion that attacks
     * @param attacked the minion that is attacked
     */
    public void selectMinionAbility(final Minion attacker, final Minion attacked) {
        if (attacker.getName().equals("Disciple")) {
            discipleAbility(attacked);
        } else if (attacker.getName().equals("The Ripper")) {
            theRipperAbility(attacked);
        } else if (attacker.getName().equals("Miraj")) {
            mirajAbility(attacker, attacked);
        } else if (attacker.getName().equals("The Cursed One")) {
            theCursedOneAbility(attacked);
        }
    }

    /**
     * The ability of the Disciple to add 2 health to a minion
     * @param attacked the minion that is attacked
     */
    public void discipleAbility(final Minion attacked) {
        attacked.setHealth(attacked.getHealth() + 2);
    }

    /**
     * The ability of The Ripper to decrease the attack of a minion by 2
     * @param attacked the minion that is attacked
     */
    public void theRipperAbility(final Minion attacked) {
        if (attacked.getAttackDamage() < 2) {
            attacked.setAttackDamage(0);
        } else {
            attacked.setAttackDamage(attacked.getAttackDamage() - 2);
        }
    }

    /**
     * The ability of The Cursed One to swap the attack and the health of a minion
     * @param attacked the minion that is attacked
     */
    public void theCursedOneAbility(final Minion attacked) {
        int attack = attacked.getAttackDamage();
        int health = attacked.getHealth();
        attacked.setAttackDamage(health);
        attacked.setHealth(attack);
    }

    /**
     * The ability of Miraj to swap the health of two minions
     * @param attacker the minion that attacks
     * @param attacked the minion that is attacked
     */
    public void mirajAbility(final Minion attacker, final Minion attacked) {
        int attackerHealth = attacker.getHealth();
        int attackedHealth = attacked.getHealth();
        attacker.setHealth(attackedHealth);
        attacked.setHealth(attackerHealth);
    }
}
