package model;

import collection.Queue;
import lombok.val;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PlayerMoveHistory implements Serializable {
    public final ElevensDeck initialDeckState;
    public final Card[] initialInPlayState;
    private final Queue<PlayerMoveHistoryEntry> moves = new Queue<>();

    public ElevensDeck currentDeckState;
    public Card[] currentInPlayState;
    public int moveCount;

    public PlayerMoveHistory(ElevensDeck initialDeck, Card[] initialInPlay) {
        initialDeckState = SerializationUtils.clone(initialDeck);
        initialInPlayState = SerializationUtils.clone(initialInPlay);
    }

    public void addEntry(PlayerMoveHistoryEntry playerMoveHistoryEntry) {
        playerMoveHistoryEntry.setPlayOrder(moveCount++);
        moves.enqueue(playerMoveHistoryEntry);
    }

    public void replay() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n NOW REPLAYING \n\n\n\n\n\n\n\n\n\n\n");
        currentDeckState = initialDeckState;
        currentInPlayState = initialInPlayState;
        val playerMoves = getPlayerMoves();
        for (PlayerMoveHistoryEntry playerMove : playerMoves) {
            for (int j = 0; j < 9; j++) {
                System.out.printf("%s: %s\n", (char) ('A' + j), currentInPlayState[j]);
            }
            currentInPlayState = currentDeckState.removeCardsFromInPlay(currentInPlayState, playerMove.first, playerMove.second, playerMove.third);
            System.out.println("\nRemoved " + Arrays.stream(playerMove.third == null ?
                    new Card[]{playerMove.first, playerMove.second} :
                    new Card[]{playerMove.first, playerMove.second, playerMove.third})
                    .map(Card::toString)
                    .collect(Collectors.joining(" and ")) + "\n");
            new Scanner(System.in).nextLine();
            for (int i = 0; i < currentInPlayState.length; i++) {
                if (currentInPlayState[i] == null) {
                    currentInPlayState[i] = currentDeckState.drawCard();
                }
            }
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n END OF REPLAY \n\n\n\n\n\n\n\n\n\n\n");
    }

    public PlayerMoveHistoryEntry[] getPlayerMoves() {
        PlayerMoveHistoryEntry[] playerMoves = new PlayerMoveHistoryEntry[moveCount];
        for (int i = 0; i < moveCount; i++) {
            playerMoves[i] = moves.dequeue();
        }
        val newPlayerMoves = new PlayerMoveHistoryEntry[moveCount];
        Arrays.stream(playerMoves).sorted(PlayerMoveHistoryEntry::compareTo).forEach(log -> newPlayerMoves[(int) Arrays.stream(newPlayerMoves).filter(Objects::nonNull).count()] = log);
        return newPlayerMoves;
    }

    public static class PlayerMoveHistoryEntry implements Comparable<PlayerMoveHistoryEntry>, Serializable {
        public final Card first;
        public final Card second;
        public final Card third;
        private int playOrder;

        public PlayerMoveHistoryEntry(Card... cards) {
            this.first = cards[0];
            this.second = cards[1];
            this.third = cards.length == 3 ? cards[2] : null;
        }

        @Override
        public int compareTo(PlayerMoveHistoryEntry o) {
            return Integer.compare(playOrder, o.playOrder);
        }

        public void setPlayOrder(int playOrder) {
            this.playOrder = playOrder;
        }
    }

}
