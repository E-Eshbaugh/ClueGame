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
		assertTrue(testList.contains(board.getCell(3, 7)));
		assertTrue(testList.contains(board.getCell(25, 37)));
		
		// Library center
		testList = board.getAdjList(3, 26);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(7, 26)));
		assertTrue(testList.contains(board.getCell(3, 22)));
		
		// Courtyard center
		testList = board.getAdjList(24, 20);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(23, 14)));
		assertTrue(testList.contains(board.getCell(18, 20)));
		assertTrue(testList.contains(board.getCell(23, 26)));
		
		//only walkways
		testList = board.getAdjList(21, 8);
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCell(21, 7)));
		assertTrue(testList.contains(board.getCell(21, 9)));
		assertTrue(testList.contains(board.getCell(20, 8)));
		assertTrue(testList.contains(board.getCell(22, 8)));
		
		// room non center
		testList = board.getAdjList(1, 1);
		assertEquals(0, testList.size());
		
		//beside room no door
		testList = board.getAdjList(6, 35);
		assertEquals(2, testList.size());
		assertFalse(testList.contains(board.getCell(6, 34)));
		assertTrue(testList.contains(board.getCell(7, 35)));
		
		
		//doorway
		testList = board.getAdjList(3, 7);
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(3, 8)));
		assertFalse(testList.contains(board.getCell(4, 7)));
		
		//secret tunnel
		testList = board.getAdjList(25, 3);
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(25, 7)));
	}
	
	// Tests out of room center, 1, 3 and 4
	//tests with secret passage
	@Test
	public void testTargetsInTower() {
		// test a roll of 1
		board.calcTargets(board.getCell(25, 37), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(25, 33)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(25, 37), 3);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(25, 33)));
		assertTrue(targets.contains(board.getCell(24, 32)));	
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(4, 7)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(25, 37), 4);
		targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCell(25, 37)));
		assertTrue(targets.contains(board.getCell(3, 7)));	
		assertTrue(targets.contains(board.getCell(25, 30)));
		assertTrue(targets.contains(board.getCell(23, 32)));	
	}
	
	@Test
	//test targets out of non secret passage room
	public void testTargetsInArmory() {
		Set<BoardCell> targets= board.getTargets();
		// test a roll of 1
		board.calcTargets(board.getCell(3, 15), 1);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCell(3, 19)));
		assertTrue(targets.contains(board.getCell(7, 15)));	
	}

	// Tests out of room center, 1, 3 and 4
	// These are LIGHT BLUE on the planning spreadsheet
	// entrance into rooms and walkways
	@Test
	public void testTargetsAtDoor() {
		// test a roll of 1, at door
		board.calcTargets(board.getCell(7, 15), 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 15)));
		assertTrue(targets.contains(board.getCell(7, 14)));	
		assertTrue(targets.contains(board.getCell(8, 15)));	
		
		// test a roll of 3
		board.calcTargets(board.getCell(7, 15), 3);
		targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCell(3, 15)));
		assertTrue(targets.contains(board.getCell(3, 20)));
		assertTrue(targets.contains(board.getCell(10, 15)));	
		assertTrue(targets.contains(board.getCell(7, 11)));
		assertTrue(targets.contains(board.getCell(4, 19)));	
		
		// test a roll of 4
		board.calcTargets(board.getCell(7, 15), 4);
		targets= board.getTargets();
		//might not actually be 17 - hard to keep track (double check later when testing)
		assertEquals(17, targets.size());
		assertTrue(targets.contains(board.getCell(7, 15)));
		assertTrue(targets.contains(board.getCell(7, 11)));
		assertTrue(targets.contains(board.getCell(3, 21)));	
		assertTrue(targets.contains(board.getCell(7, 17)));
		assertTrue(targets.contains(board.getCell(8, 16)));	
	}

	@Test
	// test to make sure occupied locations do not cause problems
	public void testTargetsOccupied() {
		// test a roll of 3 blocked 1 down
		board.getCell(6, 21).setOccupied(true);
		board.calcTargets(board.getCell(7, 21), 4);
		board.getCell(6, 21).setOccupied(false);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(14, targets.size());
		assertTrue(targets.contains(board.getCell(5, 22)));
		assertTrue(targets.contains(board.getCell(5, 20)));
		assertFalse( targets.contains( board.getCell(4, 21))) ;
		assertFalse( targets.contains( board.getCell(6, 21))) ;
	
		// we want to make sure we can get into a room, even if flagged as occupied
		board.getCell(24, 20).setOccupied(true);
		board.calcTargets(board.getCell(18, 20), 1);
		board.getCell(24, 20).setOccupied(false);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(24, 20)));	
		assertTrue(targets.contains(board.getCell(19, 20)));		
		
		// check leaving a room with a blocked doorway
		board.getCell(18, 20).setOccupied(true);
		board.calcTargets(board.getCell(3, 3), 1);
		board.getCell(12, 15).setOccupied(false);
		targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(3, 9)));	
		assertTrue(targets.contains(board.getCell(24, 8)));

	}
}
