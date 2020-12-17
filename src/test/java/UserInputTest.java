import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import model.Game;
import model.UserInput;
import org.junit.Test;
import org.junit.runner.RunWith;

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


    @Test
    @UseDataProvider("badInput")
    public void badInputShouldReturnErr(String input, UserInput.UserInputStatus expected) {
        Game.initDeck();
        UserInput userInput = new UserInput(input);
        assertThat(userInput.status, is(expected));
    }
}
