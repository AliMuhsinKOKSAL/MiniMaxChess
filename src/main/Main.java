package main;

import java.io.IOException;

import board.BoardCreator;
import game_ui.BoardUI;

public class Main {

    public static void main(String[] args) throws IOException {
    	BoardCreator.cBoard.init();
    	
//    	new mini_max.Simulation();
    	
        new BoardUI();
    }
}
