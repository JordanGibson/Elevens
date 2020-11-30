import model.UserInput;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserInputTest {
    @Test
    public void ShouldReturnEmptyWhenInputIsEmpty() {
        UserInput userInput = new UserInput("");
        assertThat(userInput.status, is(UserInput.UserInputStatus.Empty));
    }

    @Test
    public void ShouldReturnEmptyWhenInputIsWhitespace() {
        UserInput userInput = new UserInput("             ");
        assertThat(userInput.status, is(UserInput.UserInputStatus.Empty));
    }
}
