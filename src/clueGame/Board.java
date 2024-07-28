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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
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
	


    // Private constructor for singleton pattern
    private Board() {
    	roomMap = new HashMap<>();
    	roomCenterMap = new HashMap<>();
    	configFileCSV =  "ClueLayout306.csv";
    	configFileTXT = "ClueSetup306.txt";
    	csvFilePath = Paths.get("ClueInitFiles", "data", configFileCSV);
    	txtFilePath = Paths.get("ClueInitFiles", "data", configFileTXT);
    }

	// Static method to get the single instance of the Board
	public static Board getInstance() {
		return theInstance;
	}
	
	//Updates row and col nums based on the file
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
	}

	
    public void initialize() {
	    try {
	    	loadSetupConfig();
			loadLayoutConfig();
			setAdjacencies();
	    } catch (Exception e) {
	    	//write to error log
	    	try (FileWriter errorLogWrite = new FileWriter("errorlog.txt", true)) {
				errorLogWrite.write("BadConfigFormatException thrown for " + configFileTXT + " ... Bad value in file");
				errorLogWrite.write("\n");
			} catch (Exception e2) {
				System.out.println("ERROR WRITING TO errorlog.txt");
				e.printStackTrace();
			}
	    }
    }
   
    
	//read and interpret the key for the rooms (txt file)
	public void loadSetupConfig(){
		try (Scanner scanner = new Scanner(Files.newInputStream(txtFilePath))) {
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
							errorLogWrite.write("BadConfigFormatException thrown for " + configFileTXT + " ... Bad value in file");
							errorLogWrite.write("\n");
						} catch (Exception e) {
							System.out.println("ERROR WRITING TO errorlog.txt");
							e.printStackTrace();
						}
						throw new BadConfigFormatException("Invalid type in setup configuration file: " + type);
					}
					//bad file format overall
				} else {
					//append to errorlog before throwing exception
					try (FileWriter errorLogWrite = new FileWriter("logs/errorlog.txt", true)) {
						errorLogWrite.write("BadConfigFormatException thrown for " + configFileTXT + " ... Bad file format");
						errorLogWrite.write("\n");
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

		//printRoomMap();
	}

	//load the layoutconfig file (csv file)
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
				if (grid == null) {
					//numRows = 25; // You can dynamically adjust this if needed
					grid = new BoardCell[numRows][numColumns];
				}
				for (int col = 0; col < values.length; col++) {
					String cellValue = values[col].trim();
//					System.out.println( cellValue);
					char initial = cellValue.charAt(0);
					BoardCell cell = new BoardCell(row, col, cellValue);


					Room originalRoom = roomMap.get(initial);
					if (originalRoom != null) {
						Room roomCopy = new Room(originalRoom.getName());
						//roomCopy.setCenterCell(cell);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		//print out the map in character format
		try {
			printGrid(); 
		}catch(Exception e) {
			throw new BadConfigFormatException();
		}
		setRoomCenters();
		setRoomsCenterSpot();
//		printCenterRoomMap();
//		printRoomMap();
//		System.out.println(grid[3][20] + " " + grid[3][20].getRoom().getName() + "is" +grid[3][20].getRoom().isRoomCenter());
//		System.out.println(grid[14][2] + "is" +grid[3][20].getRoom().isRoomCenter());
//		System.out.println(grid[3][0].getRoom().getName() + " center cell is" +grid[3][0].getRoom().getCenterCell());
//		System.out.println(grid[2][2].getRoom().getName() + " center cell is" +grid[2][2].getRoom().getCenterCell());
		
	}
	
	// Fill the roomCenterMap
	private void setRoomCenters() {
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

	//Add comments here
	private void setRoomsCenterSpot() {
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
	

	//Add comments here
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
	
	
	public void printRoomMap() {
		for (Map.Entry<Character, Room> entry : roomMap.entrySet()) {
			System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName());
		}
	}
	public void printCenterRoomMap() {
		for (Map.Entry<Character, Room> entry : roomCenterMap.entrySet()) {
			System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName()+  entry.getValue().getCenterCell());
		}
	}
	//initializes cell adjacency lists for board, only runs once for whole game
	private void setAdjacencies() {
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            BoardCell cell = grid[row][col];

	            // Only set adjacencies if the cell is a walkway or a doorway
	            if (cell.getInitial().equals("W") || cell.isDoorway()) {
	                // Check adjacent cells
	                // Up
	                if (row > 0) {
	                    BoardCell adj = grid[row - 1][col];
	                    if (adj.getInitial().equals("W") || adj.isDoorway()) {
	                        cell.addAdj(adj);
	                    }
	                }
	                // Down
	                if (row < numRows - 1) {
	                    BoardCell adj = grid[row + 1][col];
	                    if (adj.getInitial().equals("W") || adj.isDoorway()) {
	                        cell.addAdj(adj);
	                    }
	                }
	                // Left
	                if (col > 0) {
	                    BoardCell adj = grid[row][col - 1];
	                    if (adj.getInitial().equals("W") || adj.isDoorway()) {
	                        cell.addAdj(adj);
	                    }
	                }
	                // Right
	                if (col < numColumns - 1) {
	                    BoardCell adj = grid[row][col + 1];
	                    if (adj.getInitial().equals("W") || adj.isDoorway()) {
	                        cell.addAdj(adj);
	                    }
	                }

	                // If the cell is a doorway, add the center cell of the room it leads to
	                if (cell.isDoorway()) {
	                    BoardCell adj = null;
	                    switch (cell.getDoorDirection()) {
	                        case UP:
	                            if (row > 0) {
	                                adj = grid[row - 1][col];
	                            }
	                            break;
	                        case DOWN:
	                            if (row < numRows - 1) {
	                                adj = grid[row + 1][col];
	                            }
	                            break;
	                        case LEFT:
	                            if (col > 0) {
	                                adj = grid[row][col - 1];
	                            }
	                            break;
	                        case RIGHT:
	                            if (col < numColumns - 1) {
	                                adj = grid[row][col + 1];
	                            }
	                            break;
	                        default:
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
	            //setCenterCell
	            
	            // Set the center cell of a room to have its adjacencies as all doors leading to the room
	            if (cell.isRoomCenter()) {
	                Room room = cell.getRoom();
	                for (int r = 0; r < numRows; r++) {
	                    for (int c = 0; c < numColumns; c++) {
	                        BoardCell potentialDoor = grid[r][c];
	                     // 
	                        if (potentialDoor.isDoorway()) {
	                            BoardCell adj = null;
	                            switch (potentialDoor.getDoorDirection()) {
	                                case UP:
	                                    if (r > 0) {
	                                        adj = grid[r - 1][c];
	                                    }
	                                    break;
	                                case DOWN:
	                                    if (r < numRows - 1) {
	                                        adj = grid[r + 1][c];
	                                    }
	                                    break;
	                                case LEFT:
	                                    if (c > 0) {
	                                        adj = grid[r][c - 1];
	                                    }
	                                    break;
	                                case RIGHT:
	                                    if (c < numColumns - 1) {
	                                        adj = grid[r][c + 1];
	                                    }
	                                    break;
	                                default:
	                                    break;
	                            }
	                            if (adj != null && adj.getRoom().getCenterCell().equals(cell) ) {
	                                cell.addAdj(potentialDoor);
	                            }
	                        }
	                     // Secret passage check even though var name is potentialDoor, if the statement is true then potentialDoor is a secret passage
	                        if (potentialDoor.getSecretPassage() != '\0') {
	                        	BoardCell adj = null;
	                        	if(potentialDoor.getRoom().getName().equals(cell.getRoom().getName()) ) {
	                        		adj = potentialDoor.getRoom().getCenterCell();
	                        		cell.addAdj(adj); 
	                        	}
	                        	
	    	                	   	               
	                        }
	                    }
	                }
	            }

	            // Set the center cell that the secret passage leads to
	            if (cell.getSecretPassage() != '\0') {
	                Room secretRoom = getRoom(cell.getSecretPassage());
	                if (secretRoom != null) {
	                    BoardCell centerCell = secretRoom.getCenterCell();
	                    if (centerCell != null) {
	                        cell.addAdj(centerCell);
	                    }
	                }
	            }
	        }
	    }
	}
    
    //BFD search, calculating targets for where player can move
    public void calcTargets(BoardCell boardCell, int pathLength) {
        targets = new HashSet<>();
        Set<BoardCell> visited = new HashSet<>();
        visited.add(boardCell);
        findAllTargets(boardCell, pathLength, visited);
    }
    
    //part of BFD search for finding movement targets
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
    
	//set config files and the paths to them
    public void setConfigFiles(String layoutConfigFile, String setupConfigFile) {
        this.configFileCSV = layoutConfigFile;
        this.configFileTXT = setupConfigFile;
        this.csvFilePath = Paths.get("ClueInitFiles", "data", this.configFileCSV);
    	this.txtFilePath = Paths.get("ClueInitFiles", "data", this.configFileTXT);
    }

	public Set<BoardCell> getAdjList(int i, int j) {
		return getCell(i,j).getAdjList();
	}

	public Set<BoardCell> getTargets() { 
		return targets;
	}
    
}