package main.game;

import main.Card;
import main.collection.Stack;

import java.util.Random;

public class Deck extends Stack<Card> {
    private static final Random RANDOM = new Random();

    public Deck(boolean isShuffled) {
        int[] sequentialArray = createSequentialArray();
        if(isShuffled) {
            for(int i = 0; i < sequentialArray.length; i++) {
                int randomIndex = RANDOM.nextInt(sequentialArray.length);
                int temp = sequentialArray[randomIndex];
                sequentialArray[randomIndex] = sequentialArray[i];
                sequentialArray[i] = temp;
            }
        }
        for(int cardValue = 0; cardValue < 52; cardValue++) {
            push(new Card(sequentialArray[cardValue]));
        }
    }

    private static int[] createSequentialArray() {
        int[] result = new int[52];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }
}
