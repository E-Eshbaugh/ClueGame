package clueGame;


import java.awt.Color;

public class ComputerPlayer extends Player {

	// Constructor
	public ComputerPlayer(String name, Color color, int row, int column) {
		super(name, color, row, column);
	}

	@Override
	public void makeMove() {
		// Implementation for computer player making a move
		System.out.println(getName() + " (computer) is making a move.");
	}
}

