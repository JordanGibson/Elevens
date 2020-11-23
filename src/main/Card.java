package main;

import java.util.Random;

public class Card implements Comparable<Card> {
    private final int RANK, SUIT;
    private static final Random Generator = new Random();
    private static final String[] RANKS = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private static final String[] SUITS = {"Clubs", "Diamonds", "Hearts", "Spades"};

    public Card() {
        RANK = Generator.nextInt(RANKS.length);
        SUIT = Generator.nextInt(SUITS.length);
    }

    public String getRank() {
        return RANKS[RANK];
    }

    public String getSuit() {
        return SUITS[SUIT];
    }

    public int getRankValue() {
        return RANK;
    }

    public String getColour() {
        return getSuit().equals("Diamonds") || getSuit().equals("Hearts") ? "Red" : "Black";
    }

    @Override
    public String toString() {
        return getRank() + " of " + getSuit();
    }

    @Override
    public int compareTo(Card otherCard) {
        return Integer.compare(this.getRankValue(), otherCard.getRankValue());
    }
}