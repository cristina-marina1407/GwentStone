package org.poo.gameManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.cards.Minion;
import org.poo.main.Card;

import java.util.ArrayList;

import static org.poo.gameManagement.Constants.MAX_COLUMN;
import static org.poo.gameManagement.Constants.MAX_ROW;


public class Table {
    private ArrayList<ArrayList<Minion>> table = new ArrayList<>(MAX_COLUMN);
    private ArrayList<Minion> playerOneHand = new ArrayList<>();
    private ArrayList<Minion> playerTwoHand = new ArrayList<>();

    public Table() {
        for (int i = 0; i < MAX_COLUMN; i++) {
            ArrayList<Minion> row = new ArrayList<>(MAX_ROW);
            table.add(row);
        }
    }

    /**
     * Prints the player's hand
     * @param playerHand the player's hand
     * @param objectMapper the object mapper
     * @return the player's hand
     */
    public ArrayNode printHand(final ArrayList<Minion> playerHand,
                               final ObjectMapper objectMapper) {
        ArrayNode hand = objectMapper.createArrayNode();
        for (Minion card : playerHand) {
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("name", card.getName());
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());
            ArrayNode colorsArray = objectMapper.createArrayNode();
            for (String color : card.getColors()) {
                colorsArray.add(color);
            }
            cardNode.set("colors", colorsArray);
            hand.add(cardNode);
        }
        return hand;
    }

    /**
     * Prints the table
     * @param printTable the table
     * @param objectMapper the object mapper
     * @return the table
     */
    public ArrayNode printTable(final Table printTable, final ObjectMapper objectMapper) {
        ArrayNode tablePrint = objectMapper.createArrayNode();
        for (ArrayList<Minion> row: printTable.getTable()) {
            ArrayNode cardInRow = objectMapper.createArrayNode();
            for (Minion card : row) {
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
                cardInRow.add(cardNode);
            }
            tablePrint.add(cardInRow);
        }
        return tablePrint;
    }

    /**
     * Checks if there are tanks on a player's side of the table
     * @param checkTable the table
     * @param x1 the row index
     * @param x2 the row index
     * @return true if there are tanks on the player's side of the table, false otherwise
     */
    public boolean checkTanks(final Table checkTable, final int x1, final int x2) {
        boolean check = false;

        for (int i = x1; i <= x2; i++) {
            for (Card card : checkTable.getTable().get(i)) {
                if (card.isTank()) {
                    check = true;
                    break;
                }
            }
            if (check) {
                break;
            }
        }

        return check;
    }

    /**
     * Prints the result of an attack
     * @param resultNode the result node
     * @param objectMapper the object mapper
     * @param x1 the x coordinate of the attacker
     * @param y1 the y coordinate of the attacker
     * @param x2 the x coordinate of the attacked
     * @param y2 the y coordinate of the attacked
     * @return the result of an attack for the output
     */
    public ObjectNode attackCardOutput(final ObjectNode resultNode, final ObjectMapper objectMapper,
                                   final int x1, final int y1, final int x2, final int y2) {
        ObjectNode attackerNode = objectMapper.createObjectNode();
        attackerNode.put("x", x1);
        attackerNode.put("y", y1);
        resultNode.set("cardAttacker", attackerNode);

        ObjectNode attackedNode = objectMapper.createObjectNode();
        attackedNode.put("x", x2);
        attackedNode.put("y", y2);
        resultNode.set("cardAttacked", attackedNode);
        return resultNode;
    }

    /**
     * Prints the result of an attack on the hero
     * @param resultNode the result node
     * @param objectMapper the object mapper
     * @param x the x coordinate of the attacker
     * @param y the y coordinate of the attacker
     * @return the result of an attack on the hero for the output
     */
    public ObjectNode attackHeroOutput(final ObjectNode resultNode, final ObjectMapper objectMapper,
                                       final int x, final int y) {
        ObjectNode attackerNode = objectMapper.createObjectNode();
        attackerNode.put("x", x);
        attackerNode.put("y", y);
        resultNode.set("cardAttacker", attackerNode);
        return resultNode;
    }

    /**
     * @return the table
     */
    public ArrayList<ArrayList<Minion>> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(final ArrayList<ArrayList<Minion>> table) {
        this.table = table;
    }

    /**
     * @param playerIdx the player index
     * @return the player's hand
     */
    public ArrayList<Minion> getHand(final int playerIdx) {
        if (playerIdx == 1) {
            return playerOneHand;
        } else {
            return playerTwoHand;
        }
    }

    /**
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return table.size();
    }

    /**
     * @param playerOneHand the player's hand to set
     */
    public void setPlayerOneHand(final ArrayList<Minion> playerOneHand) {
        this.playerOneHand = playerOneHand;
    }

    /**
     *
     * @param playerTwoHand the player's hand to set
     */
    public void setPlayerTwoHand(final ArrayList<Minion> playerTwoHand) {
        this.playerTwoHand = playerTwoHand;
    }
}
