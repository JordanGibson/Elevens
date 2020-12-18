package collection;

import lombok.val;
import model.Card;
import model.ElevensDeck;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
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
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n NOW REPLAYING \n\n\n\n\n\n\n\n\n");
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
            for (int i = 0; i < currentInPlayState.length; i++) {
                if (currentInPlayState[i] == null) {
                    currentInPlayState[i] = currentDeckState.drawCard();
                }
            }
        }
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
}
