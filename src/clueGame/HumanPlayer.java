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
import java.awt.event.MouseListener;

//auto generated suppress warning
@SuppressWarnings("serial")
public class HumanPlayer extends Player {
	
	private BoardCell targetCell;

	/*==========================
	 * HumanPlayer Constructor
	 =======================*/
	public HumanPlayer(String name, Color color, int row, int col) {
		// TODO Auto-generated constructor stub
		super(name, color, row, col);
	}
	
	
	/*======================
	 * Getters and setters
	===================== */
	
	public void setTarget(BoardCell target) {
		targetCell = target;
	}
	

    @Override
    /*=====================================
     * HumanPlayer override for movement
     ====================================*/
    public void makeMove() {
    	// Move the player to the clicked cell
    	Board board = Board.getInstance();
   		BoardCell currentCell = board.getCell(getRow(), getCol());
   	    setRow(targetCell.getRow());
   	    setCol(targetCell.getCol());
    	    

   	     // Mark the old cell as not occupied
   	    if(currentCell.numPlayersInRoom == 1) {
   	    	currentCell.setOccupied(false);
   	    	currentCell.repaint();
   	    	} else {
   	    		currentCell.numPlayersInRoom--;
    	    	currentCell.repaint();
    	    	
    	    }
    	    
   	    targetCell.setOccupied(true); // Mark the new cell as occupied
    	    
        // Unhighlight all cells and remove listeners
   	    for (BoardCell[] row : board.getGrid()) {
   	    	for (BoardCell cell : row) {
   	    		if (board.getTargets().contains(cell)) {
   	    			cell.setHighlighted(false);
   	    			cell.repaint(); // Repaint to remove the highlight
   	    		}
    	
   	    		// Remove all added listeners
    		    for (MouseListener listener : cell.getMouseListeners()) {
    		    	cell.removeMouseListener(listener);
    		    }
    	    }
    	}
    }
}
