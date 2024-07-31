package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSetupTests {
	
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}

	@Test
	//test number of players for a standard game
	void numPlayers() {
		assertEquals(board.getPlayers().size(), 6);
	}
	
	@Test
	//test number of cards in game and number for each type
	void numCards() {
		assertEquals(board.getCards().size(), 21);
		ArrayList<Card.CardType> seenCardTypes = new ArrayList<Card.CardType>();
		
		//check the card types of each card and make sure there are 3 types
		for (Card card : board.getCards()) {
			if (!seenCardTypes.contains(card.getType())) {
				seenCardTypes.add(card.getType());
			}
		}
		assertEquals(seenCardTypes.size(), 3);
	}
	
}
