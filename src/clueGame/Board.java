package clueGame;

import java.util.HashMap;
import java.util.Map;
//import java.util.Set;
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
//    	//layoutConfigFile = "ClueLayout306.csv";
    	layoutConfigFile =  "C:\\Users\\User\\eclipse-workspace\\ClueGame\\ClueInitFiles\\data\\ClueLayout306.csv";
    	setupConfigFile = "C:\\Users\\User\\eclipse-workspace\\ClueGame\\ClueInitFiles\\data\\ClueLayout306.txt";
    	setConfigFiles(layoutConfigFile, setupConfigFile);
    	loadLayoutConfig();
    }

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
//         grid = new BoardCell[numRows][numCols];
         try (Scanner scanner = new Scanner(new File(layoutConfigFile))) {
             int row = 0;
             while (scanner.hasNextLine()) {
                 String line = scanner.nextLine().trim();
                 String[] values = line.split(",");
                 if (row == 0) {
                     numCols = values.length;
                 }
                 if (grid == null) {
                     numRows = 25; // You can dynamically adjust this if needed
                     grid = new BoardCell[numRows][numCols];
                 }
                 for (int col = 0; col < values.length; col++) {
                     grid[row][col] = new BoardCell(row, col, values[col].trim().charAt(0));
                 }
                 row++;
             }
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         }
         printGrid();
    }
    
    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.layoutConfigFile = layoutConfigFile;
        this.setupConfigFile = setupConfigFile;
    }
    
    public void printGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print(grid[row][col].getInitial() + " ");
            }
            System.out.println();
        }
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
        //temp fix
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