import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import model.Deck;
import model.Game;
import model.UserInput;
import org.junit.Test;
import org.junit.runner.RunWith;

import static model.Game.*;
import static model.UserInput.UserInputStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ALL")
@RunWith(DataProviderRunner.class)
public class UserInputTest {

    @DataProvider
    public static Object[][] badInput() {
        return new Object[][]{
                {"test", InvalidNumberOfCards},
                {"abc", Valid3Card},
                {"ab", Valid2Card},
                {"aa", SameCard},
                {"av", InvalidSecondCard},
                {"va", InvalidFirstCard},
                {"abv", InvalidThirdCard},
                {"vab", InvalidFirstCard},
                {"x", Hint},
        };
    }

    @DataProvider
    public static Object[][] goodInput() {
        return new Object[][]{
                {"di", true},//correct 2 card input
                {"abc", true},//correct 3 card input
                {"eh", true},//correct 2 card input
                {"fg", true},//correct 2 card input
                {"eg", false},//incorrect 2 card input
                {"ah", false},//incorrect 2 card input
                {"ab", false},//incorrect 3 card input
        };
    }


    @Test
    @UseDataProvider("badInput")
    public void badInputShouldReturnErr(String input, UserInput.UserInputStatus expected) {
        Game.initDeck();
        UserInput userInput = new UserInput(input);
        assertThat(userInput.status, is(expected));
    }

    @Test
    @UseDataProvider("goodInput")
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
