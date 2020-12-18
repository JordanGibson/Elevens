import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import lombok.val;
import model.Game;
import model.UserInput;
import org.apache.commons.lang3.CharUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static model.UserInput.UserInputStatus.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
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

    @DataProvider
    public static Object[][] inputSanitising() {
        return new Object[][]{
                {"abc", "ABC"},
                {"a,b,c", "ABC"},
                {"a    b   c", "ABC"},
                {"a,,,,,,,,b,,,c", "ABC"},
                {"a     ,b      , c,,,", "ABC"},
                {",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,ab,,,,,,,,,c                ", "ABC"},
                {"ab", "AB"},
                {",,,,,,,a   b ,", "AB"},
                {",a,b,,,,,   , , , ,,,, , ,", "AB"},
                {"a", "A"},
                {"              a               ", "A"},
                {",,,,,,,,,,,,,a,,,,,,,,,,,,,,,", "A"},
                {" , , , ,a, , , , , ", "A"},
                {"aBc", "ABC"},
                {"ABC", "ABC"},
                {"AbC", "ABC"},
                {"A   b, C", "ABC"},
                {",,,,a,,, B     ,", "AB"},
                {"     , ,,A ,,,,,   , ,b,,,,      ", "AB"}
        };
    }


    @Test
    @UseDataProvider("inputs")
    public void testUserInputStatusResults(String input, UserInput.UserInputStatus expected) {
        UserInput userInput = new UserInput(input, context);
        assertTrue(userInput.status.equals(expected));
    }

    @Test
    public void shouldSetFirstAndSecondCardButNotThirdForValid2Card() {
        UserInput userInput = new UserInput(VALID_2_CARD, context);
        assertTrue(userInput.first == 'A');
        assertTrue(userInput.second == 'B');
        assertTrue(CharUtils.isAsciiPrintable(userInput.third) == false);
    }

    @Test
    public void shouldSetFirstSecondAndThirdCardForValid3Card() {
        UserInput userInput = new UserInput(VALID_3_CARD, context);
        assertTrue(userInput.first == 'A');
        assertTrue(userInput.second == 'B');
        assertTrue(userInput.third == 'C');
    }

    @Test
    @UseDataProvider("inputSanitising")
    public void testUserInputSanitisation(String input, String expected) {
        val sanitizedInput = UserInput.UserInputValidator.formatInput(input);
        assertTrue(sanitizedInput.equals(expected));
    }
}
