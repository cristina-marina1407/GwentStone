
# Tema 0 - GwentStone Lite

## Description
- This project is a simplified version of the popular card games Gwent and
Hearthstone. The game is played between two players, each with a deck of cards
containing minions and heroes.
- The game operates through game sessions, each with distinct properties and
actions. An action is actually a command that can be executed by the player.
These commands can be moves such as placing a card on the table, attacking
one of the opponent's minions or the hero, using a special ability or they can
be used for debugging purposes by printing different aspects of the game.
- The hero is the most important card, because if the hero dies, the game
ends.

## Implementation
The classes used in this project are:

*Game Class*:

- Manages the game flow, including generating the sessions, handling
the player turns and command execution. This class contains the main functions
of the game, such as place card, attack, use ability, etc.

*Player Class*:

- Represents a player in the game. This class is responsible for managing the
player's decks and hero. It also contains functions that help with the output
for the debugging commands.

*Card Class*:

- Represents a card in the game. This class is the base class for all the cards,
minions and heroes.

*Minion Class*:
- Represents a minion card. This class extends the Card class and it is used in
the Table class for the hand and the table. It also implements every ability
that a minion can have.

*Hero Class*:
- Represents a hero card with special abilities. This class extends the Card class
and implements every ability that a hero can have.

*Table Class*:
- This class contains the table and the hands of the players. The table is
represented as a list of lists, where each list represents a row of the table.
The hands are represented as lists of cards. This class also contains functions
that help with the output for the debugging commands.

## Commands
- The commands are handled by a switch that calls the appropriate function.
The Player and the Table class interact so every player can have a deck, a hand
and cards on the table. The table is implemented in a different class, because
the table should be accessible from every function, regardless of the player.

- The debugging commands are: getPlayerDeck, that shows the player's deck in
every point of the game, so we can see the cards that remain in the deck and
their stage; getCardsInHand, that shows the player's hand, which is updated
every turn, getPlayerHero, that shows the player's hero; getCardsOnTable, that
shows the cards on the table, so we can see the minions that are still alive
and can attack and use abilities, getPlayerTurn, getCardAtPosition and other
commands that show the status of the game.

- The other commands are actions that can be executed by the player, such as
placeCard, cardUsesAttack, cardUsesAbility, useAttackHero and useHeroAbility.
Every card has mana so using any of these commands will decrease the player's
mana.

- The player can attack the other minions of its enemy only after he attacked its
tanks, using a function that checks if a player has tanks on its rows,
implemented in the Table class. This condition is being checked every time a
card has to attack or use an ability and this rule also applies to the hero.

- Some cards and heroes have abilities that can be used only on the player's
minions and others have abilities that can be used only on the enemy's minions.

## Feedback
- The project was challenging and it required attention to detail, because
the game has many rules and interactions. The most challenging part was to
figure out how to implement the components of the game, the game preparation
was actually more difficult than the game itself. I also had a lot of errors
and it took a lot of time to debug them, but I learned a lot from this project.
I think this homework was a good way to practice OOP concepts and to learn how
to use them by making a lot of mistakes and correcting them.
