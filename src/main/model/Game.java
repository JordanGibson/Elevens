package model;

import collection.PlayerMoveHistory;
import collection.PlayerMoveHistoryEntry;
import lombok.val;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static model.UserInput.UserInputStatus.Hint;

public class Game implements Serializable {
    public static final int ASCII_OFFSET = 65;

    private final Card[] inPlay = new Card[9];
    private PlayerMoveHistory playerMoveHistory;
    private final ElevensDeck deck;

    public Game() {
        deck = new ElevensDeck(this);
    }

    public void start() {
        initBoard();
        playerMoveHistory = new PlayerMoveHistory(deck, inPlay);
        displayBoard();
        while (!isWon() && !isStalemate()) {
            makeTurn();
            refillInPlay();
            displayBoard();
        }
        playerMoveHistory.replay();
    }

    public void initBoard() {
        for (int i = 0; i < 9; i++) {
            inPlay[i] = deck.drawCard();
        }
    }

    public IntStream getUniqueInPlayCardValues(boolean isFaceCards) {
        return Arrays.stream(inPlay)
                .filter(Objects::nonNull)
                .filter(isFaceCards ? Card::isFaceCard : Predicate.not(Card::isFaceCard))
                .mapToInt(Card::getRankValue)
                .distinct();
    }

    public void refillInPlay() {
        // Refill the deck
        for (int i = 0; i < inPlay.length; i++) {
            if (inPlay[i] == null) {
                inPlay[i] = deck.drawCard();
            }
        }
    }

    public void displayBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%s: %s\n", (char) ('A' + i), inPlay[i]);
        }
    }

    public void makeTurn() {
        System.out.print("Enter Letters: ");
        val rawUserInput = new Scanner(System.in).nextLine();
        val userInput = new UserInput(rawUserInput, this);
        if (userInput.status == Hint) {
            displayHint();
            return;
        } else if (userInput.isInvalid()) {
            System.out.println(userInput.status.getMessage());
            return;
        }
        val cardsToRemove = deck.getCardsToRemove(userInput);
        if (cardsToRemove.length > 0) {
            playerMoveHistory.addEntry(new PlayerMoveHistoryEntry(cardsToRemove));
            Arrays.stream(cardsToRemove).forEach(this::removeCardFromBoard);
            System.out.println("Removed " + Arrays.stream(cardsToRemove).map(Card::toString).collect(Collectors.joining(" and ")));
        }
    }

    public void displayHint() {
        System.out.println("You requested a hint!\nPsst, your hint is " + deck.getHint());
    }

    public int getAsciiCharacterOfInPlay(int value) {
        return IntStream.range(0, inPlay.length)
                .filter(x -> inPlay[x].getRankValue() == value)
                .findFirst()
                .orElse(-999) + ASCII_OFFSET;
    }

    public boolean isStalemate() {
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            val firstCard = getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            if (getUniqueInPlayCardValues(false)
                    .anyMatch(value -> {
                        if (value < 0) return false;
                        return (firstCard.getRankValue() + value) == 9;
                    })) {
                return false;
            }
        }
        return getUniqueInPlayCardValues(true).sum() != 33;
    }

    public boolean isWon() {
        return Arrays.stream(inPlay).allMatch(Objects::isNull);
    }

    public Card getCardFromBoard(char selection) {
        try {
            return inPlay[selection - ASCII_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void removeCardFromBoard(Card card) {
        Arrays.stream(IntStream.range(0, inPlay.length)
                .toArray())
                .filter(index -> {
                    try {
                        return inPlay[index].equals(card);
                    } catch (NullPointerException e) {
                        return false;
                    }
                })
                .findFirst()
                .ifPresent(index -> inPlay[index] = null);
    }

    // In here, we should be validating if the selected character is in the game board, and if there is a card at this location on the board
    public boolean isSelectionValid(char selection) {
        return !(selection < 'A' || selection > 'I' || inPlay[selection - ASCII_OFFSET] == null);
    }
}
