package clueGame;

import java.awt.Color;

public class HumanPlayer extends Player {

    // Constructor
    public HumanPlayer(String name, Color color, int row, int column) {
        super(name, color, row, column);
    }

    @Override
    public void makeMove() {
        // Implementation for human player making a move
        System.out.println(getName() + " is making a move.");
    }
}