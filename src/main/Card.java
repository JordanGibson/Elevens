package main;

import java.util.Random;

public class Card implements Comparable<Card> {
    private final int rank, suit;
    private static final Random GENERATOR = new Random();
    private static final String[] RANKS = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private static final String[] SUITS = {"Clubs", "Hearts", "Spades", "Diamonds"};

    public Card() {
        rank = GENERATOR.nextInt(RANKS.length);
        suit = GENERATOR.nextInt(SUITS.length);
    }

    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return RANKS[rank];
    }

    public String getSuit() {
        return SUITS[suit];
    }

    public int getRankValue() {
        return rank;
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