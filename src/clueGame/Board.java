package clueGame;

/*Board class
 * 
 * @author Colin Myers
 * @author Ethan Eshbaugh
 * 
 * Lays out the behavior of the Board for clue game
 * 
 */

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Board {
    private BoardCell[][] grid;
    public int numRows;
    public int numCols;
    private String layoutConfigFile;
    private String setupConfigFile;
    public Map<Character, Room> roomMap;
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

    //Read csv file and set up cells and rooms to fill board
    public void loadSetupConfig() {
        try (Scanner scanner = new Scanner(new File(setupConfigFile))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith("//") || line.isEmpty()) {
                continue; // Skip comments and empty lines
            }
            String[] parts = line.split(", ");
            if (parts.length == 3) {
                String type = parts[0];
                String name = parts[1];
                char initial = parts[2].charAt(0);

                if (type.equals("Room") || type.equals("Space")) {
                    Room room = new Room(name);
                    roomMap.put(initial, room);
                } else {
                    throw new BadConfigFormatException("Invalid type in setup configuration file: " + type);
                }
            } else {
                throw new BadConfigFormatException("Invalid format in setup configuration file");
            }
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (BadConfigFormatException e) {
        System.out.println(e.getMessage());
    }
    }

    public void loadLayoutConfig() { 	
         numRows = 25;
         numCols = 24;
         grid = new BoardCell[numRows][numCols];
    }
    
    //set config files
    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.layoutConfigFile = layoutConfigFile;
        this.setupConfigFile = setupConfigFile;
    }
    
    public Room getRoom(char initial) {
        //return roomMap.get(initial);
    	return new Room("temp");
    }
    
    //return a room
    public Room getRoom(BoardCell cell) {
        for (Room room : roomMap.values()) {
            if (room.getCenterCell() == cell || room.getLabelCell() == cell) {
                return room;
            }
        }
        return new Room("temp");
    }
    
    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numCols;
    }

    public BoardCell getCell(int row, int col) {
        //return grid[row][col];
    	return new BoardCell(row,col);
    }
    
    
}