import model.Card;
import model.ElevensDeck;
import model.Game;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GameTest {

    public Game game;

    public ElevensDeck deck;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deck = spy(new ElevensDeck(game));
        game = spy(new Game(deck));
    }

    @Test
    public void deckIsPopulatedWhenGameStarted() {
        game.initBoard();
        verify(deck, Mockito.times(9)).drawCard();
        assertThat(deck.getContext() != null);
    }

    @Test
    public void gameIdentifiesStalemate() {
        doReturn(new Card(1)).when(deck).drawCard();
        game.initBoard();
        assertThat(game.isStalemate());
    }

    @Test
    public void gameIdentifiesWinWhenBoardEmpty() {
        doReturn(null).when(deck).drawCard();
        game.initBoard();
        assertThat(game.isWon());
    }
}

