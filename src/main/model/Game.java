package model;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static model.UserInput.UserInputStatus.*;

public class Game {
    public static Deck DECK = new Deck();
    public static Card[] inPlay = new Card[9]; //placeholder for 3x3 grid
    public static Card[] discard = new Card[52];
    private static final int ASCII_OFFSET = 65;

    public static void start() {
        DECK = new Deck();
        for (int i = 0; i < 9; i++) {
            inPlay[i] = DECK.drawCard();
        }
        displayBoard();
        while (!isWon() && !isStalemate()) {
            makeTurn();
            configureInPlay();
            displayBoard();
        }
        endMessages();
    }

    public static void displayMenu() {
        int choice=0; // 0 will allow the sitch case to display the menu again as anything not between 1 - 3 is an incorect option
        System.out.println("Hey there, Welcome To Elevens");
        System.out.println("1 --> Play Game\n2 --> Rules\n3 --> Quit\nEnter option: ");

        var rawUserInput = new Scanner(System.in).nextLine();
        try {
             choice=Integer.parseInt(rawUserInput);
        }catch (Exception e){
            //nothing needs to happen here as it will already be set to 0 if it fails
        }
        try {
            switch (choice) {
                case 1:
                    start();
                    break;
                case 2:
                    System.out.println("\nHOW TO PLAY ELEVENS\n");
                    System.out.println("Elevens is extremely similar to Bowling Solitaire, except that the layout is a little different and the goal is to make matching pairs that add up to 11 rather than adding matching pairs up to 10.\n" +
                            "\n" +
                            "Empty spaces in the 9-card formation are automatically filled by placing a card from the Deck in the free space. Once you run out of cards in the Deck, do not fill the empty spaces in the card formation with any other cards.\n" +
                            "\n" +
                            "To play this game, look at your 9-card formation and see if any cards can be matched that add up to 11 in total. If you have a matching pair that can create this sum, then you may remove them from place. Once you’ve done so, remember to fill in the gaps left by these two cards with two cards from the Deck.\n" +
                            "\n" +
                            "Only cards in the 9-card formation are available to play with, and you may not build any cards on top of each other during the game. Cards cannot be removed from the Deck unless they are being placed in the table layout, and you should not look at the cards in the Deck before moving them into play. They must remain unknown until they are flipped over to be placed in the 9-card formation.\n" +
                            "\n" +
                            "The ranking of cards matches their face value i.e. the two of clubs is equal to two. Aces hold a value of one and Jacks, Queens, and Kings equal eleven only when they are removed together. For example, if you have a Jack and King on your board you can’t remove either until a Queen appears. Once all three cards are present on the board they can be removed together to make “11”. They are the only cards in the game that are moved as a trio, rather than being matched as a pair.");
                    System.out.println("HOW TO WIN:\n" +
                            "\n" +
                            "To win at a round of Elevens, you must remove absolutely all cards from play – including those from the Deck. Once you have matched all cards in the Deck, then you have won the round.\n" +
                            "\n" +
                            "It is possible to play this game with more than one player. To do so, you could create a scoring system by having each player keep their matched pairs and making each set worth 1 point. The player with the highest number of points would win the game. Typically, this is a solo player game, but it’s extremely easy to make into a family-friendly or party game.\n");
                    displayMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Please choose a correct option");
                    displayMenu();
            }
        }catch (InputMismatchException e){
            System.out.println(e.getStackTrace());
            System.out.println("Please choose a correct number from 1 - 3!");
            displayMenu();
        }
    }

    private static boolean isStalemate() {
        // Better approach
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            Card firstCard = getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            if (getUniqueInPlayCardValues(false)
                    .anyMatch(value -> (firstCard.getRankValue() + value) == 9)) {
                return false;
            }
        }
        return getUniqueInPlayCardValues(true).sum() != 33;
    }

    private static IntStream getUniqueInPlayCardValues(boolean isFaceCards) {
        return Arrays.stream(inPlay)
                .filter(isFaceCards ? Card::isFaceCard : Predicate.not(Card::isFaceCard))
                .mapToInt(Card::getRankValue)
                .distinct();
    }

    private static void configureInPlay() {
        // Refill the deck
        for (int i = 0; i < inPlay.length; i++) {
            if (inPlay[i] == null) {
                inPlay[i] = DECK.drawCard();
            }
        }
        // If there are empty spaces, push them all to the bottom
        reformatInPlay();
    }

    private static void reformatInPlay() {
        Card[] newInPlay = new Card[9];
        int blanksToAdd = 0;
        for (int i = 0; i < newInPlay.length; i++) {
            if (inPlay[i] == null) {
                blanksToAdd++;
                continue;
            }
            newInPlay[i] = inPlay[i];
        }
        for (int i = 0; i < blanksToAdd; i++) {
            newInPlay[8 - i] = null;
        }
        inPlay = newInPlay;
    }

    public static void displayBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%s: %s\n", (char) ('A' + i), inPlay[i]);
        }
    }

    public static Boolean isValidPlayerMove(char first, char second, char third) {
        Card firstCard = getCardFromBoard(first);
        Card secondCard = getCardFromBoard(second);
        Card thirdCard = getCardFromBoard(third);
        // If a third card is present, then we assume we are removing a JQK triple
        if (thirdCard != null) {
            // JQK triple
            return isValidJQKSelection(firstCard, secondCard, thirdCard);
        } else {
            // Two cards add to give 11
            return isValid2CardSelection(firstCard, secondCard);
        }
    }

    private static boolean isValid2CardSelection(Card first, Card second) {
        int sum = first.getRankValue() + second.getRankValue();
        return !first.isFaceCard() && !second.isFaceCard() && sum == 9;
    }

    private static boolean isValidJQKSelection(Card first, Card second, Card third) {
        int sum = first.getRankValue() + second.getRankValue() + third.getRankValue();
        return first.getRankValue() != second.getRankValue() &&
                first.getRankValue() != third.getRankValue() &&
                second.getRankValue() != third.getRankValue() &&
                sum == 33;
    }

    // Returns if the move was valid or not
    public static boolean makeTurn() {
        System.out.println("Enter Letters: ");
        var rawUserInput = new Scanner(System.in).nextLine();
        UserInput userInput = new UserInput(rawUserInput);
        if (userInput.status != Valid2Card && userInput.status != Valid3Card) {
            System.out.println(userInput.status.getMessage());
            return false;
        }
        applyUserInput(userInput);
        return true;
    }

    public static boolean applyUserInput(UserInput userInput) {
        // After this point, we can assume that the user input is valid, as all validation is done within UserInputValidator
        if (isValidPlayerMove(userInput.first, userInput.second, userInput.third) &&
                isSelectionValid(userInput.first) && isSelectionValid(userInput.second)) {
            if (userInput.status == Valid2Card) {
                removeCardFromBoard(userInput.first);
                removeCardFromBoard(userInput.second);
            } else {
                if (isSelectionValid(userInput.third)) {
                    removeCardFromBoard(userInput.first);
                    removeCardFromBoard(userInput.second);
                    removeCardFromBoard(userInput.third);
                } else {
                    System.out.println("Invalid selection for 3rd card");
                    return false;
                }
            }
        } else {
            if(userInput.status == Valid2Card){
                System.out.println("The two cards you have chosen do not add up to 11, try again!");
            }
            if(userInput.status == Valid3Card){
                System.out.println("The three cards you have chosen are not a Jack, Queen or King, try again!");
            }
            System.out.println("Psst, if you're really stuck, you can type 'x' for a hint!");
            return false;
        }
        return true;
    }

    public static String getHint() {
        if (isStalemate()) return "";
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            Card firstCard = getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            var otherCardValue = getUniqueInPlayCardValues(false)
                    .filter(value -> (firstCard.getRankValue() + value) == 9)
                    .findFirst().orElse(-1);
            if (otherCardValue != -1)
                return String.valueOf(Character.toUpperCase(first)) + (char) (getAsciiCharacterOfInPlay(otherCardValue));
        }
        return Character.toString(getAsciiCharacterOfInPlay(10))
                + Character.toString(getAsciiCharacterOfInPlay(11))
                + Character.toString(getAsciiCharacterOfInPlay(12));
    }

    public static boolean makeValidMove() {
        UserInput userInput = new UserInput(getHint());
        if (userInput.status.equals(Empty)) return false;
        applyUserInput(userInput);
        return true;
    }

    public static void displayHint() {
        System.out.println("You requested a hint!\nPsst, your hint is " + getHint());
    }


    public static int getAsciiCharacterOfInPlay(int value) {
        return IntStream.range(0, inPlay.length)
                .filter(x -> inPlay[x].getRankValue() == value)
                .findFirst().orElse(-999) + ASCII_OFFSET;
    }

    public static boolean isWon() {
        return Arrays.stream(inPlay).allMatch(Objects::isNull);
    }

    public static Card getCardFromBoard(char selection) {
        try {
            return inPlay[selection - ASCII_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static void removeCardFromBoard(char selection) {
        discard[(int) Arrays.stream(discard).filter(Objects::nonNull).count()] = inPlay[selection - ASCII_OFFSET];
        inPlay[selection - ASCII_OFFSET] = null;
    }

    // In here, we should be validating if the selected character is in the game board, and if there is a card at this location on the board
    public static boolean isSelectionValid(char selection) {
        return !(selection < 'A' || selection > 'I' || inPlay[selection - ASCII_OFFSET] == null);
    }

    public static void endMessages(){
        Scanner scanner = new Scanner(System.in);
        if(isWon()){
            System.out.println("Congratualations you win! :) \nTry Again? (y/n)");
            String userInput = scanner.nextLine().toLowerCase();
            switch (userInput){
                case "y":
                    start();
                    break;
                case "n":
                    break;
            }
        }
        if(isStalemate()){
            System.out.println("Unfortunately this time you have lost! :( \nTry Again? (y/n)");
            String userInput = scanner.nextLine().toLowerCase();
            switch (userInput){
                case "y":
                    start();
                    break;
                case "n":
                    break;
            }
        }
    }
}