package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.*;

class ComputerAITest {
	
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
	// computer decision making
	// selectTargets() - INCLUDED
	// createSuggestion() - INCLUDED
	
	
	
	@Test
	/*Checks:
	 * [X] Room matches current location
	 * [x] If only 1 of any card type not seen(weapon/person), that is the one chosen
	 * [X] if multiple not seen (weapon/person) random one chosen
	 */
	public void botPlayerSuggestionTests() {
		//create test subject
		ComputerPlayer testSubject = new ComputerPlayer("Test Subject", Color.BLUE, 3, 3);
		Solution testSubjectSuggestion = testSubject.createSuggestion();
		
		//check room - checks room string names\\
		assertEquals(testSubjectSuggestion.getRoom().getName(), board.getCell(3, 3).getRoom().getName());
		
		//check if the only 1 not seen card for a type is chosen\\
		//weapons - missing Sword [index 0], people - missing Knight [index 6 - 5 after sword remove]
		ArrayList<Card> feedToSubject = board.getCards();
		feedToSubject.remove(0);
		feedToSubject.remove(5);
		testSubject.overrideSeen(feedToSubject);
		Solution NewTestSubjectSuggestion = testSubject.createSuggestion();
		assertEquals(NewTestSubjectSuggestion.getWeapon(), board.getCards().get(0));
		assertEquals(NewTestSubjectSuggestion.getPerson(), board.getCards().get(6));
		
		//check random selection when multiple unseen
		testSubject.overrideSeen(new ArrayList<Card>());
		for (int i = 0; i < 10000; i++) {
			Solution loopSuggestion = testSubject.createSuggestion();
			testSubject.updateSeen(loopSuggestion.getWeapon());
			testSubject.updateSeen(loopSuggestion.getPerson());
		}
		assertEquals(testSubject.getSeen().size(), 12);
		
	}
	
	//@Test
	/*Checks:
	 * [] if no rooms in list, random selection
	 * [] if room in list has not been seen, select it
	 * [] if room in list that has been seen, each target (including room) is random choice
	 */
	//public void botPlayerSelectTargetTests() {
		
		
	//}
}