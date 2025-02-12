package org.poo.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.main.Card;


public class Hero extends Card {
    public Hero(final Card card) {
        super(card.getCardInput());
    }

    /**
     * Print the hero
     * @param hero the player's hero
     * @param heroNode the hero node
     * @param objectMapper
     * @return the hero node for the output
     */
    public ObjectNode printHero(final Hero hero, final ObjectNode heroNode,
                                final ObjectMapper objectMapper) {
        heroNode.put("mana", hero.getMana());
        heroNode.put("description", hero.getDescription());
        ArrayNode colorsArray = objectMapper.createArrayNode();
        for (String color : hero.getColors()) {
            colorsArray.add(color);
        }
        heroNode.set("colors", colorsArray);
        heroNode.put("name", hero.getName());
        heroNode.put("health", hero.getHealth());
        return heroNode;
    }

    /**
     * Select the hero's ability
     * @param hero the player's hero
     * @param minion the minion to apply the ability to
     */
    public void selectHeroAbility(final Hero hero, final Minion minion) {
        if (hero.getName().equals("Lord Royce")) {
            lordRoyceAbility(minion);
        } else if (hero.getName().equals("General Kocioraw")) {
            generalKociorawAbility(minion);
        } else if (hero.getName().equals("King Mudface")) {
            kingMudfaceAbility(minion);
        }
    }

    /**
     * Lord Royce ability to freeze a row of minions
     * @param minion the minion to apply the ability to
     */
    public void lordRoyceAbility(final Minion minion) {
        minion.setFrozen(1);
    }

    /**
     * General Kocioraw ability to increase the attack damage of a minion
     * @param minion the player's minion
     */
    public void generalKociorawAbility(final Minion minion) {
        minion.setAttackDamage(minion.getAttackDamage() + 1);
    }

    /**
     * King Mudface ability to increase the health of a minion
     * @param minion the player's minion
     */
    public void kingMudfaceAbility(final Minion minion) {
        minion.setHealth(minion.getHealth() + 1);
    }
}
