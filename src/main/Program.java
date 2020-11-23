package main;

import main.game.Game;

public class Program {
    public static void main(String[] args) {
        while(!Game.DECK.isEmpty()) {
            System.out.println(Game.DECK.pop());
        }
    }
}
