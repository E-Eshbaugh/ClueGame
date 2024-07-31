package clueGame;
/*HumanPlayer class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * outlines the behaviors and attributes of the HumanPlayer realization of Player
 * Child of Player
 * 
 * only ever 1 HumanPlayer
 * 
 * 7/31/2024
 */

import java.awt.Color;

public class HumanPlayer extends Player {

	/*==========================
	 * HumanPlayer Constructor
	 =======================*/
	public HumanPlayer(String name, Color color, int row, int col, boolean human) {
		// TODO Auto-generated constructor stub
		super(name, color, row, col, human);
	}

    @Override
    /*=====================================
     * HumanPlayer override for movement
     ====================================*/
    public void makeMove() {
        // Implementation for human player making a move
        System.out.println(getName() + " is making a move.");
    }
}
