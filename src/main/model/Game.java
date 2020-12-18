package model;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import model.PlayerMoveHistory.PlayerMoveHistoryEntry;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static model.UserInput.UserInputStatus.Hint;

@NoArgsConstructor
public class Game implements Serializable {
    public static final int ASCII_OFFSET = 65;
    public static final Scanner scanner = new Scanner(System.in);

    private final Card[] inPlay = new Card[9];
    private PlayerMoveHistory playerMoveHistory;
    private ElevensDeck deck;

    public Game(ElevensDeck deck) {
        this.deck = deck;
    }

    public void displayMenu() {
        var choice = 0; // 0 will allow the switch case to display the menu again as anything not between 1 - 4 is an incorrect option
        System.out.println("Buckle up bucko we playing elevens now");
        System.out.print("1 --> Play Game\n2 --> Rules\n3 --> Play Demonstration Game\n4 --> Quit\n\nEnter option: ");

        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ignored) {
        }

        switch (choice) {
            case 1:
                startGame();
                break;
            case 2:
                gameRules();
                displayMenu();
                break;
            case 3:
                startAutomatedGame();
                break;
            case 4:
                return;
            default:
                System.out.println("Please choose a correct option");
                displayMenu();
        }
    }

    public void startGame() {
        preStartRoutine();
        while (!isWon() && !isStalemate()) {
            makeTurn();
            refillInPlay();
            displayBoard();
        }
        endMessages();
    }

    public void startAutomatedGame() {
        preStartRoutine();
        while (!isWon() && !isStalemate()) {
            playerMoveHistory.addEntry(deck.makeValidMove(inPlay));
            System.out.println("\nPress enter to continue\n");
            scanner.nextLine();
            refillInPlay();
            displayBoard();
        }
        endMessages();
    }

    public void preStartRoutine() {
        do {
            this.deck = new ElevensDeck(this);
            initBoard();
        } while (isStalemate());
        playerMoveHistory = new PlayerMoveHistory(deck, inPlay);
        displayBoard();
    }

    public void initBoard() {
        for (int i = 0; i < 9; i++) {
            inPlay[i] = deck.drawCard();
        }
    }

    public IntStream getUniqueInPlayCardValues(boolean isFaceCards) {
        return Arrays.stream(inPlay)
                .filter(Objects::nonNull)
                .filter(isFaceCards ? Card::isFaceCard : Predicate.not(Card::isFaceCard))
                .mapToInt(Card::getRankValue)
                .distinct();
    }

    public void refillInPlay() {
        // Refill the deck
        for (int i = 0; i < inPlay.length; i++) {
            if (inPlay[i] == null) {
                inPlay[i] = deck.drawCard();
            }
        }
    }

    public void displayBoard() {
        for (int i = 0; i < 9; i++) {
            System.out.printf("%s: %s\n", (char) ('A' + i), inPlay[i]);
        }
    }

    public void makeTurn() {
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

    public void displayHint() {
        System.out.println("You requested a hint!\nPsst, your hint is " + deck.getHint());
    }

    public int getAsciiCharacterOfInPlay(int value) {
        return IntStream.range(0, inPlay.length)
                .filter(index -> inPlay[index] != null && inPlay[index].getRankValue() == value)
                .findFirst()
                .orElse(-999) + ASCII_OFFSET;
    }

    public boolean isStalemate() {
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

    public boolean isWon() {
        return Arrays.stream(inPlay).allMatch(Objects::isNull);
    }

    public Card getCardFromBoard(char selection) {
        try {
            return inPlay[selection - ASCII_OFFSET];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void removeCardFromBoard(Card card) {
        // This is important as the stream may produce duplicate values if we do not first convert it to an array, before converting it back to a stream
        Arrays.stream(IntStream.range(0, inPlay.length).toArray())
                .filter(index -> inPlay[index] != null && inPlay[index].equals(card))
                .findFirst()
                .ifPresent(index -> inPlay[index] = null);
    }

    // In here, we should be validating if the selected character is in the game board, and if there is a card at this location on the board
    public boolean isSelectionValid(char selection) {
        return !(selection < 'A' || selection > 'I' || inPlay[selection - ASCII_OFFSET] == null);
    }

    public void endMessages() {
        System.out.println(isWon() ?
                "Congratulations you win! :) \n" :
                "It's a stalemate! This time, you have lost! :( \n"
        );
        offerUserReplay();
        displayMenu();
    }

    @SneakyThrows
    public void offerUserReplay() {
        System.out.print("Type y and hit enter to watch a replay, or any other key to return to the main menu: ");
        val choice = scanner.nextLine();
        if (choice.equals("y")) playerMoveHistory.replay();
    }

    public void gameRules() {
        System.out.println("""
                             
                HOW TO PLAY ELEVENS
                                
                Elevens is extremely similar to Bowling Solitaire, except that the layout is a little different and the goal is to make matching pairs that add up to 11 rather than adding matching pairs up to 10.
                                
                Empty spaces in the 9-card formation are automatically filled by placing a card from the Deck in the free space. Once you run out of cards in the Deck, do not fill the empty spaces in the card formation with any other cards.
                                
                To play this game, look at your 9-card formation and see if any cards can be matched that add up to 11 in total. If you have a matching pair that can create this sum, then you may remove them from place. Once you’ve done so, remember to fill in the gaps left by these two cards with two cards from the Deck.
                                
                Only cards in the 9-card formation are available to play with, and you may not build any cards on top of each other during the game. Cards cannot be removed from the Deck unless they are being placed in the table layout, and you should not look at the cards in the Deck before moving them into play. They must remain unknown until they are flipped over to be placed in the 9-card formation.
                                
                The ranking of cards matches their face value i.e. the two of clubs is equal to two. Aces hold a value of one and Jacks, Queens, and Kings equal eleven only when they are removed together. For example, if you have a Jack and King on your board you can’t remove either until a Queen appears. Once all three cards are present on the board they can be removed together to make “11”. They are the only cards in the game that are moved as a trio, rather than being matched as a pair.);

                HOW TO WIN:

                To win at a round of Elevens, you must remove absolutely all cards from play – including those from the Deck. Once you have matched all cards in the Deck, then you have won the round.

                It is possible to play this game with more than one player. To do so, you could create a scoring system by having each player keep their matched pairs and making each set worth 1 point. The player with the highest number of points would win the game. Typically, this is a solo player game, but it’s extremely easy to make into a family-friendly or party game.
                                
                Press 'x' for a hint as well, good luck!
                                
                """);
    }
}
