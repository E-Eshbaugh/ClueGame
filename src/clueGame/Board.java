package clueGame;

import java.util.HashMap;
import java.util.Map;
//import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Board {
    private BoardCell[][] grid;
    public int numRows;
    public int numCols;
    private String layoutConfigFile;
    private String setupConfigFile;
    private Path layoutConfigPath;
    private Path setupConfigPath;
    public Map<Character, Room> roomMap;
    private static Board theInstance = new Board();

    // Private constructor for singleton pattern
    private Board() {
    	roomMap = new HashMap<>();
    	layoutConfigFile =  "ClueLayout306.csv";
    	setupConfigFile = "ClueSetup306.txt";
    	layoutConfigPath = Paths.get("ClueInitFiles", "data", layoutConfigFile);
    	setupConfigPath = Paths.get("ClueInitFiles", "data", setupConfigFile);
    }

    // Static method to get the single instance of the Board
    public static Board getInstance() {
        return theInstance;
    }

    public void initialize() {
        // Implementation to initialize the board
    	loadLayoutConfig();
    	loadSetupConfig();
    }
    
    //read and interpret the key for the rooms (txt file)
    public void loadSetupConfig() {
    	try (Scanner scanner = new Scanner(Files.newInputStream(setupConfigPath))) {
    		while (scanner.hasNextLine()) {
    			String line = scanner.nextLine().trim();
    			if (line.startsWith("//") || line.isEmpty()) {
    				continue; // Skip comments and empty lines
    			}
    			//split the legend into the 3 seperate parts
    			String[] parts = line.split(", ");
    			if (parts.length == 3) {
    				String type = parts[0];
    				String name = parts[1];
    				char initial = parts[2].charAt(0);

    				if (type.equals("Room") || type.equals("Space")) {
    					Room room = new Room(name);
    					roomMap.put(initial, room);
    					//a line isnt formatted properly
    				} else {
    					//append to errorlog before throwing exception
    					try (FileWriter errorLogWrite = new FileWriter("errorlog.txt", true)) {
    						errorLogWrite.write("BadConfigFormatException thrown for " + setupConfigFile + " ... Bad value in file");
    					} catch (Exception e) {
    						System.out.println("ERROR WRITING TO errorlog.txt");
    						e.printStackTrace();
    					}
    					throw new BadConfigFormatException("Invalid type in setup configuration file: " + type);
    				}
    				//bad file format overall
    			} else {
    				//append to errorlog before throwing exception
    				try (FileWriter errorLogWrite = new FileWriter("errorlog.txt", true)) {
    					errorLogWrite.write("BadConfigFormatException thrown for " + setupConfigFile + " ... Bad file format");
    				} catch (Exception e) {
    					System.out.println("ERROR WRITING TO errorlog.txt");
    					e.printStackTrace();
    				}
    				throw new BadConfigFormatException("Invalid format in setup configuration file");
    			}
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (BadConfigFormatException e) {
    		System.out.println(e.getMessage());
    	}
    	printRoomMap();
    }

    //load the layoutconfig file (csv file)
    public void loadLayoutConfig() { 	
         numRows = 0;
         numCols = 0;
//         grid = new BoardCell[numRows][numCols];
         try (Scanner scanner = new Scanner(Files.newInputStream(layoutConfigPath))) {
        	 //Get number of rows and columns
        	 while (scanner.hasNextLine()) {
        		 numRows++;
        		 String curLine = scanner.nextLine();
        		 numCols = curLine.length();
        	 }
        	 
         } catch (IOException e) {
             e.printStackTrace();
         }
         
         numCols = (numCols+1)/2;
         
         try (Scanner scanner = new Scanner(Files.newInputStream(layoutConfigPath))){
        	 int row = 0;
             while (scanner.hasNextLine()) {
                 String line = scanner.nextLine().trim();
                 String[] values = line.split(",");
                 if (row == 0) {
                     numCols = values.length;
                 }
                 if (grid == null) {
                     //numRows = 25; // You can dynamically adjust this if needed
                     grid = new BoardCell[numRows][numCols];
                 }
                 for (int col = 0; col < values.length; col++) {
                     grid[row][col] = new BoardCell(row, col, values[col].trim().charAt(0));
                 }
                 row++;
             }
         } catch (IOException e) {
        	 e.printStackTrace();
         }
         printGrid();
    }
    
    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.layoutConfigFile = layoutConfigFile;
        this.setupConfigFile = setupConfigFile;
        this.layoutConfigPath = Paths.get("ClueInitFiles", "data", this.layoutConfigFile);
    	this.setupConfigPath = Paths.get("ClueInitFiles", "data", this.setupConfigFile);
    }
    
    public void printGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print(grid[row][col].getInitial() + " ");
            }
            System.out.println();
        }
    }
    public void printRoomMap() {
        for (Map.Entry<Character, Room> entry : roomMap.entrySet()) {
            System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName());
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