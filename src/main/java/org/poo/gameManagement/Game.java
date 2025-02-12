package org.poo.gameManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;
import org.poo.fileio.StartGameInput;
import org.poo.main.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.poo.gameManagement.Constants.FIRST_ROW_PLAYER1;
import static org.poo.gameManagement.Constants.FIRST_ROW_PLAYER2;
import static org.poo.gameManagement.Constants.MAX_GAME_MANA;
import static org.poo.gameManagement.Constants.MAX_ROW;
import static org.poo.gameManagement.Constants.SECOND_ROW_PLAYER1;
import static org.poo.gameManagement.Constants.SECOND_ROW_PLAYER2;


public final class Game {
    private Input input;
    private StartGameInput startGameInput;
    private ArrayNode output;
    private ObjectMapper objectMapper;

    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private GameInput currentSession;
    private int startingTurn;
    private int currentTurn;
    private Table table;
    private int gameMana;
    private int playerOneWins;
    private int playerTwoWins;
    private int numberOfGamesPlayed;
    private boolean heroOneUsedAbility;
    private boolean heroTwoUsedAbility;


    public Game(final Input inputData, final ArrayNode output, final ObjectMapper objectMapper) {
        this.input = inputData;
        this.output = output;
        this.objectMapper = objectMapper;
        currentSession = inputData.getGames().get(0);
        this.startGameInput = currentSession.getStartGame();
        this.playerOneWins = 0;
        this.playerTwoWins = 0;
        this.numberOfGamesPlayed = 0;
        this.heroOneUsedAbility = false;
        this.heroTwoUsedAbility = false;
        generateGameSessions();
    }

    /**
     * Generates the game sessions and starts every game.
     */
    private void generateGameSessions() {
        for (GameInput session : input.getGames()) {
            currentSession = session;
            startGameInput = currentSession.getStartGame();
            playerOne = new Player(1, 1, input, startGameInput);
            playerTwo = new Player(1, 2, input, startGameInput);

            if (session.getStartGame().getStartingPlayer() == 1) {
                currentPlayer = playerOne;
            } else {
                currentPlayer = playerTwo;
            }

            table = new Table();

            int shuffleSeed = currentSession.getStartGame().getShuffleSeed();
            Random random1 = new Random(shuffleSeed);
            Random random2 = new Random(shuffleSeed);
            Collections.shuffle(playerOne.getDeck(), random1);
            Collections.shuffle(playerTwo.getDeck(), random2);

            startingTurn = currentSession.getStartGame().getStartingPlayer();
            currentTurn = currentSession.getStartGame().getStartingPlayer();

            gameMana = 1;
            if (!playerOne.getDeck().isEmpty()) {
                addCardInHand(1, playerOne);
            }
            if (!playerTwo.getDeck().isEmpty()) {
                addCardInHand(2, playerTwo);
            }
            numberOfGamesPlayed++;
            for (ActionsInput action : session.getActions()) {
                command(action);
            }
        }
    }

    /**
     * Executes the command given by the input.
     * @param actionsInput the input command
     */
    public void command(final ActionsInput actionsInput) {
        String command = actionsInput.getCommand();
        switch (command) {
            case "getPlayerDeck":
                int playerNumber = actionsInput.getPlayerIdx();
                ObjectNode newNode = getPlayerDeck(playerNumber);
                output.add(newNode);
                break;
            case "getPlayerHero":
                playerNumber = actionsInput.getPlayerIdx();
                ObjectNode heroNode = getPlayerHero(playerNumber);
                output.add(heroNode);
                break;
            case "getPlayerTurn":
                ObjectNode turnNode = getPlayerTurn();
                output.add(turnNode);
                break;
            case "getCardsInHand":
                playerNumber = actionsInput.getPlayerIdx();
                ObjectNode handNode = getCardsInHand(playerNumber);
                output.add(handNode);
                break;
            case "endPlayerTurn":
                endPlayerTurn();
                break;
            case "placeCard":
                int handIndex = actionsInput.getHandIdx();
                ObjectNode cardNode = placeCard(currentPlayer.getPlayer(), handIndex);
                if (!cardNode.isEmpty()) {
                    output.add(cardNode);
                }
                break;
            case "getCardsOnTable":
                newNode = getCardsOnTable();
                output.add(newNode);
                break;
            case "getPlayerMana":
                playerNumber = actionsInput.getPlayerIdx();
                ObjectNode manaNode = getPlayerMana(playerNumber);
                output.add(manaNode);
                break;
            case "cardUsesAttack":
                int x1 = actionsInput.getCardAttacker().getX();
                int y1 = actionsInput.getCardAttacker().getY();
                int x2 = actionsInput.getCardAttacked().getX();
                int y2 = actionsInput.getCardAttacked().getY();
                ObjectNode attackNode = cardUsesAttack(x1, y1, x2, y2);
                if (!attackNode.isEmpty()) {
                    output.add(attackNode);
                }
                break;
            case "getCardAtPosition":
                int x = actionsInput.getX();
                int y = actionsInput.getY();
                ObjectNode positionNode = getCardAtPosition(x, y);
                output.add(positionNode);
                break;
            case "cardUsesAbility":
                x1 = actionsInput.getCardAttacker().getX();
                y1 = actionsInput.getCardAttacker().getY();
                x2 = actionsInput.getCardAttacked().getX();
                y2 = actionsInput.getCardAttacked().getY();
                ObjectNode abilityNode = cardUsesAbility(x1, y1, x2, y2);
                if (!abilityNode.isEmpty()) {
                    output.add(abilityNode);
                }
                break;
            case "useAttackHero":
                x = actionsInput.getCardAttacker().getX();
                y = actionsInput.getCardAttacker().getY();
                ObjectNode attackHeroNode = usesAttackHero(x, y);
                if (!attackHeroNode.isEmpty()) {
                    output.add(attackHeroNode);
                }
                break;
            case "useHeroAbility":
                int affectedRow = actionsInput.getAffectedRow();
                ObjectNode abilityHeroNode = useHeroAbility(affectedRow);
                if (!abilityHeroNode.isEmpty()) {
                    output.add(abilityHeroNode);
                }
                break;
            case "getFrozenCardsOnTable":
                newNode = getFrozenCardsOnTable();
                output.add(newNode);
                break;
            case "getTotalGamesPlayed":
                ObjectNode gamesNode = getTotalGamesPlayed();
                output.add(gamesNode);
                break;
            case "getPlayerOneWins":
                ObjectNode playerOneNode = getPlayerOneWins();
                output.add(playerOneNode);
                break;
            case "getPlayerTwoWins":
                ObjectNode playerTwoNode = getPlayerTwoWins();
                output.add(playerTwoNode);
                break;
            default:
                break;
        }
    }

    /**
     * @param index the player's index
     * @return the player's deck
     */
    private ObjectNode getPlayerDeck(final int index) {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getPlayerDeck");
        newNode.put("playerIdx", index);
        ArrayNode deck = objectMapper.createArrayNode();

        if (index == 1) {
            playerOne.printDeck(playerOne, deck, objectMapper);
            newNode.set("output", deck);
        } else {
            playerTwo.printDeck(playerTwo, deck, objectMapper);
            newNode.set("output", deck);
        }
        return newNode;
    }

    /**
     * @param index the player's index
     * @return the player's hero
     */
    private ObjectNode getPlayerHero(final int index) {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getPlayerHero");
        newNode.put("playerIdx", index);
        Hero hero;

        if (index == 1) {
            hero = playerOne.getHero();
        } else {
            hero = playerTwo.getHero();
        }

        ObjectNode heroNode = objectMapper.createObjectNode();
        hero.printHero(hero, heroNode, objectMapper);
        newNode.set("output", heroNode);
        return newNode;
    }

    /**
     * @return the player's turn
     */
    private ObjectNode getPlayerTurn() {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getPlayerTurn");
        newNode.put("output", currentTurn);
        return newNode;
    }

    /**
     * Adds a card in the player's hand.
     * @param index the player's index
     * @param player the player
     */
    private void addCardInHand(final int index, final Player player) {
        Minion card = player.getDeck().get(0);
        // remove the card from the deck
        player.getDeck().remove(0);
        table.getHand(index).add(card);
    }

    /**
     * @param index the player's index
     * @return the cards in the player's hand
     */
    private ObjectNode getCardsInHand(final int index) {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getCardsInHand");
        newNode.put("playerIdx", index);

        ArrayList<Minion> playerHand;
        playerHand = table.getHand(index);
        ArrayNode hand = table.printHand(playerHand, objectMapper);

        newNode.set("output", hand);
        return newNode;
    }

    /**
     * Switches the player's turn.
     */
    private void endPlayerTurn() {
        // reset the usedAttack and usedAbility variables for the player's cards on the table
        // and unfreeze the frozen cards
        if (currentTurn == 1) {
            for (int i = FIRST_ROW_PLAYER1; i <= SECOND_ROW_PLAYER1; i++) {
                for (Card card : table.getTable().get(i)) {
                    card.setUsedAttack(false);
                    card.setUsedAbility(false);
                    card.setFrozen(0);
                }
            }
            currentTurn = 2;
            currentPlayer = playerTwo;
            heroOneUsedAbility = false;
        } else if (currentTurn == 2) {
            for (int i = SECOND_ROW_PLAYER2; i <= FIRST_ROW_PLAYER2; i++) {
                for (Minion card : table.getTable().get(i)) {
                    card.setUsedAttack(false);
                    card.setUsedAbility(false);
                    card.setFrozen(0);
                }
            }
            currentTurn = 1;
            currentPlayer = playerOne;
            heroTwoUsedAbility = false;
        }

        // check if the round is over, so the game mana can be increased
        //and the players can draw another card from the deck
        if (currentTurn == startingTurn) {
            if (!playerOne.getDeck().isEmpty()) {
                addCardInHand(1, playerOne);
            }
            if (!playerTwo.getDeck().isEmpty()) {
                addCardInHand(2, playerTwo);
            }
            if (gameMana < MAX_GAME_MANA) {
                gameMana++;
            }
            playerOne.setMana(playerOne.getMana() + gameMana);
            playerTwo.setMana(playerTwo.getMana() + gameMana);
        }
    }

    /*/
     * Places a card on the table.
     * @param index the player's index
     * @param handIndex the index of the card in the player's hand
     * @return the result of the action in case of an error
     */
    private ObjectNode placeCard(final int index, final int handIndex) {
        ArrayList<Minion> hand = table.getHand(index);
        ObjectNode resultNode = objectMapper.createObjectNode();

        // check if the player has enough mana to place the card on the table
        Minion card = hand.get(handIndex);
        if (card.getMana() > currentPlayer.getMana()) {
            resultNode.put("command", "placeCard");
            resultNode.put("handIdx", handIndex);
            resultNode.put("error", "Not enough mana to place card on table.");
            return resultNode;
        }

        // check if there are any empty positions on the table
        if (table.getNumberOfRows() >= MAX_ROW) {
            resultNode.put("command", "placeCard");
            resultNode.put("playerIdx", index);
            resultNode.put("handIdx", handIndex);
            resultNode.put("error", "Cannot place card on table since row is full.");
            return resultNode;
        }

        // setting the rows based on the current player
        int firstRow;
        int secondRow;
        if (currentTurn == 1) {
            firstRow = FIRST_ROW_PLAYER1;
            secondRow = SECOND_ROW_PLAYER1;
        } else {
            firstRow = FIRST_ROW_PLAYER2;
            secondRow = SECOND_ROW_PLAYER2;
        }

        // adding the tanks on the first row before adding any other cards
        if (card.getName().equals("Warden") || card.getName().equals("Goliath")
                || card.getName().equals("The Ripper") || card.getName().equals("Miraj")) {
            table.getTable().get(firstRow).add(card);
        } else {
            table.getTable().get(secondRow).add(card);
        }

        // remove the card from the player's hand
        hand.remove(handIndex);
        // decrease the player's mana
        currentPlayer.setMana(currentPlayer.getMana() - card.getMana());
        return resultNode;
    }

    /**
     * @return the cards on the table
     */
    private ObjectNode getCardsOnTable() {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getCardsOnTable");
        ArrayNode tablePrint = table.printTable(table, objectMapper);
        newNode.set("output", tablePrint);
        return newNode;
    }

    /**
     * @param index the player's index
     * @return the player's mana
     */
    private ObjectNode getPlayerMana(final int index) {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getPlayerMana");

        if (index == 1) {
            newNode.put("output", playerOne.getMana());
            newNode.put("playerIdx", playerOne.getPlayer());
            return newNode;
        } else {
            newNode.put("output", playerTwo.getMana());
            newNode.put("playerIdx", playerTwo.getPlayer());
            return newNode;
        }
    }

    /**
     * Performs an attack between two cards.
     * @param x1 the x coordinate of the attacker card
     * @param y1 the y coordinate of the attacker card
     * @param x2 the x coordinate of the attacked card
     * @param y2 the y coordinate of the attacked card
     * @return the result of the action in case of an error
     */
    private ObjectNode cardUsesAttack(final int x1, final int y1, final int x2, final int y2) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        //get the attacker and attacked cards from the table
        Minion cardAttacker = table.getTable().get(x1).get(y1);
        Minion cardAttacked = table.getTable().get(x2).get(y2);

        if (currentTurn == 1) {
            //check if the attacked card belongs to the enemy
            if (x2 == FIRST_ROW_PLAYER1 || x2 == SECOND_ROW_PLAYER1) {
                resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                resultNode.put("command", "cardUsesAttack");
                resultNode.put("error", "Attacked card does not belong to the enemy.");
                return resultNode;
            }

            //check if the player has tanks on the table and if the attacked card is one of them
            boolean check = table.checkTanks(table, SECOND_ROW_PLAYER2, FIRST_ROW_PLAYER2);
            if (check && !cardAttacked.isTank()) {
                resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                resultNode.put("command", "cardUsesAttack");
                resultNode.put("error", "Attacked card is not of type 'Tank'.");
                return resultNode;
            }

        } else {
            //check if the attacked card belongs to the enemy
            if (x2 == SECOND_ROW_PLAYER2 || x2 == FIRST_ROW_PLAYER2) {
                resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                resultNode.put("command", "cardUsesAttack");
                resultNode.put("error", "Attacked card does not belong to the enemy.");
                return resultNode;
            }
            //check if the player has tanks on the table and if the attacked card is one of them
            boolean verif = table.checkTanks(table, FIRST_ROW_PLAYER1, SECOND_ROW_PLAYER1);
            if (verif && !cardAttacked.isTank()) {
                resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                resultNode.put("command", "cardUsesAttack");
                resultNode.put("error", "Attacked card is not of type 'Tank'.");
                return resultNode;
            }
        }

        //check if the attacker card has already attacked this turn
        if (cardAttacker.isUsedAttack() || cardAttacker.isUsedAbility()) {
            resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
            resultNode.put("command", "cardUsesAttack");
            resultNode.put("error", "Attacker card has already attacked this turn.");
            return resultNode;
        }

        //check if the attacker card is frozen
        if (cardAttacker.getFrozen() == 1) {
            resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
            resultNode.put("command", "cardUsesAttack");
            resultNode.put("error", "Attacker card is frozen.");
            return resultNode;
        }

        // the attacker card attacks the attacked card
        cardAttacked.setHealth(cardAttacked.getHealth() - cardAttacker.getAttackDamage());

        //check if the attacked card has died
        if (cardAttacked.getHealth() <= 0) {
            table.getTable().get(x2).remove(cardAttacked);
        }

        cardAttacker.setUsedAttack(true);
        return resultNode;
    }

    /**
     * @param x the x coordinate of the card
     * @param y the y coordinate of the card
     * @return the card at the given position
     */
    private ObjectNode getCardAtPosition(final int x, final int y) {
        ObjectNode newNode = objectMapper.createObjectNode();

        // check if the given coordinates are valid
        if (x < 0 || x >= table.getTable().size()) {
            newNode.put("command", "getCardAtPosition");
            newNode.put("output", "No card available at that position.");
            newNode.put("x", x);
            newNode.put("y", y);
            return newNode;
        }
        if (y < 0 || y >= table.getTable().get(x).size()) {
            newNode.put("command", "getCardAtPosition");
            newNode.put("output", "No card available at that position.");
            newNode.put("x", x);
            newNode.put("y", y);
            return newNode;
        }

        // print the card at the given position
        Card card = table.getTable().get(x).get(y);
        newNode.put("command", "getCardAtPosition");
        if (card == null) {
            newNode.put("output", "No card available at that position.");
            newNode.put("x", x);
            newNode.put("y", y);
            return newNode;
        } else {
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("attackDamage", card.getAttackDamage());
            ArrayNode colorsArray = objectMapper.createArrayNode();
            for (String color : card.getColors()) {
                colorsArray.add(color);
            }
            cardNode.set("colors", colorsArray);
            cardNode.put("description", card.getDescription());
            cardNode.put("health", card.getHealth());
            cardNode.put("mana", card.getMana());
            cardNode.put("name", card.getName());
            newNode.set("output", cardNode);
            newNode.put("x", x);
            newNode.put("y", y);
            return newNode;
        }
    }

    /**
     * Uses the ability of a card on another card.
     * @param x1 the x coordinate of the attacker card
     * @param y1 the y coordinate of the attacker card
     * @param x2 the x coordinate of the attacked card
     * @param y2 the y coordinate of the attacked card
     * @return the result of the action in case of an error
     */
    private ObjectNode cardUsesAbility(final int x1, final int y1, final int x2, final int y2) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        Minion cardAttacker = table.getTable().get(x1).get(y1);
        Minion cardAttacked = table.getTable().get(x2).get(y2);

        // check if the attacker card is frozen
        if (cardAttacker.getFrozen() == 1) {
            resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
            resultNode.put("command", "cardUsesAbility");
            resultNode.put("error", "Attacker card is frozen.");
            return resultNode;
        }

        // check if the attacker card has already attacked this turn
        if (cardAttacker.isUsedAttack() || cardAttacker.isUsedAbility()) {
            resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
            resultNode.put("command", "cardUsesAbility");
            resultNode.put("error", "Attacker card has already attacked this turn.");
            return resultNode;
        }

        // if the attacker card is Disciple, its ability can be used on the current player's cards,
        // because is in their benefit
        if (cardAttacker.getName().equals("Disciple")) {
            if (currentTurn == 1) {
                if (x2 == SECOND_ROW_PLAYER2 || x2 == FIRST_ROW_PLAYER2) {
                    resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                    resultNode.put("command", "cardUsesAbility");
                    resultNode.put("error", "Attacked card does not belong to the current player.");
                    return resultNode;
                }
            } else {
                if (x2 == FIRST_ROW_PLAYER1 || x2 == SECOND_ROW_PLAYER1) {
                    resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                    resultNode.put("command", "cardUsesAbility");
                    resultNode.put("error", "Attacked card does not belong to the current player.");
                    return resultNode;
                }
            }
            // the other cards can only use their ability on the enemy's cards
        } else if (cardAttacker.getName().equals("The Ripper")
                || cardAttacker.getName().equals("Miraj")
                || cardAttacker.getName().equals("The Cursed One")) {
            if (currentTurn == 1) {
                if (x2 == FIRST_ROW_PLAYER1 || x2 == SECOND_ROW_PLAYER1) {
                    resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                    resultNode.put("command", "cardUsesAbility");
                    resultNode.put("error", "Attacked card does not belong to the enemy.");
                    return resultNode;
                }
                boolean verif = table.checkTanks(table, SECOND_ROW_PLAYER2, FIRST_ROW_PLAYER2);
                if (verif && !cardAttacked.isTank()) {
                    resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                    resultNode.put("command", "cardUsesAbility");
                    resultNode.put("error", "Attacked card is not of type 'Tank'.");
                    return resultNode;
                }
            } else {
                if (x2 == SECOND_ROW_PLAYER2 || x2 == FIRST_ROW_PLAYER2) {
                    resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                    resultNode.put("command", "cardUsesAbility");
                    resultNode.put("error", "Attacked card does not belong to the enemy.");
                    return resultNode;
                }
                boolean verif = table.checkTanks(table, FIRST_ROW_PLAYER1, SECOND_ROW_PLAYER1);
                if (verif && !cardAttacked.isTank()) {
                    resultNode = table.attackCardOutput(resultNode, objectMapper, x1, y1, x2, y2);
                    resultNode.put("command", "cardUsesAbility");
                    resultNode.put("error", "Attacked card is not of type 'Tank'.");
                    return resultNode;
                }
            }
        }

        // use the attacker card's ability on the attacked card
        cardAttacker.selectMinionAbility(cardAttacker, cardAttacked);

        // check if the attacked card has died
        if (cardAttacked.getHealth() <= 0) {
            table.getTable().get(x2).remove(cardAttacked);
        }

        cardAttacker.setUsedAbility(true);
        return resultNode;
    }

    /**
     * Performs an attack on the enemy hero.
      * @param x the x coordinate of the attacker card
     * @param y the y coordinate of the attacker card
     * @return the result of the action in case of an error
     */
    private ObjectNode usesAttackHero(final int x, final int y) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        Minion cardAttacker = table.getTable().get(x).get(y);
        Hero hero;

        // get the enemy hero
        if (currentTurn == 1) {
            hero = playerTwo.getHero();
        } else {
            hero = playerOne.getHero();
        }

        if (cardAttacker.getFrozen() == 1) {
            resultNode = table.attackHeroOutput(resultNode, objectMapper, x, y);
            resultNode.put("command", "useAttackHero");
            resultNode.put("error", "Attacker card is frozen.");
            return resultNode;
        }

        if (cardAttacker.isUsedAttack() || cardAttacker.isUsedAbility()) {
            resultNode = table.attackHeroOutput(resultNode, objectMapper, x, y);
            resultNode.put("command", "useAttackHero");
            resultNode.put("error", "Attacker card has already attacked this turn.");
            return resultNode;
        }

        // check if the enemy player has tanks on the table that have to be attacked first
        if (currentTurn == 1) {
            boolean verif = table.checkTanks(table, SECOND_ROW_PLAYER2, FIRST_ROW_PLAYER2);
            if (verif) {
                resultNode = table.attackHeroOutput(resultNode, objectMapper, x, y);
                resultNode.put("command", "useAttackHero");
                resultNode.put("error", "Attacked card is not of type 'Tank'.");
                return resultNode;
            }
        } else {
            boolean verif = table.checkTanks(table, FIRST_ROW_PLAYER1, SECOND_ROW_PLAYER1);
            if (verif) {
                resultNode = table.attackHeroOutput(resultNode, objectMapper, x, y);
                resultNode.put("command", "useAttackHero");
                resultNode.put("error", "Attacked card is not of type 'Tank'.");
                return resultNode;
            }
        }
        // the attacker card attacks the enemy hero
        hero.setHealth(hero.getHealth() - cardAttacker.getAttackDamage());

        // check if the enemy hero has died which means the game has ended
        if (hero.getHealth() <= 0) {
            if (currentTurn == 1) {
                resultNode.put("gameEnded", "Player one killed the enemy hero.");
                playerOneWins++;
            } else {
                resultNode.put("gameEnded", "Player two killed the enemy hero.");
                playerTwoWins++;
            }
        }

        cardAttacker.setUsedAttack(true);
        return resultNode;
    }

    /**
     * Uses the hero's ability on a row.
     * @param affectedRow the row that is affected by the hero's ability
     * @return the result of the action in case of an error
     */
    private ObjectNode useHeroAbility(final int affectedRow) {
        ObjectNode resultNode = objectMapper.createObjectNode();

        // get the current player's hero
        Hero hero;
        if (currentTurn == 1) {
            hero = playerOne.getHero();
        } else {
            hero = playerTwo.getHero();
        }

        // check if the player has enough mana to use the hero's ability
        if (hero.getMana() > currentPlayer.getMana()) {
            resultNode.put("command", "useHeroAbility");
            resultNode.put("affectedRow", affectedRow);
            resultNode.put("error", "Not enough mana to use hero's ability.");
            return resultNode;
        }

        // check if the hero has already used its ability this turn
        if (currentTurn == 1) {
            if (heroOneUsedAbility) {
                resultNode.put("command", "useHeroAbility");
                resultNode.put("affectedRow", affectedRow);
                resultNode.put("error", "Hero has already attacked this turn.");
                return resultNode;
            }
        } else {
            if (heroTwoUsedAbility) {
                resultNode.put("command", "useHeroAbility");
                resultNode.put("affectedRow", affectedRow);
                resultNode.put("error", "Hero has already attacked this turn.");
                return resultNode;
            }
        }

        // check if the selected row belongs to the current player or to the enemy
        if (hero.getName().equals("Lord Royce") || hero.getName().equals("Empress Thorina")) {
            if (currentTurn == 1) {
                if (affectedRow == FIRST_ROW_PLAYER1 || affectedRow == SECOND_ROW_PLAYER1) {
                    resultNode.put("command", "useHeroAbility");
                    resultNode.put("affectedRow", affectedRow);
                    resultNode.put("error", "Selected row does not belong to the enemy.");
                    return resultNode;
                }
            } else {
                if (affectedRow == SECOND_ROW_PLAYER2 || affectedRow == FIRST_ROW_PLAYER2) {
                    resultNode.put("command", "useHeroAbility");
                    resultNode.put("affectedRow", affectedRow);
                    resultNode.put("error", "Selected row does not belong to the enemy.");
                    return resultNode;
                }
            }
        } else {
            if (currentTurn == 1) {
                if (affectedRow == SECOND_ROW_PLAYER2 || affectedRow == FIRST_ROW_PLAYER2) {
                    resultNode.put("command", "useHeroAbility");
                    resultNode.put("affectedRow", affectedRow);
                    resultNode.put("error", "Selected row does not belong to the current player.");
                    return resultNode;
                }
            } else {
                if (affectedRow == FIRST_ROW_PLAYER1 || affectedRow == SECOND_ROW_PLAYER1) {
                    resultNode.put("command", "useHeroAbility");
                    resultNode.put("affectedRow", affectedRow);
                    resultNode.put("error", "Selected row does not belong to the current player.");
                    return resultNode;
                }
            }
        }

        // determine the card with the maximum health from the selected row
        int maxHealth = 0;
        for (int i = 0; i < table.getTable().get(affectedRow).size(); i++) {
            Minion minion = table.getTable().get(affectedRow).get(i);
            if (minion.getHealth() > maxHealth) {
                maxHealth = minion.getHealth();
            }
        }

        // use the hero's ability on the card
        for (int i = 0; i < table.getTable().get(affectedRow).size(); i++) {
            Minion minion = table.getTable().get(affectedRow).get(i);
            // the Empress Thorina's ability can only be used on the card with the maximum health
            if (hero.getName().equals("Empress Thorina")) {
                if (minion.getHealth() == maxHealth) {
                    table.getTable().get(affectedRow).remove(minion);
                    break;
                }
            } else {
                hero.selectHeroAbility(hero, minion);
                if (minion.getHealth() <= 0) {
                    // remove the card from the table if it has died
                    table.getTable().get(affectedRow).remove(minion);
                }
            }
        }
        // decrease the player's mana
        currentPlayer.setMana(currentPlayer.getMana() - hero.getMana());

        if (currentTurn == 1) {
            heroOneUsedAbility = true;
        } else {
            heroTwoUsedAbility = true;
        }

        return resultNode;
    }

    /**
     * @return the frozen cards on the table
     */
    private ObjectNode getFrozenCardsOnTable()  {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getFrozenCardsOnTable");
        ArrayNode tablePrint = objectMapper.createArrayNode();

        for (ArrayList<Minion> row: table.getTable()) {
            for (Minion card : row) {
                if (card.getFrozen() == 1) {
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
                    tablePrint.add(cardNode);
                }
            }
        }

        newNode.set("output", tablePrint);
        return newNode;
    }

    /**
     * @return the total number of games played
     */
    private ObjectNode getTotalGamesPlayed() {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getTotalGamesPlayed");
        newNode.put("output", numberOfGamesPlayed);
        return newNode;
    }

    /**
     * @return the number of wins for player one
     */
    private ObjectNode getPlayerOneWins() {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getPlayerOneWins");
        newNode.put("output", playerOneWins);
        return newNode;
    }

    /**
     * @return the number of wins for player two
     */
    private ObjectNode getPlayerTwoWins() {
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.put("command", "getPlayerTwoWins");
        newNode.put("output", playerTwoWins);
        return newNode;
    }

    public ArrayNode getOutput() {
        return output;
    }

    public void setOutput(final ArrayNode output) {
        this.output = output;
    }

    public int getStartingTurn() {
        return startingTurn;
    }

    public void setStartingTurn(final int startingTurn) {
        this.startingTurn = startingTurn;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(final int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getGameMana() {
        return gameMana;
    }

    public void setGameMana(final int gameMana) {
        this.gameMana = gameMana;
    }

    public void setPlayerOneWins(final int playerOneWins) {
        this.playerOneWins = playerOneWins;
    }

    public void setPlayerTwoWins(final int playerTwoWins) {
        this.playerTwoWins = playerTwoWins;
    }

    public int getNumberOfGamesPlayed() {
        return numberOfGamesPlayed;
    }

    public void setNumberOfGamesPlayed(final int numberOfGamesPlayed) {
        this.numberOfGamesPlayed = numberOfGamesPlayed;
    }

    public boolean isHeroOneUsedAbility() {
        return heroOneUsedAbility;
    }

    public void setHeroOneUsedAbility(final boolean heroOneUsedAbility) {
        this.heroOneUsedAbility = heroOneUsedAbility;
    }

    public boolean isHeroTwoUsedAbility() {
        return heroTwoUsedAbility;
    }

    public void setHeroTwoUsedAbility(final boolean heroTwoUsedAbility) {
        this.heroTwoUsedAbility = heroTwoUsedAbility;
    }
}
