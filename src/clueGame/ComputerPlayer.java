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
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

	// Constructor
	public ComputerPlayer(String name, Color pieceColor, int row, int column) {
		super(name, pieceColor, row, column);
	}
	
	/*================================================
	 * makeMove: Selects a move to make, 
	 * If a target is in a room and the room is not in that player's seen list, 
	 * select the room (or if multiple rooms select randomly).
     * Otherwise, select a target randomly from the target list.
	 ==================================================*/
	
	@Override
	public void makeMove() {
		
		Board board = Board.getInstance();
	    Random rand = new Random();
	    int diceRoll = rand.nextInt(6) + 1; // Generate a random number between 1 and 6

	    board.calcTargets(board.getCell(getRow(), getCol()), diceRoll);
	    Set<BoardCell> possibleTargets = board.getTargets();

	    ArrayList<BoardCell> unseenRoomTargets = new ArrayList<>();

	    // Check for rooms in targets that the player hasn't seen
	    for (BoardCell target : possibleTargets) {
	        if (target.isRoomCenter() && !getSeen().contains(new Card(target.getRoom().getName(), Card.CardType.ROOM))) {
	            unseenRoomTargets.add(target);
	        }
	    }

	    BoardCell chosenTarget;
	    if (!unseenRoomTargets.isEmpty()) {
	        // Randomly select unseen targe
	        chosenTarget = unseenRoomTargets.get(rand.nextInt(unseenRoomTargets.size()));
	    } else {
	        // Randomly select availible target
	        int targetIndex = rand.nextInt(possibleTargets.size());
	        chosenTarget = (BoardCell) possibleTargets.toArray()[targetIndex];
	    }

	    // Move the player 
	    setRow(chosenTarget.getRow());
	    setCol(chosenTarget.getCol());

	    //System.out.println(getName() + " (computer) rolled a " + diceRoll + " and moved to " + chosenTarget);
	}
	
	/*================================================
	 * createSuggestion: used by bots to create suggestions
	 * uses current room the bot is in, and completes the 
	 * suggestions with weapon and player the bot hasn't seen 
	 * yet
	 * 
	 ==================================================*/
	public Solution createSuggestion() {
		BoardCell currentCell = Board.getInstance().getCell(getRow(), getCol());
	    Room currentRoom = currentCell.getRoom();
	    Card roomCard = new Card(currentRoom.getName(), Card.CardType.ROOM);
		//Card roomCard = null;
	    
        Card personCard = null;
        Card weaponCard = null;
        
        
        
        
        ArrayList<Card> unseenPersons = new ArrayList<>();
        ArrayList<Card> unseenWeapons = new ArrayList<>();

        for (Card card : Board.getInstance().getCards()) {
            if (card.getType() == Card.CardType.PERSON && !getSeen().contains(card)) {
                unseenPersons.add(card);
            } else if (card.getType() == Card.CardType.WEAPON && !getSeen().contains(card)) {
                unseenWeapons.add(card);
            }
        }

        if (!unseenPersons.isEmpty()) {
            Random rand = new Random();
            personCard = unseenPersons.get(rand.nextInt(unseenPersons.size()));
        }

        if (!unseenWeapons.isEmpty()) {
            Random rand = new Random();
            weaponCard = unseenWeapons.get(rand.nextInt(unseenWeapons.size()));
        }

        return new Solution(roomCard, personCard, weaponCard);
	}
}

