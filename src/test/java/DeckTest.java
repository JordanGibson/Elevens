import lombok.val;
import model.Card;
import model.ElevensDeck;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {
    @Test
    public void deckShouldBe52Cards() {
        val deck = new ElevensDeck(null);
        assertThat(deck.getRemainingCardCount() == 52);
    }

    @Test
    public void deckSizeReducesBy1WhenCardDrawn() {
        val deck = new ElevensDeck(null);
        assertThat(deck.getRemainingCardCount() == 52);
        deck.drawCard();
        assertThat(deck.getRemainingCardCount() == 51);
    }

    @Test
    public void deckDoesNotContainDuplicates() {
        val deck = new ElevensDeck(null);
        val cards = new Card[52];
        for (int i = 0; !deck.isEmpty(); i++) {
            cards[i] = deck.pop();
        }
        assertThat(cards).doesNotHaveDuplicates();
    }

    @Test
    public void deckIsShuffled() {
        val deck = new ElevensDeck(null);
        val cards = new Card[52];
        for (int i = 0; !deck.isEmpty(); i++) {
            cards[i] = deck.pop();
        }
        val sortedDeck = Arrays.stream(cards).sorted(Comparator.comparing(Card::getValue).reversed()).toArray();
        assertThat(cards != sortedDeck);
    }
}
