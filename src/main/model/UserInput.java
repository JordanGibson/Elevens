package model;

import lombok.val;

import java.io.Serializable;

import static model.UserInput.UserInputStatus.*;

public class UserInput implements Serializable {

    public final char first;
    public final char second;
    public final char third;
    public final UserInputStatus status;

    public UserInput(String input, Game context) {
        status = UserInputValidator.validate(input, context);
        if (isInvalid()) {
            first = second = third = '?';
            return;
        }
        val parsedInput = UserInputValidator.formatInput(input);
        first = parsedInput.charAt(0);
        second = parsedInput.charAt(1);
        third = parsedInput.length() == 3 ? parsedInput.charAt(2) : 0;
    }

    public boolean isInvalid() {
        return status != Valid2Card && status != Valid3Card;
    }

    private static class UserInputValidator {
        public static UserInputStatus validate(String input, Game context) {
            val parsedInput = formatInput(input);
            if (parsedInput.isEmpty()) {
                return Empty;
            } else if (parsedInput.length() == 1 && parsedInput.equals("X")) {
                return Hint;
            } else if (parsedInput.length() != 2 && parsedInput.length() != 3) {
                return InvalidNumberOfCards;
            }

            val first = parsedInput.charAt(0);
            val second = parsedInput.charAt(1);
            val third = parsedInput.length() > 2 ? parsedInput.charAt(2) : 0;

            if (first == second || first == third || second == third) {
                return SameCard;
            } else if (!context.isSelectionValid(first)) {
                return InvalidFirstCard;
            } else if (!context.isSelectionValid(second)) {
                return InvalidSecondCard;
            } else if (parsedInput.length() > 2 && !context.isSelectionValid(third)) {
                return InvalidThirdCard;
            }
            return third == 0 ? Valid2Card : Valid3Card;

        }

        private static String formatInput(String input) {
            return input.replaceAll(" ", "")
                    .replaceAll(",", "")
                    .toUpperCase();
        }
    }

    public enum UserInputStatus {
        Empty("Input was blank!"),
        Hint(""),
        InvalidNumberOfCards("You must select 2 or 3 cards!"),
        SameCard("You must select unique cards!"),
        InvalidFirstCard("The first card you selected was not part of the game board!"),
        InvalidSecondCard("The second card you selected was not part of the game board!"),
        InvalidThirdCard("The third card you selected was not part of the game board!"),
        Valid2Card(""),
        Valid3Card("");

        private final String message;

        UserInputStatus(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
