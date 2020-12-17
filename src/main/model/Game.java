package model;

import lombok.val;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static model.UserInput.UserInputStatus.*;

public class Game {
    private static final Card[] inPlay = new Card[9]; //placeholder for 3x3 grid
    private static Deck deck;
    private static final Card[] discard = new Card[52];
    private static final int ASCII_OFFSET = 65;

    public static void start() {
        deck = freshShuffledDeck();
        for (int i = 0; i < 9; i++) {
            inPlay[i] = deck.drawCard();
        }
        displayBoard();
        while (!isWon() && !isStalemate()) {
            makeTurn();
            configureInPlay();
            displayBoard();
        }
    }

    private static boolean isStalemate() {
        // Better approach
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            val firstCard = getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            if (getUniqueInPlayCardValues(false)
                    .anyMatch(value -> (firstCard.getRankValue() + value) == 9)) {
                return false;
            }
        }
        return getUniqueInPlayCardValues(true).sum() != 33;
    }

    private static IntStream getUniqueInPlayCardValues(boolean isFaceCards) {
        return Arrays.stream(inPlay)
                .filter(isFaceCards ? Card::isFaceCard : Predicate.not(Card::isFaceCard))
                .mapToInt(Card::getRankValue)
                .distinct();
    }

    private static void configureInPlay() {
        // Refill the deck
        for (int i = 0; i < inPlay.length; i++) {
            if (inPlay[i] == null) {
                inPlay[i] = deck.drawCard();
            }
        }
    }

    public static void displayBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%s: %s\n", (char) ('A' + i), inPlay[i]);
        }
    }

    public static Boolean isValidPlayerMove(char first, char second, char third) {
        val firstCard = getCardFromBoard(first);
        val secondCard = getCardFromBoard(second);
        val thirdCard = getCardFromBoard(third);
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
        val sum = first.getRankValue() + second.getRankValue();
        return !first.isFaceCard() && !second.isFaceCard() && sum == 9;
    }

    private static boolean isValidJQKSelection(Card first, Card second, Card third) {
        val sum = first.getRankValue() + second.getRankValue() + third.getRankValue();
        return first.getRankValue() != second.getRankValue() &&
                first.getRankValue() != third.getRankValue() &&
                second.getRankValue() != third.getRankValue() &&
                sum == 33;
    }

    // Returns if the move was valid or not
    public static boolean makeTurn() {
        val rawUserInput = new Scanner(System.in).nextLine(); // TODO: Get user input, to be fixed in #8
        val userInput = new UserInput(rawUserInput);
        if (userInput.status == Hint) {
            displayHint();
        }
        if (userInput.status != Valid2Card && userInput.status != Valid3Card) {
            System.out.println(userInput.status.getMessage());
            return false;
        }
        applyUserInput(userInput);
        return true;
    }

    public static boolean applyUserInput(UserInput userInput) {
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
                    return false;
                }
            }
        } else {
            System.out.println("Invalid selection for cards");
            return false;
        }
        return true;
    }

    public static String getHint() {
        if (isStalemate()) return "";
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            Card firstCard = getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            var otherCardValue = getUniqueInPlayCardValues(false)
                    .filter(value -> (firstCard.getRankValue() + value) == 9)
                    .findFirst().orElse(-1);
            if (otherCardValue != -1)
                return String.valueOf(Character.toUpperCase(first)) + (char) (getAsciiCharacterOfInPlay(otherCardValue));
        }
        return Character.toString(getAsciiCharacterOfInPlay(10))
                + Character.toString(getAsciiCharacterOfInPlay(11))
                + Character.toString(getAsciiCharacterOfInPlay(12));
    }

    public static boolean makeValidMove() {
        val userInput = new UserInput(getHint());
        if (userInput.status.equals(Empty)) return false;
        applyUserInput(userInput);
        return true;
    }

    public static void displayHint() {
        System.out.println("You requested a hint!\nPsst, your hint is " + getHint());
    }


    public static int getAsciiCharacterOfInPlay(int value) {
        return IntStream.range(0, inPlay.length)
                .filter(x -> inPlay[x].getRankValue() == value)
                .findFirst().orElse(-999) + ASCII_OFFSET;
    }

    public static boolean isWon() {
        return Arrays.stream(inPlay).allMatch(Objects::isNull);
    }

    public static Card getCardFromBoard(char selection) {
        try {
            return inPlay[selection - ASCII_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void removeCardFromBoard(char selection) {
        discard[(int) Arrays.stream(discard).filter(Objects::nonNull).count()] = inPlay[selection - ASCII_OFFSET];
        inPlay[selection - ASCII_OFFSET] = null;
    }

    public static Deck freshShuffledDeck() {
        return new Deck();
    }

    // In here, we should be validating if the selected character is in the game board, and if there is a card at this location on the board
    public static boolean isSelectionValid(char selection) {
        return !(selection < 'A' || selection > 'I' || inPlay[selection - ASCII_OFFSET] == null);
    }
}