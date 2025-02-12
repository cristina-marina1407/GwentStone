package org.poo.main;

import org.poo.fileio.CardInput;
import java.util.ArrayList;


public class    Card {
    private CardInput cardInput;
    private int frozen = 0;
    private boolean isTank = false;
    private boolean usedAttack = false;
    private boolean usedAbility = false;

    public Card() {

    }

    public Card(final CardInput cardInput) {
        this.cardInput = new CardInput();
        this.cardInput.setMana(cardInput.getMana());
        this.cardInput.setAttackDamage((cardInput.getAttackDamage()));
        this.cardInput.setHealth(cardInput.getHealth());
        this.cardInput.setDescription(cardInput.getDescription());
        this.cardInput.setColors(cardInput.getColors());
        this.cardInput.setName(cardInput.getName());
        if (cardInput.getName().equals("Goliath") || cardInput.getName().equals("Warden")) {
            isTank = true;
        }
    }

    public Card(final CardInput cardInput, final int frozen, final boolean usedAttack,
                final boolean usedAbility) {
        this(cardInput);
        this.usedAttack = usedAttack;
        this.usedAbility = usedAbility;
        this.frozen = frozen;
    }

    /**
     * @return the mana of the card
     */
    public int getMana() {
        return cardInput.getMana();
    }

    /**
     * @param mana the mana to set
     */
    public void setMana(final int mana) {
        this.cardInput.setMana(mana);
    }

    /**
     * @return the attack damage of the card
     */
    public int getAttackDamage() {
        return cardInput.getAttackDamage();
    }

    /**
     * @param attackDamage the attack damage to set
     */
    public void setAttackDamage(final int attackDamage) {
        this.cardInput.setAttackDamage(attackDamage);
    }

    /**
     * @return the health of the card
     */
    public int getHealth() {
        return cardInput.getHealth();
    }

    /**
     * @param health the health to set
     */
    public void setHealth(final int health) {
        this.cardInput.setHealth(health);
    }

    /**
     * @return the description of the card
     */
    public String getDescription() {
        return cardInput.getDescription();
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.cardInput.setDescription(description);
    }

    /**
     * @return the colors of the card
     */
    public ArrayList<String> getColors() {
        return cardInput.getColors();
    }

    /**
     * @param colors the colors to set
     */
    public void setColors(final ArrayList<String> colors) {
        this.cardInput.setColors(colors);
    }

    /**
     * @return the name of the card
     */
    public String getName() {
        return cardInput.getName();
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.cardInput.setName(name);
    }

    /**
     * @return frozen variable of the card
     */
    public int getFrozen() {
        return frozen;
    }

    /**
     * @param frozen the frozen variable to set
     */
    public void setFrozen(final int frozen) {
        this.frozen = frozen;
    }

    /**
     * @return true if the card is a tank
     */
    public boolean isTank() {
        return isTank;
    }

    /**
     * @param tank the tank variable to set
     */
    public void setTank(final boolean tank) {
        isTank = tank;
    }

    /**
     * @return true if the card used attack
     */
    public boolean isUsedAttack() {
        return usedAttack;
    }

    /**
     * @param usedAttack the used attack variable to set
     */
    public void setUsedAttack(final boolean usedAttack) {
        this.usedAttack = usedAttack;
    }

    /**
     * @return true if the card used ability
     */
    public boolean isUsedAbility() {
        return usedAbility;
    }

    /**
     * @param usedAbility the used ability variable to set
     */
    public void setUsedAbility(final boolean usedAbility) {
        this.usedAbility = usedAbility;
    }

    /**
     * @return the card input
     */
    public CardInput getCardInput() {
        return cardInput;
    }

    /**
     * @param cardInput the card input to set
     */
    public void setCardInput(final CardInput cardInput) {
        this.cardInput = cardInput;
    }
}




