/*Board class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Myers
 * 
 * outlines the behaviors of the Board for clue game, controls board actions
 * info in class -> [the game board ("grid"), where each player is, player movement targets]
 * 
 */

package clueGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Board {
	private BoardCell[][] grid;
	public int numRows;
	public int numColumns;
	private String configFileCSV;
	private String configFileTXT;
	private Path csvFilePath;
	private Path txtFilePath;
	public Map<Character, Room> roomMap;
	public Map<Character, Room> roomCenterMap;
	private static Board theInstance = new Board();
	private Set<BoardCell> targets;
	private ArrayList<Player> players;
	private ArrayList<Card> Deck;
	private Solution theAnswer;
	


    /*=============================================
     *  Private constructor for singleton pattern
     =============================================*/
    private Board() {
    	roomMap = new HashMap<>();
    	roomCenterMap = new HashMap<>();
    	configFileCSV =  "ClueLayout306.csv";
    	configFileTXT = "ClueSetup306.txt";
    	csvFilePath = Paths.get("ClueInitFiles", "data", configFileCSV);
    	txtFilePath = Paths.get("ClueInitFiles", "data", configFileTXT);
    }

    
    
    /*==================================
     * Getters & Setters
     ===================================*/
	public Room getRoom(char initial) {
		return roomMap.get(initial);
	}

	public Room getRoom(BoardCell cell) {
		return cell.getRoom();
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		return getCell(i,j).getAdjList();
	}

	public Set<BoardCell> getTargets() { 
		return targets;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addCard(Card card) {
    	Deck.add(card);
    }
    public ArrayList<Card> getCards() {
        return Deck;
    }
	
	
	
	/*===============================================
	 * Generates a player and adds to player list
	 * Used by loadSetupConfig
	 ==============================================*/
	public void generatePlayer(String name, Color color, int row, int col, boolean human) {
		if (human) {
			players.add(new HumanPlayer(name, color, row, col));
		}
		else players.add(new ComputerPlayer(name, color, row, col));
	}
    
	
	
	/*============================================
	 * Randomly deal the cards out to the players
	 * Calls shuffle
	 ============================================*/
	public void deal() {
		
	}
	
	
	
	/*=======================================================================
	 * Creates and shuffles cards into a random order in cards ArrayList
	 * Used by deal()
	 ========================================================================*/
	public void shuffle() {
		
	}
	
	
    
	/*========================================================
	 * Static method to get the single instance of the Board
	 ========================================================*/
	public static Board getInstance() {
		return theInstance;
	}
	
	
	
	/*============================================
	 * sets the config files and the 
	 * relative paths to them
	 ==============================================*/
    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.configFileCSV = layoutConfigFile;
        this.configFileTXT = setupConfigFile;
        this.csvFilePath = Paths.get("ClueInitFiles", "data", this.configFileCSV);
    	this.txtFilePath = Paths.get("ClueInitFiles", "data", this.configFileTXT);
    }
	
	
	
	/* ============================================================
	 * Updates row and col nums based on the file and initializes grid
	 * Uses File Scanner -> exceptions thrown handled in method
	 ==============================================================*/
	public void updateDimensions() {
		try (Scanner scanner = new Scanner(Files.newInputStream(csvFilePath))){
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				this.numRows++;
				this.numColumns = line.length();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//get correct length (minus the ,'s)
		this.numColumns = (this.numColumns+1)/2;
		
		//initialize grid
		grid = new BoardCell[numRows][numColumns];
	}
	
	
	
	/*==========================================
	 * Visualizes map to console
	 * Calls printCenterRoomMap & printRoomMap
	 ============================================*/
	public void visualizeMap() {
		printGrid();
		printCenterRoomMap();
		printRoomMap();
	}

	
	
	/* ==================================================================================
	 * Initializes grid base on the files
	 * Calls loadSetupConfig, loadLayoutConfig, and setAdjacencies
	 * Calls visualizeMap if not commented out - prints map to console
	 * Catches BadConfigFormatExceptions from loadLayoutConfig & loadSetupConfig
	 * 
	 * Runs once per board generation
	 ===============================================================================*/
    public void initialize() {
	    try {
	    	loadSetupConfig();
			loadLayoutConfig();
			setAdjacencies();
			deal();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    //====comment out to stop map from printing in console====
	    visualizeMap();
    }
   
    
    
	/*============================================================
	 * read and interpret the key for the rooms (TXT file)
	 * splits the file at commas and puts room type, name, and character into an string Array[3]
	 * 
	 * Uses File Scanner, IOExceptions and BadConfigFormatExceptions handled in method
	 =============================================================*/
	public void loadSetupConfig() throws BadConfigFormatException{
		try (Scanner scanner = new Scanner(Files.newInputStream(txtFilePath))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith("//") || line.isEmpty()) {
					continue; // Skip comments and empty lines
				}
				//split the legend into the 3 separate parts
				String[] parts = line.split(", ");
				if (parts.length == 3) {
					String type = parts[0];
					String name = parts[1];
					char initial = parts[2].charAt(0);
					if (type.equals("Room") || type.equals("Space")) {
						Room room = new Room(name);
						roomMap.put(initial, room);
						//a line isn't formatted properly
					} else {
						throw new BadConfigFormatException("Invalid type in setup configuration file: " + type);
					}
					//bad file format overall
				} else {
					throw new BadConfigFormatException("Invalid format in setup configuration file");
				}
			}
		} catch (Exception e) {
			throw new BadConfigFormatException();
		}
	}

	
	
	/*===================================================================
	 * load the layoutconfig file (CSV file)
	 * Calls updateDimensions, setRoomCenterInitials, and setRoomsCenterCells
	 * Uses File Scanner, exceptions handled in method
	 =====================================================================*/
	public void loadLayoutConfig() throws BadConfigFormatException{ 	
		this.updateDimensions();
		try (Scanner scanner = new Scanner(Files.newInputStream(csvFilePath))) {
			int row = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] values = line.split(",");
				if (row == 0) {
					numColumns = values.length;
				}
				for (int col = 0; col < numColumns; col++) {
					String cellValue = values[col].trim();
					char initial = cellValue.charAt(0);
					BoardCell cell = new BoardCell(row, col, cellValue);


					Room originalRoom = roomMap.get(initial);
					if (originalRoom != null) {
						Room roomCopy = new Room(originalRoom.getName());
						cell.setRoom(roomCopy);
					}

					// Check for special characters
					for (int i = 1; i < cellValue.length(); i++) {
						char special = cellValue.charAt(i);
						switch (special) {
						case '*':
							cell.setRoomCenter(true);
							cell.getRoom().setCenterCell(cell);
							break;
						case '#':
							cell.setLabel(true);
							cell.getRoom().setLabelCell(cell);
							break;
						case '<':
							cell.setDoorDirection(DoorDirection.LEFT);
							cell.getRoom().setDoorDirection(DoorDirection.LEFT);
							break;
						case '^':
							cell.setDoorDirection(DoorDirection.UP);
							cell.getRoom().setDoorDirection(DoorDirection.UP);
							break;
						case 'v':
							cell.setDoorDirection(DoorDirection.DOWN);
							cell.getRoom().setDoorDirection(DoorDirection.DOWN);
							break;
						case '>':
							cell.setDoorDirection(DoorDirection.RIGHT);
							cell.getRoom().setDoorDirection(DoorDirection.RIGHT);
							break;
						default:
							cell.setSecretPassage(special);
							cell.getRoom().setSecretPassage(special);
							break;
						}
					}
					grid[row][col] = cell;
				}
				row++;
			}
			
			//set up centers mapping
			setRoomCenterInitials();
			setRoomsCenterCell();
			
		} catch (Exception e) {
			throw new BadConfigFormatException();
		}
	}
	
	
	
	/* =================================================================
	 * Fill the roomCenterMap
	 * Maps all room center cells to their appropriate room symbols
	 ==================================================================*/
	private void setRoomCenterInitials() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                BoardCell cell = grid[row][col];
                if (cell.isRoomCenter()) {
                    Room room = cell.getRoom();
                    if (room != null) {
                        room.setCenterCell(cell);
						roomCenterMap.put(Character.valueOf(cell.getInitial().charAt(0)), cell.getRoom()); 
						
                    }
                }
            }
        }
    }
	
	

	/*================================================================
	 * Searches whole board and maps all room cells to their 
	 * correct room center tile
	 ================================================================*/
	private void setRoomsCenterCell() {
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            BoardCell cell = grid[row][col];
	            char initial = cell.getInitial().charAt(0);
	            if(cell.getInitial().length() == 2 ) {
	            	initial = cell.getInitial().charAt(1);
	            } 
	            if (roomCenterMap.containsKey(initial)) {
	                Room room = roomMap.get(initial);
	                if (room != null) {
	                    BoardCell centerCell = roomCenterMap.get(initial).getCenterCell();
	                    cell.getRoom().setCenterCell(centerCell);
	                }
	            }
	        }
	    }
	}
	

	
	/*==================================================
	 * Provides a visual representation of the game map
	 * for easier grid spot location
	=================================================*/
	public void printGrid() {
		for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            String initial = grid[row][col].getInitial();
	            if (initial.length() > 1) {
	                System.out.print(initial + ""); // Add extra space if the value is more than one letter
	            } else {
	                System.out.print(initial + " ");
	            }
	        }
	        System.out.println();
	    }
	}
	
	
	
	/*=================================
	 * Prints out the Room map with the 
	 * room initial and name
	 ====================================*/
	public void printRoomMap() {
		for (Map.Entry<Character, Room> entry : roomMap.entrySet()) {
			System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName());
		}
	}
	
	
	
	/*====================================
	 * Prints out the CenterRoomMap with the 
	 * room initial and room center cell
	 ======================================*/
	public void printCenterRoomMap() {
		for (Map.Entry<Character, Room> entry : roomCenterMap.entrySet()) {
			System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName()+  entry.getValue().getCenterCell());
		}
	}
	
	public void printDeck() {
		for (Card card : Deck) {
	        System.out.println(card);
	    }
	}
	
	
	
	/*=============================================================================
	 * initializes cell adjacency lists for each cell on the board
	 * only runs once for whole game
	 ==========================================================================*/
	private void setAdjacencies() {
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            BoardCell cell = grid[row][col];

	            if (cell.getInitial().equals("W") || cell.isDoorway()) {
	                addWalkwayOrDoorAdjacencies(row, col, cell);
	                addDoorwayCenterAdjacency(row, col, cell);
	            }

	            if (cell.isRoomCenter()) {
	                addRoomCenterAdjacencies(cell);
	            }

	            if (cell.getSecretPassage() != '\0') {
	                addSecretPassageAdjacency(cell);
	            }
	        }
	    }
	}
	/*=============================================================================
	 * Used by setAdjacencies to add all possible walkways
	 ==========================================================================*/
	private void addWalkwayOrDoorAdjacencies(int row, int col, BoardCell cell) {
	    addAdjacency(cell, row - 1, col); // Up
	    addAdjacency(cell, row + 1, col); // Down
	    addAdjacency(cell, row, col - 1); // Left
	    addAdjacency(cell, row, col + 1); // Right
	}
	/*=============================================================================
	 * Used by addWalkwayOrDoorAdjacencies performs logic to ensure cell should 
	 * be added and if so adds it.
	 ==========================================================================*/
	private void addAdjacency(BoardCell cell, int adjRow, int adjCol) {
	    if (adjRow >= 0 && adjRow < numRows && adjCol >= 0 && adjCol < numColumns) {
	        BoardCell adj = grid[adjRow][adjCol];
	        if (adj.getInitial().equals("W") || adj.isDoorway()) {
	            cell.addAdj(adj);
	        }
	    }
	}
	/*=============================================================================
	 * Used by SetAdjacencies, adds the center room cell to adjacencies list
	 * for Doorways
	 ==========================================================================*/
	private void addDoorwayCenterAdjacency(int row, int col, BoardCell cell) {
	    if (cell.isDoorway()) {
	        BoardCell adj = null;
	        switch (cell.getDoorDirection()) {
	            case UP:
	                adj = row > 0 ? grid[row - 1][col] : null;
	                break;
	            case DOWN:
	                adj = row < numRows - 1 ? grid[row + 1][col] : null;
	                break;
	            case LEFT:
	                adj = col > 0 ? grid[row][col - 1] : null;
	                break;
	            case RIGHT:
	                adj = col < numColumns - 1 ? grid[row][col + 1] : null;
	                break;
	        }
	        if (adj != null && adj.getRoom() != null) {
	            BoardCell centerCell = adj.getRoom().getCenterCell();
	            if (centerCell != null) {
	                cell.addAdj(centerCell);
	            }
	        }
	    }
	}
	/*=============================================================================
	 * Used by SetAdjacencies, adds doorways and secret passages to
	 * the adj list for center room cells
	 ==========================================================================*/
	private void addRoomCenterAdjacencies(BoardCell cell) {
	    //Room room = cell.getRoom();
	    for (int r = 0; r < numRows; r++) {
	        for (int c = 0; c < numColumns; c++) {
	            BoardCell potentialDoor = grid[r][c];
	            if (potentialDoor.isDoorway()) {
	                BoardCell adj = null;
	                switch (potentialDoor.getDoorDirection()) {
	                    case UP:
	                        adj = r > 0 ? grid[r - 1][c] : null;
	                        break;
	                    case DOWN:
	                        adj = r < numRows - 1 ? grid[r + 1][c] : null;
	                        break;
	                    case LEFT:
	                        adj = c > 0 ? grid[r][c - 1] : null;
	                        break;
	                    case RIGHT:
	                        adj = c < numColumns - 1 ? grid[r][c + 1] : null;
	                        break;
	                }
	                if (adj != null && adj.getRoom().getCenterCell().equals(cell)) {
	                    cell.addAdj(potentialDoor);
	                }
	            }
	            if (potentialDoor.getSecretPassage() != '\0' && potentialDoor.getRoom().getName().equals(cell.getRoom().getName())) {
	                cell.addAdj(potentialDoor.getRoom().getCenterCell());
	            }
	        }
	    }
	}
	
	/*=============================================================================
	 * Used by SetAdjacencies, adds center cell
	 * the adj list for secret passages
	 ==========================================================================*/
	private void addSecretPassageAdjacency(BoardCell cell) {
	    Room secretRoom = getRoom(cell.getSecretPassage());
	    if (secretRoom != null) {
	        BoardCell centerCell = secretRoom.getCenterCell();
	        if (centerCell != null) {
	            cell.addAdj(centerCell);
	        }
	    }
	}
	
	
    /*===============================================================
     * Begins a BFD search, calculating targets for 
     * where player can move using adjacency list
     * 
     * Calls findAllTargets
     ================================================================*/
    public void calcTargets(BoardCell boardCell, int pathLength) {
        targets = new HashSet<>();
        Set<BoardCell> visited = new HashSet<>();
        visited.add(boardCell);
        findAllTargets(boardCell, pathLength, visited);
    }
    
    /*============================================================
     * Meat of the BFD search for finding movement targets
     * 
     * Calls itself recursively on each item in adjList
     =============================================================*/
    private void findAllTargets(BoardCell cell, int steps, Set<BoardCell> visited) {
    	
//    	for all adjacent spots to each cell, starting at desired first cell
        for (BoardCell currCell : cell.getAdjList()) {
//        	if cell is occupied and isnt a room center, or has already been visited, skip this iteration
            if (visited.contains(currCell) || (!currCell.isRoomCenter() && currCell.getOccupied())) continue;
//            add current cell to list of visited cells
            visited.add(currCell);
//            if at last step, add to targets list
            if (steps == 1 || currCell.isRoomCenter()) {
                targets.add(currCell);
            //continue until at step number passed into method
            } else {
                findAllTargets(currCell, steps - 1, visited);
            }
            visited.remove(currCell);
        }
    }   
    
}

