/*Board class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Myers
 * 
 * outlines the behaviors of the Board for clue game
 * 
 */

package clueGame;

import java.util.HashMap;
import java.util.Map;
//import java.util.Set;
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
	
	//Updates row and col nums based on the file
	public void updateDimensions() {
		try (Scanner scanner = new Scanner(Files.newInputStream(layoutConfigPath))){
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
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    }
   
	//read and interpret the key for the rooms (txt file)
	public void loadSetupConfig(){
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
					try (FileWriter errorLogWrite = new FileWriter("errorlog.txt", true)) {
						errorLogWrite.write("BadConfigFormatException thrown for " + setupConfigFile + " ... Bad file format");
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
		printRoomMap();
	}

	//load the layoutconfig file (csv file)

	public void loadLayoutConfig() throws BadConfigFormatException{ 	
		this.updateDimensions();
		try (Scanner scanner = new Scanner(Files.newInputStream(layoutConfigPath))) {
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
					char initial = cellValue.charAt(0);
					BoardCell cell = new BoardCell(row, col, String.valueOf(initial));


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
		} catch (IOException e) {
			e.printStackTrace();
		}
		//print out the map in character format
		try {
			printGrid();
		}catch(Exception e) {
			throw new BadConfigFormatException();
		}
	}

	//DONT DELTE LATER WIHTOUT REWORKING THE TRY CATCH BLOCK ABOVE ^
	public void printGrid() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
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
        this.layoutConfigFile = layoutConfigFile;
        this.setupConfigFile = setupConfigFile;
        this.layoutConfigPath = Paths.get("ClueInitFiles", "data", this.layoutConfigFile);
    	this.setupConfigPath = Paths.get("ClueInitFiles", "data", this.setupConfigFile);
    }
    
    




}