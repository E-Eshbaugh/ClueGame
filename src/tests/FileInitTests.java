package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;

public class FileInitTests {
	
	public static final int LEGEND_SIZE = 23;
	public static final int NUM_ROWS = 42;
	public static final int NUM_COLUMNS = 29;
	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		Board.createInstance();
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		assertEquals("Courtyard", board.getRoom('C').getName() );
		assertEquals("Ballroom", board.getRoom('B').getName() );
		assertEquals("Tower", board.getRoom('T').getName() );
		assertEquals("Dungeon", board.getRoom('D').getName() );
		assertEquals("Unused", board.getRoom('X').getName() );
	}

	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
	// two cells that are not a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell cell = board.getCell(7, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(15, 7);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(3, 10);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(22, 3);
		assertTrue(cell.isDoorway());
		assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(8, 3);
		assertFalse(cell.isDoorway());
		//tests second non-door
		cell = board.getCell(30, 0);
		assertFalse(cell.isDoorway());
	}
	

	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() {
		int numDoors = 0;
		for (int row = 0; row < NUM_ROWS; row++)
			for (int col = 0; col < NUM_COLUMNS; col++) {
				BoardCell cell = board.getCell(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(17, numDoors);
	}


// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRooms() {
		//standard room location
		BoardCell cell = board.getCell( 1, 1);
		Room room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Dungeon" ) ;
		assertFalse( cell.isLabel() );
		assertFalse( cell.isRoomCenter() ) ;
		assertFalse( cell.isDoorway()) ;

		//label cell test
		cell = board.getCell(0, 14);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Great Hall") ;
		assertTrue( cell.isLabel() );
		assertTrue( room.getLabelCell() == cell );
		
		//room center cell test
		cell = board.getCell(15, 3);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Armory" ) ;
		assertTrue( cell.isRoomCenter() );
		assertTrue( room.getCenterCell() == cell );
		
		//Secret passage test -> Tower to Dungeon
		cell = board.getCell(39, 27);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Tower" ) ;
		assertTrue( cell.getSecretPassage() == 'D' );
		
		//test a walkway
		cell = board.getCell(9, 7);
		room = board.getRoom( cell );
		assertTrue( room != null );
		assertEquals( room.getName(), "Walkway" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
		// test an Unused space
		cell = board.getCell(0, 0);
		room = board.getRoom( cell ) ;
		assertTrue( room != null );
		assertEquals( room.getName(), "Unused" ) ;
		assertFalse( cell.isRoomCenter() );
		assertFalse( cell.isLabel() );
		
	}
	
	//test initial and final cell are unused
	@Test
	public void testFirstLast() {
		BoardCell firstCell = board.getCell(0,0);
		//col 27 and not 28 because 28 is Null type, (27,41) is last cell of any type
		BoardCell lastCell = board.getCell(41,27);
		Room firstRoom = board.getRoom(firstCell);
		Room lastRoom = board.getRoom(lastCell);
		assertEquals(firstRoom.getName(), "Unused");
		assertEquals(lastRoom.getName(), "Unused");
		
	}

}

