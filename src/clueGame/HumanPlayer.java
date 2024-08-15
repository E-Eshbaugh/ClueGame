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
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

//auto generated suppress warning
@SuppressWarnings("serial")
public class HumanPlayer extends Player {
	
	private BoardCell targetCell;
	private BoardCell tempRoomForAdjacent;
	
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
	public BoardCell getTempRoomForAdjacent() {
		return tempRoomForAdjacent;
	}


	public void setTempRoomForAdjacent(BoardCell tempRoomForAdjacent) {
		this.tempRoomForAdjacent = tempRoomForAdjacent;
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
   	    
   	// Check if the player was already in the room due to a previous suggestion and decided to stay
   	    boolean wasInRoom = currentCell.isRoomCenter();
    	    

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
   	    
   	// Check if the player is in a room and wasn't already there due to a previous suggestion
   	 if (targetCell.isRoomCenter() && !wasInRoom) {
         // Create and display the suggestion dialog
         JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
         Room currentRoom = targetCell.getRoom();
         ArrayList<String> people = new ArrayList<>();
         ArrayList<String> weapons = new ArrayList<>();

         // Fill the people and weapons lists from the game data
         for (Card card : board.getCards()) {
             if (card.getType() == Card.CardType.PERSON) people.add(card.getName());
             if (card.getType() == Card.CardType.WEAPON) weapons.add(card.getName());
         }

         SuggestionDialog suggestionDialog = new SuggestionDialog(parentFrame, currentRoom.getName(), people, weapons);
         suggestionDialog.setVisible(true);

         // Get the suggestion and handle it
         Solution suggestion = suggestionDialog.getSuggestion();
         if (suggestion != null) {
             board.handleSuggestion(suggestion, this);
         }
     }

     ClueGame.turnOver = true;
    }


	
}
