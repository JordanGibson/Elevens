package model;

import collection.PlayerMoveHistory;
import collection.PlayerMoveHistoryEntry;
import lombok.val;

import java.io.Serializable;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static model.UserInput.UserInputStatus.Hint;

public class Game implements Serializable {
    public static final int ASCII_OFFSET = 65;
    public static final Scanner scanner = new Scanner(System.in);

    private final Card[] inPlay = new Card[9];
    private PlayerMoveHistory playerMoveHistory;
    private ElevensDeck deck;

    public void start() {
        deck = new ElevensDeck(this);
        initBoard();
        playerMoveHistory = new PlayerMoveHistory(deck, inPlay);
        displayBoard();
        while (!isWon() && !isStalemate()) {
            makeTurn();
            refillInPlay();
            displayBoard();
        }
        endMessages();
    }

    public void displayMenu() {
        int choice=0; // 0 will allow the sitch case to display the menu again as anything not between 1 - 3 is an incorect option
        System.out.println("Buckle up bucko we playing elevens now");
        System.out.println("1 --> Play Game\n2 --> Rules\n3 --> Quit\nEnter option: ");

        var rawUserInput = scanner.nextLine();
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
                    gameRules();
                    displayMenu();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Please choose a correct option");
                    displayMenu();
            }
        }catch (InputMismatchException e){
            System.out.println("Please choose a correct number from 1 - 3!");
            displayMenu();
        }
    }


    private void initBoard() {
        for (int i = 0; i < 9; i++) {
            inPlay[i] = deck.drawCard();
        }
    }

    protected IntStream getUniqueInPlayCardValues(boolean isFaceCards) {
        return Arrays.stream(inPlay)
                .filter(Objects::nonNull)
                .filter(isFaceCards ? Card::isFaceCard : Predicate.not(Card::isFaceCard))
                .mapToInt(Card::getRankValue)
                .distinct();
    }

    private void refillInPlay() {
        // Refill the deck
        for (int i = 0; i < inPlay.length; i++) {
            if (inPlay[i] == null) {
                inPlay[i] = deck.drawCard();
            }
        }
    }

    protected void displayBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%s: %s\n", (char) ('A' + i), inPlay[i]);
        }
    }

    protected void makeTurn() {
        System.out.print("Enter Letters: ");
        val rawUserInput = scanner.nextLine();
        val userInput = new UserInput(rawUserInput, this);
        if (userInput.status == Hint) {
            displayHint();
            return;
        } else if (userInput.isInvalid()) {
            System.out.println(userInput.status.getMessage());
            return;
        }
        val cardsToRemove = deck.getCardsToRemove(userInput);
        if (cardsToRemove.length > 0) {
            playerMoveHistory.addEntry(new PlayerMoveHistoryEntry(cardsToRemove));
            Arrays.stream(cardsToRemove).forEach(this::removeCardFromBoard);
            System.out.println("Removed " + Arrays.stream(cardsToRemove).map(Card::toString).collect(Collectors.joining(" and ")));
        }
    }

    protected void displayHint() {
        System.out.println("You requested a hint!\nPsst, your hint is " + deck.getHint());
    }

    protected int getAsciiCharacterOfInPlay(int value) {
        return IntStream.range(0, inPlay.length)
                .filter(x -> inPlay[x].getRankValue() == value)
                .findFirst()
                .orElse(-999) + ASCII_OFFSET;
    }

    protected boolean isStalemate() {
        for (char first = ASCII_OFFSET; first < 9 + ASCII_OFFSET; first++) {
            val firstCard = getCardFromBoard(first);
            if (firstCard == null || firstCard.isFaceCard()) continue;
            if (getUniqueInPlayCardValues(false)
                    .anyMatch(value -> {
                        if (value < 0) return false;
                        return (firstCard.getRankValue() + value) == 9;
                    })) {
                return false;
            }
        }
        return getUniqueInPlayCardValues(true).sum() != 33;
    }

    protected boolean isWon() {
        return Arrays.stream(inPlay).allMatch(Objects::isNull);
    }

    protected Card getCardFromBoard(char selection) {
        try {
            return inPlay[selection - ASCII_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    protected void removeCardFromBoard(Card card) {
        Arrays.stream(IntStream.range(0, inPlay.length)
                .toArray())
                .filter(index -> {
                    try {
                        return inPlay[index].equals(card);
                    } catch (NullPointerException e) {
                        return false;
                    }
                })
                .findFirst()
                .ifPresent(index -> inPlay[index] = null);
    }

    // In here, we should be validating if the selected character is in the game board, and if there is a card at this location on the board
    protected boolean isSelectionValid(char selection) {
        return !(selection < 'A' || selection > 'I' || inPlay[selection - ASCII_OFFSET] == null);
    }

    public void endMessages(){
        if(isWon()){
            System.out.println("Congratualations you win! :) \n");
            movesPlayback();

        }
        if(isStalemate()) {
            System.out.println("Unfortunately this time you have lost! :( \n");
            movesPlayback();
        }
    }

    public void movesPlayback(){
        System.out.println("would you like to see your moves back? (y/n) : ");
        String choice = scanner.nextLine();
        if (choice.equals("y")) playerMoveHistory.replay(); playAgain();
    }

    public void playAgain(){
        System.out.println("Play again? (y/n) : ");
        String userInput = scanner.nextLine().toLowerCase();
        if(userInput.equals("y"))start();
        return;
    }

    public void gameRules(){
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
        System.out.println("Press 'x' for a hint as well, good luck!\n");
    }
}
