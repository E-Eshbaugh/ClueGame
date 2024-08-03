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
	
	/*================================================
	 * createSuggestion: used my bots to create suggestions
	 * uses current room the bot is in, and completes the 
	 * suggestions with weapon and player the bot hasn't seen 
	 * yet
	 * 
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
            if (card.getType() == Card.CardType.PERSON && !seen().contains(card)) {
                unseenPersons.add(card);
            } else if (card.getType() == Card.CardType.WEAPON && !seen().contains(card)) {
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

