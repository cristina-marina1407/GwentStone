package org.poo.gameManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.CardInput;
import org.poo.fileio.Input;
import org.poo.fileio.StartGameInput;
import org.poo.main.Card;

import java.util.ArrayList;
import static org.poo.gameManagement.Constants.MAX_HEALTH;


public final class Player {
    private int mana;
    private int player;
    private ArrayList<CardInput> deckInput;
    private ArrayList<Minion> deck = new ArrayList<>();
    private Hero hero;

    public Player(final int mana, final int player,
                  final Input input, final StartGameInput startGameInput) {
        this.mana = mana;
        this.player = player;

        // get the deck and hero for the player
        if (player == 1) {
            this.deckInput = input.getPlayerOneDecks().getDecks()
                    .get(startGameInput.getPlayerOneDeckIdx());

            for (CardInput cardInput : deckInput) {
                Card card = new Card(cardInput);
                Minion minion = new Minion(card);
                deck.add(minion);
            }

            CardInput heroInput = startGameInput.getPlayerOneHero();
            hero = new Hero(new Card(heroInput));
            hero.setHealth(MAX_HEALTH);
        } else {
            this.deckInput = input.getPlayerTwoDecks().getDecks()
                    .get(startGameInput.getPlayerTwoDeckIdx());

            for (CardInput cardInput : deckInput) {
                Card card = new Card(cardInput);
                Minion minion = new Minion(card);
                deck.add(minion);
            }

            CardInput heroInput = startGameInput.getPlayerTwoHero();
            hero = new Hero(new Card(heroInput));
            hero.setHealth(MAX_HEALTH);
        }
    }

    /**
     * Print the deck of the player
     * @param currentPlayer the player
     * @param playerDeck  the deck
     * @param objectMapper
     * @return the deck of the player
     */
    public ArrayNode printDeck(final Player currentPlayer, final ArrayNode playerDeck,
                               final ObjectMapper objectMapper) {
        for (Minion card : currentPlayer.getDeck()) {
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());
            ArrayNode colorsArray = objectMapper.createArrayNode();
            for (String color : card.getColors()) {
                colorsArray.add(color);
            }
            cardNode.set("colors", colorsArray);
            cardNode.put("name", card.getName());
            playerDeck.add(cardNode);
        }
        return playerDeck;

    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<Minion> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<Minion> deck) {
        this.deck = deck;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(final int player) {
        this.player = player;
    }
}
