import model.Card;
import model.Deck;
import model.UserInput;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static model.Game.*;
import static model.Game.displayBoard;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GameTest {
    @Test
    public void BoilerplateTest() {
        assertThat(1, is(1));
    }

    public void correctInputDoenstBreak(String input, boolean expected) {
        Deck deck = new Deck(1,2,3,4,5,6,7,10,11,12);
        for (int i = 0; i < 9; i++) {
            inPlay[i] = deck.drawCard();
        }
        displayBoard();
        System.out.println("initial deck ^^\n");
        UserInput userInput = new UserInput(input);
        assertThat(applyUserInput(userInput), is(expected));
        displayBoard();
        System.out.println("after change deck ^^");
    }
}
