package main.game;

public class Game {
    public static Deck DECK = new Deck();

    //(!Game.DECK.isEmpty())

    public static void StartGame(){
        for (int i = 1; i < 9; i++) {
            System.out.println(Game.DECK.pop());
        }

    }
    public static void DrawCards(){

    }
    public static Boolean isValidMatch(){
        return true;
    }
    public static Boolean isValidUserInput(){
        if(1==1){//placeholder for if user input is 2 chars
            //check if cards are not royals and add to 11
            return true;
        }else if(1==1){//placeholder for if user input is 3 chars
            //check if cards are royals
            return true;
        }else{//if neither are correct
            return false;//fail the vibe check
        }
    }
    public static void IsGameWon(){

    }
    public static void NextTurn(){

    }
    public static void MoveToWastePile(){

    }


}
