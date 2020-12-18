import lombok.val;
import model.Card;
import model.Deck;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DeckTest {
    @Test
    public void deckShouldBe52Cards() {
        val deck = new Deck();
        assertThat(deck.getRemainingCardCount(), is(52));
    }

    @Test
    public void deckSizeReducesBy1WhenCardDrawn() {
        val deck = new Deck();
        assertThat(deck.getRemainingCardCount(), is(52));
        deck.drawCard();
        assertThat(deck.getRemainingCardCount(), is(51));
    }

    @Test
    public void deckDoesNotContainDuplicates() {
        val deck = new Deck();
        val cards = new Card[52];
        for (int i = 0; !deck.isEmpty(); i++) {
            cards[i] = deck.pop();
        }
        Assertions.assertThat(cards).doesNotHaveDuplicates();
    }

    @Test
    public void deckIsShuffled() {
        val deck = new Deck();
        val cards = new Card[52];
        for (int i = 0; !deck.isEmpty(); i++) {
            cards[i] = deck.pop();
        }
        val sortedDeck = Arrays.stream(cards).sorted(Comparator.comparing(Card::getValue).reversed()).toArray();
        assertThat(cards, IsNot.not(sortedDeck));
    }
}
