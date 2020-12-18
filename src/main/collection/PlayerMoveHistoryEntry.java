package collection;

import model.Card;

import java.io.Serializable;

/**
 * A PlayerMoveHistoryEntry will only be added to the history when a valid move is made and cards are removed
 */
public class PlayerMoveHistoryEntry implements Comparable<PlayerMoveHistoryEntry>, Serializable {
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
