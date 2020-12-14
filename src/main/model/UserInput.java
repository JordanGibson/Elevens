package model;

import static model.UserInput.UserInputStatus.*;

public class UserInput {

    public final char first;
    public final char second;
    public final char third;
    public final UserInputStatus status;

    public UserInput(String input) {
        status = UserInputValidator.validate(input);
        if (!status.equals(Valid2Card) && !status.equals(Valid3Card)) {
            first = second = third = '?';
            if (status.equals(Hint)) {
                Game.displayHint();
                return;
            }
            return;
        }
        var parsedInput = UserInputValidator.formatInput(input);
        first = parsedInput.charAt(0);
        second = parsedInput.charAt(1);
        third = parsedInput.length() == 3 ? parsedInput.charAt(2) : 0;
    }

    private static class UserInputValidator {
        public static UserInputStatus validate(String input) {
            var parsedInput = formatInput(input);
            if (parsedInput.isEmpty()) {
                return Empty;
            } else if (parsedInput.length() == 1 && parsedInput.equals("X")) {
                return Hint;
            } else if (parsedInput.length() != 2 && parsedInput.length() != 3) {
                return InvalidNumberOfCards;
            }

            char first = parsedInput.charAt(0);
            char second = parsedInput.charAt(1);
            char third = parsedInput.length() > 2 ? parsedInput.charAt(2) : 0;

            if (first == second || first == third || second == third) {
                return SameCard;
            } else if (!Game.isSelectionValid(first)) {
                return InvalidFirstCard;
            } else if (!Game.isSelectionValid(second)) {
                return InvalidSecondCard;
            } else if (parsedInput.length() > 2 && !Game.isSelectionValid(third)) {
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
