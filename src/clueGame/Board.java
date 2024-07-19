package clueGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Board {
    private BoardCell[][] grid;
    public int numRows;
    public int numCols;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Map<Character, Room> roomMap;
    private static Board theInstance = new Board();

    // Private constructor for singleton pattern
    private Board() {
    	roomMap = new HashMap<>();
    }

    // Static method to get the single instance of the Board
    public static Board getInstance() {
        return theInstance;
    }

    public void initialize() {
        // Implementation to initialize the board
    	setConfigFiles(layoutConfigFile, setupConfigFile);
    	loadLayoutConfig();
    }

    public void loadSetupConfig() {
//        roomMap.put('C', new Room("Conservatory"));
//        roomMap.put('B', new Room("Ballroom"));
//        roomMap.put('R', new Room("Billiard Room"));
//        roomMap.put('D', new Room("Dining Room"));
//        roomMap.put('W', new Room("Walkway"));
    }

    public void loadLayoutConfig() { 	
         numRows = 25;
         numCols = 24;
         grid = new BoardCell[numRows][numCols];
    }
    
    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.layoutConfigFile = layoutConfigFile;
        this.setupConfigFile = setupConfigFile;
    }
    
    public Room getRoom(char initial) {
        return roomMap.get(initial);
    }
    
    public Room getRoom(BoardCell cell) {
        for (Room room : roomMap.values()) {
            if (room.getCenterCell() == cell || room.getLabelCell() == cell) {
                return room;
            }
        }
        return roomMap.get(cell.getSecretPassage());
    }
    
    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numCols;
    }

    public BoardCell getCell(int row, int col) {
        return grid[row][col];
    }
    
    
}