package model;

import collection.Stack;

import java.util.Random;

public class Deck extends Stack<Card> {
    private static final Random RANDOM = new Random();
    private int currentSize = 52;

    public Deck() {
        int[] sequentialArray = createSequentialArray();
        for (int i = 0; i < sequentialArray.length; i++) {
            int randomIndex = RANDOM.nextInt(sequentialArray.length);
            int temp = sequentialArray[randomIndex];
            sequentialArray[randomIndex] = sequentialArray[i];
            sequentialArray[i] = temp;
        }
        for (int j : sequentialArray) {
            push(new Card(j));
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
        int[] result = new int[52];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }
}