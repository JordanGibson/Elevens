package main;

import main.model.Card;
import main.model.Deck;
import main.model.UserInput;

import java.util.Scanner;

import static main.model.UserInput.UserInputStatus.Valid2Card;
import static main.model.UserInput.UserInputStatus.Valid3Card;

public class Game {
    public static Deck DECK = new Deck();
    public static Card[] inPlay = new Card[9]; //placeholder for 3x3 grid
    private static final int ASCII_OFFSET = 65;

    public static void start() {
        for (int i = 0; i < 9; i++) {
            inPlay[i] = DECK.drawCard();
        }
        while (!isWon()) {
            displayBoard();
            makeTurn();
            configureInPlay();
        }
    }

    private static void configureInPlay() {
        // Refill the deck
        for (int i = 0; i < inPlay.length; i++) {
            if (inPlay[i] == null) {
                inPlay[i] = DECK.drawCard();
            }
        }
        // If there are empty spaces, push them all to the bottom
        reformatInPlay();
    }

    private static void reformatInPlay() {
        Card[] newInPlay = new Card[9];
        int blanksToAdd = 0;
        for (int i = 0; i < newInPlay.length; i++) {
            if (inPlay[i] == null) {
                blanksToAdd++;
                continue;
            }
            newInPlay[i] = inPlay[i];
        }
        for (int i = 0; i < blanksToAdd; i++) {
            newInPlay[8 - i] = null;
        }
        inPlay = newInPlay;
    }

    public static void displayBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%s: %s\n", (char) ('A' + i), inPlay[i]);
        }
    }

    public static Boolean isValidPlayerMove(char first, char second, char third) {
        Card firstCard = getCardFromBoard(first);
        Card secondCard = getCardFromBoard(second);
        Card thirdCard = getCardFromBoard(third);
        // If a third card is present, then we assume we are removing a JQK triple
        if (thirdCard != null) {
            // JQK triple
            return isValidJQKSelection(firstCard, secondCard, thirdCard);
        } else {
            // Two cards add to give 11
            return isValid2CardSelection(firstCard, secondCard);
        }
    }

    private static boolean isValid2CardSelection(Card first, Card second) {
        int sum = first.getRankValue() + second.getRankValue();
        return !first.isFaceCard() && !second.isFaceCard() && sum == 9;
    }

    private static boolean isValidJQKSelection(Card first, Card second, Card third) {
        int sum = first.getRankValue() + second.getRankValue() + third.getRankValue();
        return first.getRankValue() != second.getRankValue() &&
                first.getRankValue() != third.getRankValue() &&
                second.getRankValue() != third.getRankValue() &&
                sum == 33;
    }

    // Returns if the move was valid or not
    public static boolean makeTurn() {
        var rawUserInput = new Scanner(System.in).nextLine(); // TODO: Get user input, to be fixed in #8
        UserInput userInput = new UserInput(rawUserInput);
        if (userInput.status != Valid2Card && userInput.status != Valid3Card) {
            System.out.println(userInput.status.getMessage());
            return false;
        }
        // After this point, we can assume that the user input is valid, as all validation is done within UserInputValidator
        if (isValidPlayerMove(userInput.first, userInput.second, userInput.third) &&
                isSelectionValid(userInput.first) && isSelectionValid(userInput.second)) {
            if (userInput.status == Valid2Card) {
                removeCardFromBoard(userInput.first);
                removeCardFromBoard(userInput.second);
            } else {
                if (isSelectionValid(userInput.third)) {
                    removeCardFromBoard(userInput.first);
                    removeCardFromBoard(userInput.second);
                    removeCardFromBoard(userInput.third);
                } else {
                    System.out.println("Invalid selection for 3rd card");
                }
            }
        } else {
            // TODO: Improve this error handling logic
            System.out.println("Invalid selection for cards");
        }
        return true;
    }

    public static boolean isWon() {
        return inPlay.length == 0;
    }

    public static Card getCardFromBoard(char selection) {
        try {
            return inPlay[selection - ASCII_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void removeCardFromBoard(char selection) {
        inPlay[selection - ASCII_OFFSET] = null;
    }

    // In here, we should be validating if the selected character is in the game board, and if there is a card at this location on the board
    public static boolean isSelectionValid(char selection) {
        return !(selection < 'A' || selection > 'I' || inPlay[selection - ASCII_OFFSET] == null);
    }
}
