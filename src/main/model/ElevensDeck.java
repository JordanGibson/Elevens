package model;

import lombok.val;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

import static model.Game.ASCII_OFFSET;
import static model.UserInput.UserInputStatus.Empty;
import static model.UserInput.UserInputStatus.Valid2Card;

public class ElevensDeck extends Deck implements Serializable {

    private final Game context;

    public ElevensDeck(Game context) {
        super();
        this.context = context;
    }

    public Boolean isValidPlayerMove(char first, char second, char third) {
        val firstCard = context.getCardFromBoard(first);
        val secondCard = context.getCardFromBoard(second);
        val thirdCard = context.getCardFromBoard(third);
        // If a third card is present, then we assume we are removing a JQK triple
        if (thirdCard != null) {
            // JQK triple
            return isValidJQKSelection(firstCard, secondCard, thirdCard);
        } else {
            // Two cards add to give 11
            return isValid2CardSelection(firstCard, secondCard);
        }
    }

    private boolean isValid2CardSelection(Card first, Card second) {
        val sum = first.getRankValue() + second.getRankValue();
        return !first.isFaceCard() && !second.isFaceCard() && sum == 9;
    }

    private boolean isValidJQKSelection(Card first, Card second, Card third) {
        val sum = first.getRankValue() + second.getRankValue() + third.getRankValue();
        return first.getRankValue() != second.getRankValue() &&
                first.getRankValue() != third.getRankValue() &&
                second.getRankValue() != third.getRankValue() &&
                sum == 33;
    }

    public Card[] removeCardsFromInPlay(Card[] inPlay, Card... cards) {
        Arrays.stream(cards).filter(Objects::nonNull).forEach(card -> removeCardFromBoard(inPlay, card));
        return inPlay;
    }

    public Card[] getCardsToRemove(UserInput userInput) {
        // After this point, we can assume that the user input is valid, as all validation is done within UserInputValidator
        Card[] cardsToRemove = new Card[0];
        if (isValidPlayerMove(userInput.first, userInput.second, userInput.third) &&
                isSelectionValid(userInput.first) && isSelectionValid(userInput.second)) {
            if (userInput.status == Valid2Card) {
                cardsToRemove = new Card[]{
                        getCardFromBoard(userInput.first),
                        getCardFromBoard(userInput.second)
                };
            } else {
                if (isSelectionValid(userInput.third)) {
                    cardsToRemove = new Card[]{
                            getCardFromBoard(userInput.first),
                            getCardFromBoard(userInput.second),
                            getCardFromBoard(userInput.third)
                    };
                }
            }
        } else {
            System.out.println((userInput.status == Valid2Card ?
                    "The two cards you have chosen do not add up to 11, try again!" :
                    "The three cards you have chosen are not a Jack, Queen or King, try again!") +
                    "\nPsst, if you're really stuck, you can type 'x' for a hint!");
        }
        return cardsToRemove;
    }

    private boolean isSelectionValid(char selection) {
        return context.isSelectionValid(selection);
    }

    private Card getCardFromBoard(char selection) {
        return context.getCardFromBoard(selection);
    }

    public String getHint() {
        if (context.isStalemate()) return "";
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            Card firstCard = context.getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            var otherCardValue = context.getUniqueInPlayCardValues(false)
                    .filter(value -> (firstCard.getRankValue() + value) == 9)
                    .findFirst().orElse(-1);
            if (otherCardValue != -1)
                return String.valueOf(Character.toUpperCase(first)) + (char) (context.getAsciiCharacterOfInPlay(otherCardValue));
        }
        return Character.toString(context.getAsciiCharacterOfInPlay(10))
                + Character.toString(context.getAsciiCharacterOfInPlay(11))
                + Character.toString(context.getAsciiCharacterOfInPlay(12));
    }

    public boolean makeValidMove() {
        val userInput = new UserInput(getHint(), context);
        if (userInput.status.equals(Empty)) return false;
        getCardsToRemove(userInput);
        return true;
    }

    protected void removeCardFromBoard(Card[] inPlay, Card card) {
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
}
