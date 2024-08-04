package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSolutionTests {
	
	private static Board board;

	@BeforeAll
	static void setUp() {
		// Board is singleton, get the only instance
		Board.createInstance();
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");		
		// Initialize will load config files 
		board.initialize();
	}
	
	// ==-- TO TEST --==\\
	// checkAccusation() - INCLUDED
	// disproveSuggestion() - INCLUDED
	// handleSuggestion() - INCLUDED
	
	@Test
	/*Checks:
	 * [X] solution correct
	 * [X] solutions that that have wrong room, person, room 
	 */
	public void accusationTests() {
		Solution correctSolution = board.revealAnswer();
		assertTrue(board.accusationCheck(correctSolution));
		
		Card solutionPerson = new Card("person", Card.CardType.PERSON);
		Card solutionWeapon = new Card("weapon", Card.CardType.WEAPON);
		Card solutionRoom = new Card("room", Card.CardType.ROOM);
		Solution newAnswer = new Solution(solutionRoom, solutionPerson, solutionWeapon);
		board.setAnswer(newAnswer);
		
		Solution badRoom = new Solution(new Card("a different room", Card.CardType.ROOM), solutionPerson, solutionWeapon);
		assertFalse(board.accusationCheck(badRoom));
		
		Solution badPerson = new Solution(solutionRoom, new Card("Mike Tyson", Card.CardType.PERSON), solutionWeapon);
		assertFalse(board.accusationCheck(badPerson));
		
		Solution badWeapon = new Solution(solutionRoom, solutionPerson, new Card("a banana", Card.CardType.WEAPON));
		assertFalse(board.accusationCheck(badWeapon));
	}
	
	@Test
	/*Checks:
	 * [] only 1 matching card returned
	 * [] if >1 matching card, random matching card returned
	 * [] if no matching cards, null returned
	 */
	public void disproveSuggestionTests() {
		
	}
	
	@Test
	/*Checks:
	 * [X] undisprovable suggestion returns null
	 * [X] suggestion suggesting player can disprove returns null
	 * [X] suggestion only human can disprove returns answer
	 * [] suggestion 2 can disprove, correct player disproves (next in list)
	 */
	public void handleSuggestionTests() {
		ComputerPlayer bot1 = new ComputerPlayer("bot1", Color.black, 3, 3);
		bot1.overrideHand(new ArrayList<>());
		bot1.updateHand(new Card("weapon1", Card.CardType.WEAPON));
		bot1.updateHand(new Card("room1", Card.CardType.ROOM));
		bot1.updateHand(new Card("person1", Card.CardType.PERSON));
		
		ComputerPlayer bot2 = new ComputerPlayer("bot2", Color.black, 3, 3);
		bot2.overrideHand(new ArrayList<>());
		bot2.updateHand(new Card("weapon2", Card.CardType.WEAPON));
		bot2.updateHand(new Card("room2", Card.CardType.ROOM));
		bot2.updateHand(new Card("person2", Card.CardType.PERSON));
		
		HumanPlayer human = new HumanPlayer("human", Color.black, 3, 3);
		human.overrideHand(new ArrayList<>());
		human.updateHand(new Card("weapon3", Card.CardType.WEAPON));
		human.updateHand(new Card("room3", Card.CardType.ROOM));
		human.updateHand(new Card("person3", Card.CardType.PERSON));
		
		
	
		ArrayList<Player> playerList = new ArrayList<Player>();
		playerList.add(bot1);
		playerList.add(bot2);
		playerList.add(human);
		
		//playerList -> [bot1, bot2, human]
		
		board.setPlayers(playerList);
		
		//unprovable suggestion
		Solution unprovable = new Solution(new Card("room4", Card.CardType.ROOM), new Card("person4", Card.CardType.PERSON), new Card("weapon4", Card.CardType.WEAPON));
		assertNull(board.handleSuggestion(unprovable, bot1));
		
		Solution suggestingPlayer = new Solution(new Card("room2", Card.CardType.ROOM), new Card("person4", Card.CardType.PERSON), new Card("weapon4", Card.CardType.WEAPON));
		assertNull(board.handleSuggestion(suggestingPlayer, bot2));
		
		Solution humanSolution = new Solution(new Card("room3", Card.CardType.ROOM), new Card("person4", Card.CardType.PERSON), new Card("weapon4", Card.CardType.WEAPON));
		assertEquals("room3", board.handleSuggestion(humanSolution, bot2).getName());
		
		Solution correctRotation = new Solution(new Card("room1", Card.CardType.ROOM), new Card("person2", Card.CardType.PERSON), new Card("weapon4", Card.CardType.WEAPON));
		assertEquals("room1", board.handleSuggestion(humanSolution, human).getName());
	}

	
}