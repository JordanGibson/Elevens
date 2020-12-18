package model;

import collection.Stack;
import lombok.val;

import java.util.Random;

public class Deck extends Stack<Card> {
    private static final Random RANDOM = new Random();
    private int currentSize = 52;

    public Deck() {
        val sequentialArray = createSequentialArray();
        for (int i = 0; i < sequentialArray.length; i++) {
            val randomIndex = RANDOM.nextInt(sequentialArray.length);
            val temp = sequentialArray[randomIndex];
            sequentialArray[randomIndex] = sequentialArray[i];
            sequentialArray[i] = temp;
        }
        for (int j : sequentialArray) {
            push(new Card(j));
        }
    }

    public Deck(int... cards) {
        currentSize = cards.length;
        for (var card : cards) {
            push(new Card(card));
        }
    }

    public Card drawCard() {
        if (isEmpty()) {
            return null;
        }
        currentSize--;
        return pop();
    }

    public int getRemainingCardCount() {
        return currentSize;
    }

    private static int[] createSequentialArray() {
        val result = new int[52];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }
}