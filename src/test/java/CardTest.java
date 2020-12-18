import model.Card;
import model.Deck;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CardTest {
    @Test
    public void BoilerplateTest() {
        assertThat(1, is(1));
    }
}
