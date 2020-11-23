import model.Card;
import model.Deck;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class DeckTest {
    @Test
    public void DeckShouldBe52Cards() {
        Deck deck = new Deck();
        assertThat(deck.getRemainingCardCount(), is(52));
    }

    @Test
    public void DeckSizeReducesBy1WhenCardDrawn() {
        Deck deck = new Deck();
        assertThat(deck.getRemainingCardCount(), is(52));
        deck.drawCard();
        assertThat(deck.getRemainingCardCount(), is(51));
    }

    @Test
    public void DeckDoesNotContainDuplicates() {
        Deck deck = new Deck();
        Card[] cards = new Card[52];
        for (int i = 0; !deck.isEmpty(); i++) {
            cards[i] = deck.pop();
        }
        Assertions.assertThat(cards).doesNotHaveDuplicates();
    }

    @Test
    public void DeckIsShuffled() {
        Deck deck = new Deck();
        Card[] cards = new Card[52];
        for (int i = 0; !deck.isEmpty(); i++) {
            cards[i] = deck.pop();
        }
        var sortedDeck = Arrays.stream(cards).sorted(Comparator.comparing(Card::getValue).reversed()).toArray();
        assertThat(cards, IsNot.not(sortedDeck));
    }
}
