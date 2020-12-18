import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import model.Deck;
import model.Game;
import model.UserInput;
import net.bytebuddy.implementation.bytecode.Addition;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import static model.UserInput.UserInputStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;


@SuppressWarnings("ALL")
@RunWith(DataProviderRunner.class)
public class UserInputTest {

    private static final String VALID_2_CARD = "ab";
    private static final String VALID_3_CARD = "abc";

    @Mock
    private Game context;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doReturn(true).when(context).isSelectionValid(not(eq('V')));
    }

    @DataProvider
    public static Object[][] inputs() {
        return new Object[][]{
                {"", Empty},
                {"x", Hint},
                {"test", InvalidNumberOfCards},
                {"aa", SameCard},
                {"va", InvalidFirstCard},
                {"vab", InvalidFirstCard},
                {"av", InvalidSecondCard},
                {"abv", InvalidThirdCard},
                {VALID_2_CARD, Valid2Card},
                {VALID_3_CARD, Valid3Card}
        };
    }


    @Test
    @UseDataProvider("inputs")
    public void testUserInputStatusResults(String input, UserInput.UserInputStatus expected) {
        UserInput userInput = new UserInput(input, context);
        assertThat(userInput.status, is(expected));
    }

    @Test
    public void shouldSetFirstAndSecondCardButNotThirdForValid2Card() {
        UserInput userInput = new UserInput(VALID_2_CARD, context);
        assertThat(userInput.first, is('A'));
        assertThat(userInput.second, is('B'));
        assertThat(CharUtils.isAsciiPrintable(userInput.third), is(false));
    }

    @Test
    public void shouldSetFirstSecondAndThirdCardForValid3Card() {
        UserInput userInput = new UserInput(VALID_3_CARD, context);
        assertThat(userInput.first, is('A'));
        assertThat(userInput.second, is('B'));
        assertThat(userInput.third, is('C'));
    }
}
