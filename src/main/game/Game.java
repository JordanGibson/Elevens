package main.game;

import main.Card;

public class Game {
    public static Deck DECK = new Deck();
    public static Card[] InPlay = new Card[9]; //placeholder for 3x3 grid
    public static Deck ThrowPile = new Deck();//cards paired go here

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
