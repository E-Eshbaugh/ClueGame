package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSetupTests {
	
	private static Board board;

	@BeforeAll
	static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
		
		System.out.println(board.getCards());
	}

	@Test
	//test number of players read in from txt
	void numPlayers() {
		assertEquals(board.getPlayers().size(), board.getNumPlayers());
	}
	
	@Test
	//test number of cards in game and number for each type
	void numCards() {
		assertEquals(21, board.getNumCards());
		ArrayList<Card.CardType> seenCardTypes = new ArrayList<Card.CardType>();
		
		//check the card types of each card and make sure there are 3 types
		for (Card card : board.getCards()) {
			if (!seenCardTypes.contains(card.getType())) {
				seenCardTypes.add(card.getType());
			}
		}
		assertEquals(seenCardTypes.size(), 3);
	}

	@Test
	//Test composition of HumanPlayer
	void testHuman() {
		//test number of humans
		int numHumans =  0;
		for (Player player : board.getPlayers()) {
			if (player.isHuman()) numHumans++;
		}
		assertEquals(numHumans ,1);
		
		//test the characteristics
		for (Player player : board.getPlayers()) {
			if (player.isHuman()) {
				assertNotNull(player.getName());
				assertNotNull(player.getColor());
				assertEquals(board.getNumCards()/board.getNumPlayers(), player.getHand().size());
				assertTrue(player.isHuman());
				
			}
		}
	}
	
	@Test
	//tests composition of ComputerPlayer
	void testBots() {
		//test number of humans
		int numBot =  0;
		for (Player player : board.getPlayers()) {
			if (!player.isHuman()) numBot++;
		}
		assertEquals(numBot ,5);
		
		//test the characteristics
		for (Player player : board.getPlayers()) {
			if (!player.isHuman()) {
				assertNotNull(player.getName());
				assertNotNull(player.getColor());
				assertEquals(board.getNumCards()/board.getNumPlayers(), player.getHand().size());
				assertFalse(player.isHuman());
				
			}
		}
	}
	
	@Test
	//test the solution
	void testSolution() {
		assertNotNull(board.revealAnswer().getWeapon());
		assertNotNull(board.revealAnswer().getRoom());
		assertNotNull(board.revealAnswer().getPerson());
	}
	
	@Test
	//test the deal
	void testDeal() {
		//make sure all dealt
		for (Card card : board.getCards()) {
			assertTrue(card.isHasBeenDealt());
		}
		//make sure all equalish dealt out
		for (Player player : board.getPlayers()) {
			assertEquals(board.getNumCards()/board.getNumPlayers(), player.getHand().size());
		}
		
		//make sure no duplicates of cards
		ArrayList<Card> seenCard = new ArrayList<Card>();
		for (Player player : board.getPlayers()) {
			for (Card card : player.getHand()) {
				assertFalse(seenCard.contains(card));
			}
		}
		
	}

}
