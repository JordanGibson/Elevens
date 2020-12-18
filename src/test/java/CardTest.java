import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import model.Card;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(DataProviderRunner.class)
public class CardTest {

    @DataProvider
    public static Object[][] cardExamples() {
        return new Object[][]{
                {0, "Ace of Clubs"},
                {12, "King of Clubs"},
                {15, "3 of Hearts"},
                {40, "2 of Diamonds"}
        };
    }

    @Test
    @UseDataProvider("cardExamples")
    public void cardToStringWorks(int input, String expected) {
        Card card = new Card(input);
        assertTrue(card.toString().equals(expected));
    }
}
