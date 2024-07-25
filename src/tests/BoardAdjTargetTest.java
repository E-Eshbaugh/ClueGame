package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTest {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
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

	// Ensure that player does not move around within room
	// These cells are LIGHT ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesRooms()
	{
		// Dungeon Center
		Set<BoardCell> testList = board.getAdjList(3, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(7, 3)));
		assertTrue(testList.contains(board.getCell(37, 25)));
		
		// Library center
		testList = board.getAdjList(26, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(26, 7)));
		assertTrue(testList.contains(board.getCell(22, 3)));
		
		// Courtyard center
		testList = board.getAdjList(20, 24);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(14, 23)));
		assertTrue(testList.contains(board.getCell(20, 18)));
		assertTrue(testList.contains(board.getCell(26, 23)));
		
		//only walkways
		testList = board.getAdjList(8, 21);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(7, 21)));
		assertTrue(testList.contains(board.getCell(9, 21)));
		assertTrue(testList.contains(board.getCell(8, 20)));
		assertTrue(testList.contains(board.getCell(8, 22)));
		
		// room non center
		testList = board.getAdjList(1, 1);
		assertEquals(0, testList.size());
		
		//beside room no door
		testList = board.getAdjList(35, 6);
		assertEquals(2, testList.size());
		assertFalse(testList.contains(board.getCell(35, 5)));
		assertTrue(testList.contains(board.getCell(35, 7)));
		
		
		//doorway
		testList = board.getAdjList(7, 3);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(8, 3)));
		assertFalse(testList.contains(board.getCell(6, 3)));
		
		//secret tunnel
		testList = board.getAdjList(3, 25);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(37, 3)));
		assertTrue(testList.contains(board.getCell(7, 25)));
	}
	
	// Tests out of room center, 1, 3 and 4
	//tests with secret passage
	@Test
	public void testTargetsInTower() {
		// test a roll of 1
		board.calcTargets(board.getCell(37, 25), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(33, 25)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(37, 25), 3);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(31, 25)));	
		assertTrue(targets.contains(board.getCell(33, 23)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(37, 25), 4);
		targets= board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));	
		assertTrue(targets.contains(board.getCell(30, 25)));
		assertTrue(targets.contains(board.getCell(32, 23)));	
	}
	
	@Test
	//test targets out of non secret passage room
	public void testTargetsInArmory() {
		Set<BoardCell> targets= board.getTargets();
		// test a roll of 1
		board.calcTargets(board.getCell(15, 3), 1);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(19, 3)));
		assertTrue(targets.contains(board.getCell(15, 7)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	// entrance into rooms and walkways
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(7, 14), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 14)));
		assertTrue(targets.contains(board.getCell(7, 13)));	
		assertTrue(targets.contains(board.getCell(7, 15)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(7, 14), 3);
		targets= board.getTargets();
		assertEquals(13, targets.size());
		assertTrue(targets.contains(board.getCell(3, 14)));
		assertTrue(targets.contains(board.getCell(7, 13)));	
		assertTrue(targets.contains(board.getCell(7, 11)));
		assertTrue(targets.contains(board.getCell(6, 16)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(7, 14), 4);
		targets= board.getTargets();
		assertEquals(16, targets.size());
		assertTrue(targets.contains(board.getCell(7, 18)));
		assertTrue(targets.contains(board.getCell(10, 13)));	
		assertTrue(targets.contains(board.getCell(7, 10)));
		assertTrue(targets.contains(board.getCell(3, 14)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 3 blocked 1 down
		board.getCell(6, 21).setOccupied(true);
		board.calcTargets(board.getCell(7, 21), 3);
		board.getCell(6, 21).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(5, 22)));
		assertTrue(targets.contains(board.getCell(5, 20)));
		assertFalse( targets.contains( board.getCell(4, 21))) ;
		assertFalse( targets.contains( board.getCell(6, 21))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(20, 24).setOccupied(true);
		board.calcTargets(board.getCell(20, 18), 1);
		board.getCell(20, 24).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(20, 24)));	
		assertTrue(targets.contains(board.getCell(19, 18)));		
		
		// check leaving a room with a blocked doorway
		board.getCell(22, 3).setOccupied(true);
		board.calcTargets(board.getCell(26, 3), 3);
		board.getCell(22, 3).setOccupied(false);
		targets= board.getTargets();
		assertEquals(5, targets.size());
		assertTrue(targets.contains(board.getCell(26, 9)));
		assertTrue(targets.contains(board.getCell(27, 8)));	
		assertTrue(targets.contains(board.getCell(25, 8)));

	}
}
