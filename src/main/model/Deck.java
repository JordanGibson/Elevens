package main.model;

import main.collection.Stack;

import java.util.Random;

public class Deck extends Stack<Card> {
    private static final Random RANDOM = new Random();

    public Deck() {
        int[] sequentialArray = createSequentialArray();
        for (int i = 0; i < sequentialArray.length; i++) {
            int randomIndex = RANDOM.nextInt(sequentialArray.length);
            int temp = sequentialArray[randomIndex];
            sequentialArray[randomIndex] = sequentialArray[i];
            sequentialArray[i] = temp;
        }
        for (int cardValue = 0; cardValue < 52; cardValue++) {
            push(new Card(sequentialArray[cardValue]));
        }
    }

    public Card drawCard() {
        if (isEmpty()) {
            return null;
        }
        return pop();
    }

    private static int[] createSequentialArray() {
        int[] result = new int[52];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }
}
