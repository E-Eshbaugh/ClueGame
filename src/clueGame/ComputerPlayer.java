package clueGame;
/*ComputerPlayer class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * outlines the behaviors and attributes of the ComputerPlayer realization of Player
 * Child of Player
 * 
 * Standard number for game: 5
 * 
 * 7/31/2024
 */


import java.awt.Color;

public class ComputerPlayer extends Player {

	// Constructor
	public ComputerPlayer(String name, Color pieceColor, int row, int column) {
		super(name, pieceColor, row, column);
	}

	@Override
	public void makeMove() {
		// Implementation for computer player making a move
		System.out.println(getName() + " (computer) is making a move.");
	}
}

