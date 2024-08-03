package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class GameSolutionTests {
	
	private static Board board;

	@BeforeAll
	static void setUp() {
		// Board is singleton, get the only instance
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
	 * [] solution correct
	 * [] solutions that that have wrong room, person, room 
	 */
	public void accusationTests() {
		
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
	 * [] undisprovable suggestion returns null
	 * [] suggestion suggesting player can disprove returns null
	 * [] suggestion only human can disprove returns answer
	 * [] suggestion 2 can disprove, correct player disproves (next in list
	 */
	public void handleSuggestionTests() {
		
	}

	
}