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
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.util.Scanner;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

//auto generated suppress warning
@SuppressWarnings("serial")
public class Board extends JPanel{
	private BoardCell[][] grid;
	public int numRows;
	public int numColumns;
	private int numPlayers;
	private int numCards;
	private String configFileCSV;
	private String configFileTXT;
	private Path csvFilePath;
	private Path txtFilePath;
	public Map<Character, Room> mapsIntialToRoom;
	public Map<Character, Room> mapsRoomToCenters;
	private static Board theInstance;
	private Set<BoardCell> targets;
	private static ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Card> deck = new ArrayList<Card>();
	private Solution theAnswer;
	private ArrayList<Character> legend = new ArrayList<Character>();

    
    /*==================================
     * Getters & Setters
     ===================================*/
    public void setNumPlayers(int num) {
    	numPlayers = num;
    }
    
    public int getNumPlayers() {
    	return numPlayers;
    }
    
    public void setNumCards(int num) {
    	numCards = num;
    }
    
    public int getNumCards() {
    	return numCards;
    }
    
    public Solution revealAnswer() {
    	return theAnswer; 	
    }
    
	public Room getRoom(char initial) {
		return mapsIntialToRoom.get(initial);
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
	
	public BoardCell[][] getGrid() {
		return grid;
	}
	
	public Set<BoardCell> getAdjList(int i, int j) {
		return getCell(i,j).getAdjList();
	}

	public Set<BoardCell> getTargets() { 
		return targets;
	}
	public void addTarget(BoardCell Center) { 
		targets.add(Center);
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addCard(Card card) {
    	deck.add(card);
    }
    public ArrayList<Card> getCards() {
        return deck;
    }
    
    public void setPlayers(ArrayList<Player> playerList) {
    	Board.players = new ArrayList<Player>(playerList);
    }
    
    public static HumanPlayer getHuman() {
    	for (Player player : players) if (player.isHuman()) return (HumanPlayer) player;
    	return null;
    }
	
    //=============================================================================================================\\
    //=========================---------------------- BOARD CONSTRUCTION METHODS ---------------====================\\
    //==============================================================================================================\\
    
    /*=============================================
     *  Private constructor for singleton pattern
     =============================================*/
    private Board() {
    	mapsIntialToRoom = new HashMap<>();
    	mapsRoomToCenters = new HashMap<>();
    	configFileCSV =  "ClueLayout.csv";
    	configFileTXT = "ClueSetup.txt";
    	csvFilePath = Paths.get("ClueInitFiles", "data", configFileCSV);
    	txtFilePath = Paths.get("ClueInitFiles", "data", configFileTXT);
    }
    
    
    /*=============================================
     * Draw Board
     =============================================*/

    public JPanel drawBoard(int width, int height) {
        JPanel basePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                this.setSize(width, height);
                paintBoard(g, this);
            }
        };
        
        return basePanel;
    }
    private void paintBoard(Graphics g, JPanel basePanel) {
        int cellWidth = basePanel.getWidth() / numColumns;
        int cellHeight = basePanel.getHeight() / numRows;

        basePanel.removeAll();
        basePanel.setLayout(null);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                BoardCell cell = grid[row][col];
                JPanel cellPanel = cell.draw(players);

                // Set the bounds for the cellPanel and add it to the basePanel
                cellPanel.setBounds(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                basePanel.add(cellPanel);

                // If the cell has a room label (using # to identify)
                if (cell.getInitial().length() > 1 && cell.getInitial().charAt(1) == '#') {
                    JLabel label = new JLabel(cell.getRoom().getName(), SwingConstants.CENTER);
                    label.setForeground(Color.WHITE);
                    
                    // Calculate the position and size for the label
                    int xPosition = col * cellWidth - cellWidth*2; // Start before the cellPanel
                    int yPosition = row * cellHeight + cellHeight; // Align vertically with the cellPanel
                    int labelWidth = cellWidth * 5; // Span across multiple cells
                    int labelHeight = cellHeight; // Same height as the cell

                    // Set the bounds of the label
                    label.setBounds(xPosition, yPosition, labelWidth, labelHeight);

                    // Add the label to the basePanel
                    basePanel.add(label, JLayeredPane.PALETTE_LAYER);
                }
            }
        }

        basePanel.revalidate();
        //basePanel.repaint();
    }
	
    
    //=====================================================================================================\\
    //=====================-------------------- GAME SETUP METHODS ----------------=========================\\
    //======================================================================================================\\
    
    
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
			setupCardsAndPlayers();
			updatePlayerMoves();
			deal();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    //====comment out to stop map from printing in console====
	    //visualizeMap();
    }
    
    
    /*===============================================
	 * Random selects a player to make the human player
	 ==============================================*/
	public void turnToHuman() {
		Random rand = new Random();
		
		int tobeHuman = rand.nextInt(players.size());
		//get the attributes to clone computerPlayer into HumanPlayer
		String name = players.get(tobeHuman).getName();
		Color color = players.get(tobeHuman).getColor();
		int row = players.get(tobeHuman).getRow();
		int col = players.get(tobeHuman).getCol();
		
		//replace bot at location with humanplayer
		players.set(tobeHuman, new HumanPlayer(name, color, row, col));
		players.get(tobeHuman).makeHuman(true);
	}
	
	
	
	/*================================================
	 * Set the answer to the game
	 * for testing purposes
	 =================================================*/
	public void setAnswer(Solution answer) {
		theAnswer = answer;
	}
    
	
	
	/*=======================================================================
	 * Creates and shuffles cards into a random order in cards ArrayList
	 * Used by deal()
	 ========================================================================*/
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	
	
	/*========================================================
	 * Static method to get the single instance of the Board
	 ========================================================*/
	public static Board getInstance() {
		return theInstance;
	}

    
    
    /*===============================================================
     * Sets up players and deck as well as numPlayers and numCards
     * used by initialize
     ===============================================================*/
    public void setupCardsAndPlayers() {
    	//make one player human player
    	turnToHuman();
    	
    	//add player cards to deck 
    	for (Player playerCard : players) {
    		String pCardName = playerCard.getName();
    		deck.add(new Card(pCardName, Card.CardType.PERSON));
    	}
    	
    	//add room cards to deck
    	for (Entry<Character, Room> roomCard : mapsIntialToRoom.entrySet()) {
    		String rCardName = roomCard.getValue().getName();
    		if (rCardName.equals("Unused") || rCardName.equals("Walkway")) continue;
    		else deck.add(new Card(rCardName, Card.CardType.ROOM));
    	}
    	
    	//get num cards and num players
    	numCards = deck.size();
    	numPlayers = players.size();   	
    	
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
						mapsRoomToCenters.put(Character.valueOf(cell.getInitial().charAt(0)), cell.getRoom()); 
						
                    }
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
	                    case NONE:
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
	            if (mapsRoomToCenters.containsKey(initial)) {
	                Room room = mapsIntialToRoom.get(initial);
	                if (room != null) {
	                    BoardCell centerCell = mapsRoomToCenters.get(initial).getCenterCell();
	                    cell.getRoom().setCenterCell(centerCell);
	                }
	            }
	        }
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
		addAdjacency(cell, row , col);
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
	            case NONE:
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
	
	
    //====================================================================================================\\
    //======================-------------------- GAME FLOW MANAGEMENT METHODS -------------===============\\
    //====================================================================================================\\
    
	
	/*===============================================
	 * Handle a suggestion made Process all the players in turn, 
	 * each to see if they can dispute the suggestion. If return null
	 * no player can dispute the suggestion. Otherwise return the 
	 * first card that disputed the suggestion.
	 ==============================================*/
	public Card handleSuggestion(Solution suggestion, Player accuser) {
	    Card disprovingCard = null;
	    Player disprovingPlayer = null;
	    
	    // Get the room name from the suggestion
	    String roomName = suggestion.getRoom().getName();
	    BoardCell roomCenter = null;

	    // Find the center cell of the room based on the room's initial
	    for (int row = 0; row < numRows; row++) {
	        for (int col = 0; col < numColumns; col++) {
	            BoardCell cell = grid[row][col];
	            if (cell.isRoomCenter() && cell.getRoom().getName().equals(roomName)) {
	                roomCenter = cell;
	                break;  // Exit the loop once we find the center cell
	            }
	        }
	        if (roomCenter != null) break;  // Exit outer loop if center cell found
	    }

	    if (roomCenter != null) {
	        // Move the suggested person to the center of the room
	        for (Player player : players) {
	            if (player.getName().equals(suggestion.getPerson().getName())) {	
	            	player.setTempRoomForAdjacent(roomCenter);
	            	//addTarget(roomCenter);
//	                player.setRow(roomCenter.getRow());
//	                player.setCol(roomCenter.getCol());
//	                roomCenter.setOccupied(true);
	                repaint();
	                break;
	            }
	        }
	    } else {
	        System.out.println("Error: Room center could not be found for " + roomName);
	    }

	    // Check if any player can disprove the suggestion
	    for (Player player : players) {
	        if (!player.equals(accuser)) {
	            disprovingCard = player.disproveSuggestion(suggestion);
	            if (disprovingCard != null) {
	                disprovingPlayer = player;
	                break;
	            }
	        }
	    }

	    // Update the GameControlPanel with the result
	    String suggestionText = suggestion.getPerson().getName() + " with the " + suggestion.getWeapon().getName() + " in the " + suggestion.getRoom().getName();
	    ClueGame.getGameControlPanel().setGuess(suggestionText);
	    
	    if (disprovingCard != null) {
	        String resultText;
	        if (accuser.isHuman()) {
	            resultText = "Disproved by " + disprovingPlayer.getName() + ": " + disprovingCard.getName();
	            ClueGame.humanPlayer.updateSeen(disprovingCard);
	            accuser.updateSeen(disprovingCard);
	            ClueGame.getCardsPanel().refresh();
	        } else {
	            resultText = "Disproved by " + disprovingPlayer.getName() + ".";
	            accuser.updateSeen(disprovingCard);
	        }
	        ClueGame.getGameControlPanel().setGuessResult(resultText);
	    } else {
	        ClueGame.getGameControlPanel().setGuessResult("No one could disprove the suggestion.");
	    }

	    return disprovingCard;
	}
	
	
	
	/*================================================
	 * Accusation checking, returns true if accusation
	 * matches the Answer
	 * Uses the equals overload in Solution class
	 ================================================*/
	public boolean accusationCheck(Solution accusation) {
		if (accusation.equals(theAnswer)) return true;
		else return false;
	}
	
	
	
	/*========================================================
	 * Deal the cards out to the players after random shuffle
	 * Calls shuffle
	 =======================================================*/
	public void deal() {
		
		// Initialize theAnswer with the first card of each type
        Card roomCard = null;
        Card personCard = null;
        Card weaponCard = null;

        ArrayList<Card> remainingCards = new ArrayList<>();
        
        Collections.shuffle(deck);

        for (Card card : deck) {
            if (card.getType() == Card.CardType.ROOM && roomCard == null) {
                roomCard = card;
            } else if (card.getType() == Card.CardType.PERSON && personCard == null) {
                personCard = card;
            } else if (card.getType() == Card.CardType.WEAPON && weaponCard == null) {
                weaponCard = card;
            } else {
                remainingCards.add(card);
            }
        }

        theAnswer = new Solution(roomCard, personCard, weaponCard);
        Collections.shuffle(deck);
        
        // Shuffle the remaining cards
        Collections.shuffle(remainingCards);

        // Deal the remaining cards to the players
        for (Player player : players) {
            for (int i = 0; i < 3; i++) {
                if (!remainingCards.isEmpty()) {
                	remainingCards.get(0).setColor(player.getColor());
                    player.updateHand(remainingCards.remove(0));
                }
            }
        }
        
        //set dealt values to true now that all cards have been dealt
        for (Card card : deck) card.setHasBeenDealt(true);
	}
	
	
	
	/* ============================================================
	 * Updates row and col nums based on the file and initializes grid
	 * Uses File Scanner -> exceptions thrown handled in method
	 ==============================================================*/
	public void updateDimensions() {
		try (Scanner scanner = new Scanner(Files.newInputStream(csvFilePath))){
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				numRows++;
				numColumns = line.length();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		//get correct length (minus the ,'s)
		this.numColumns = (this.numColumns+1)/2;
		
		//initialize grid
		grid = new BoardCell[numRows][numColumns];
	}
	
	
	/*====================================================
     * Updates occupied spaces based on where players are
     ======================================================*/
    public void updatePlayerMoves() {
    	for (Player player : players) {
    		grid[player.getRow()][player.getCol()].isOccupied = true;
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
        targets.add(boardCell);
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
	
	
	//======================================================================================================\\
	//=======================------------------- METHODS USED FOR TESTING SETUP ----------------============\\
	//======================================================================================================\\
	
	
	/*=============================
	 * board refresher for testing
	 ===============================*/
	public static void createInstance() {
		theInstance = new Board();
	}
	
	
	
	/*==========================================
	 * Visualizes map to console
	 * Calls printCenterRoomMap & printRoomMap
	 ============================================*/
	public void visualizeMap() {
		printGrid();
		printCenterRoomMap();
		//printRoomMap();
		printDeck();
	}
	
	
	/*=================================
	 * Prints out the Room map with the 
	 * room initial and name
	 ====================================*/
	public void printRoomMap() {
		for (Map.Entry<Character, Room> entry : mapsIntialToRoom.entrySet()) {
			System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName());
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

	
	
	
	/*====================================
	 * Prints out the CenterRoomMap with the 
	 * room initial and room center cell
	 ======================================*/
	public void printCenterRoomMap() {
		for (Map.Entry<Character, Room> entry : mapsRoomToCenters.entrySet()) {
			System.out.println("Initial: " + entry.getKey() + ", Room: " + entry.getValue().getName() +  entry.getValue().getCenterCell());
		}
	}
	
	
	/*=================================
	 * Prints out the deck of cards
	 =================================*/
	public void printDeck() {
		for (Card card : deck) {
	        System.out.println(card);
	    }
	}

	
	//===============================================================================================================\\
    //=============================---------------------- FILE MANAGEMENT METHODS ---------------====================\\
	//===============================================================================================================\\
	
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
    
	
    /*==============================================================================================================================
     * Uses a switch to each lines info -room/space, player, or weapon- and assigns the information to the appropriate place
     * used by loadSetupConfig
     * 
     * Throws bad format exception to loadSetupConfig
     =========================================================================================================================*/
    public void readData(String[] parts) throws BadConfigFormatException{
    	switch (parts.length) {
    	
	    	//Weapons - should have 2 info parts
			case 2 :
				String weaponName = parts[1];
				deck.add(new Card(weaponName, Card.CardType.WEAPON));
			break;
			
			//rooms and spaces - should have 3 info parts
			case 3 :
				String type = parts[0];
				String roomName = parts[1];
				char initial = parts[2].charAt(0);
				if (type.equals("Room") || type.equals("Space")) {
					Room room = new Room(roomName);
					mapsIntialToRoom.put(initial, room);
					legend.add(initial);
				}
				break;
				
			//Player pieces - should have 4 info parts
			case 4 :
				String playerName = parts[0];
				
				//turn string from a hex codes in file into java.awt.color
				Color color = Color.decode(parts[1]);
				
				int row = Integer.parseInt(parts[2]);
				int col = Integer.parseInt(parts[3]);
				
				//start by having all players as bots so that we can make a random player the human so each time you play you can be someone new			
				players.add(new ComputerPlayer(playerName, color, row, col));
				
				break;
				
			//no proper amount of info parts - BadConfigFormatException
			default :
				throw new BadConfigFormatException();
		}
    }
    
    
    
	/*============================================================================================================================================
	 * read and interpret the key for the rooms (TXT file)
	 * splits the file at commas and using the type specified, puts proper information into proper spots (players, deck, roomMap) [via readData]
	 * 
	 * Uses File Scanner, BadConfigFormatExceptions thrown up to initialize
	 ===============================================================================================================================================*/
	public void loadSetupConfig() throws BadConfigFormatException{
		try (Scanner scanner = new Scanner(Files.newInputStream(txtFilePath))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith("//") || line.isEmpty()) {
					continue; // Skip comments and empty lines
				}
				//split the legend at commas
				String[] parts = line.split(", ");
				
				readData(parts);
				
			}
		//readData failed -- bad format in file
		} catch (BadConfigFormatException e) {
			throw new BadConfigFormatException("ERROR IN TXT FILE");
		//file read failed -- look at scanner or file path
		} catch (IOException a) {
			throw new BadConfigFormatException("ERROR WITH OPENING TXT FILE");
		}
	}

	
	
	/*===================================================================
	 * load the layoutconfig file (CSV file)
	 * Calls updateDimensions, setRoomCenterInitials, and setRoomsCenterCells
	 * Uses File Scanner, exceptions handled in method
	 =====================================================================*/
	public void loadLayoutConfig() throws BadConfigFormatException{ 
		updateDimensions();
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

					Room originalRoom = mapsIntialToRoom.get(initial);
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
}
	

