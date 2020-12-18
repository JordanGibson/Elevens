package model;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {
    private final int rank, suit;
    private static final String[] RANKS = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    private static final String[] SUITS = {"Clubs", "Hearts", "Spades", "Diamonds"};
    private final int value;


    public Card(int value) {
        this.rank = value % 13;
        this.suit = value / 13;
        this.value = value;
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

    public boolean isFaceCard() {
        return rank >= 10;
    }

    @Override
    public String toString() {
        return getRank() + " of " + getSuit();
    }

    @Override
    public int compareTo(Card otherCard) {
        return Integer.compare(this.getRankValue(), otherCard.getRankValue());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Card && ((Card) obj).getValue() == getValue();
    }

    public int getValue() {
        return value;
    }
}