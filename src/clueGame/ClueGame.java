package clueGame;
/*ClueGame class - extends JPanel
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * handles the GUI aspects of the game and general game flow
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//auto generated suppress to hide warning about no UID long return (?) 
@SuppressWarnings("serial")
public class ClueGame extends JPanel{
	
	Random rand = new Random();
	private static JFrame frame = new JFrame();
	private static boolean isHumanTurn = true;
	public static boolean turnOver = true;
	private static CardsGUIPanel cardsPanel;
	private static GameControlPanel gameControlPanel;
	private static JPanel gamePanel;
	private static Board board;
	protected static HumanPlayer humanPlayer;
	private static Player currentPlayer;
	private static ArrayList<Player> playerMoveOrder = new ArrayList<Player>();
	
	
	//=============================================================================================\\
	//=======================------------------- SETUP METHODS -------------=======================\\
	//=============================================================================================\\
	
	/*======================
	 * Constructor
	 ========================*/
	private ClueGame() {
		initializeBoard();
		setupMoveOrder();
		setupFrame();
		setupGamePanel();
		setupCardsPanel();
		setupControlPanel();
		nextTurn();
	}
	
	
	/*================================
	 * Sets up the game JFrame
	 * named frame
	 * -static
	 ==============================*/
	private void setupFrame() {
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setTitle("Medieval Clue");
	    frame.setSize(800, 800);
	    frame.setLayout(new BorderLayout());
	    frame.setMinimumSize(new Dimension(400, 400)); 
	    frame.setResizable(true); 

	    frame.addComponentListener(new java.awt.event.ComponentAdapter() {
	        @Override
	        public void componentResized(java.awt.event.ComponentEvent evt) {
	            updateBoardPanel(); 
	        }
	    });
	}
	
	private void updateBoardPanel() {
	    gamePanel.revalidate();
	    gamePanel.repaint();
	}
	
	
	
	/*=========================
	 * Creates the CardsGUIPanel
	 * on right side of game frame
	 * 
	 * calls CardsGUIPanel()
	 * -static
	 =============================*/
	private void setupCardsPanel() {
		cardsPanel = new CardsGUIPanel();
		cardsPanel.setPreferredSize(new Dimension(150, 0));
		frame.add(cardsPanel, BorderLayout.EAST);
	}
	
	
	
	/*===================================
	 * Sets up the panel that holds the 
	 * game board
	 * 
	 * calls board.drawBoard()
	 * -static
	 =====================================*/
	private static void setupGamePanel() {
	    gamePanel = new JPanel(new GridBagLayout());
	    JPanel boardPanel = new JPanel(new GridBagLayout());
	    boardPanel.add(board.drawBoard(boardPanel.getWidth(), boardPanel.getHeight()));
	    gamePanel.add(boardPanel);
	    gamePanel.revalidate();
	    gamePanel.repaint();
	    
	    //listener for resizing
	    gamePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	//find the new size for board
                int gamePanelWidth = gamePanel.getWidth();
                int gamePanelHeight = gamePanel.getHeight();
                int newSize = Math.min(gamePanelWidth, gamePanelHeight);
                
                JPanel boardPanel = board.drawBoard(newSize, newSize);
                boardPanel.setPreferredSize(new Dimension(newSize, newSize));
                
                // Revalidate gamePanel to apply changes
                gamePanel.removeAll();
                gamePanel.add(boardPanel);
                gamePanel.revalidate();
                gamePanel.repaint();
            }
        });
	   
	    frame.add(gamePanel, BorderLayout.CENTER);
	}
	
	
	
	/*================================
	 * sets up and creates the game 
	 * control panel at the bottom of
	 * the game frame
	 * 
	 * calls GameControlPanel()
	 * -static
	 ====================================*/
	private void setupControlPanel() {
		gameControlPanel = new GameControlPanel();
		gameControlPanel.setPreferredSize(new Dimension(0,100));
		gameControlPanel.setTurn(playerMoveOrder.get(0), 0);
		frame.add(gameControlPanel, BorderLayout.SOUTH);
	}
	
	
	
	/*=======================
	 * initializes the board
	 * instance for the game
	 ========================*/
	private void initializeBoard() {
		Board.createInstance();
        board = Board.getInstance();
        board.initialize();
        humanPlayer = Board.getHuman();
	}
	
	
	
	/*==================================
	 * adds human player and all bot 
	 * players to the move list, then 
	 * shuffles into a random order 
	 * and pulls humanPlayer to the top
	 * to get the order players make moves
	 =====================================*/
	private void setupMoveOrder() {
		playerMoveOrder.add(humanPlayer);
	    for (Player player : board.getPlayers()) if (!player.isHuman()) playerMoveOrder.add(player);
	    Collections.shuffle(playerMoveOrder);
	    Collections.swap(playerMoveOrder, playerMoveOrder.indexOf(humanPlayer), 0);
	    currentPlayer = playerMoveOrder.get(0);
	}
	
	
	
	//=============================================================================================\\
	//=======================--------------- GAME CONTROL METHODS ---------========================\\
	//=============================================================================================\\
	
	/*=========================
	 * Display game frame
	 =========================*/
	private void displayGame() {
		frame.setVisible(true);
	}
	
	
	
	/*===============================
	 * Displays starting splash frame
	 ==============================*/
	private static void showSplashFrame() { 
		String message = "You are: The " + humanPlayer.getName() + "\nCan you find the solution\nbefore the Computer players?";
        JOptionPane.showMessageDialog(null, message, "Welcome To Clue", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	
	/*======================
	 * updates current player
	 * to next player in list
	 * and turn on isHumanTurn
	 * if next current player
	 * is human player
	 ========================*/
	private static void currentPlayerUpdate() {
		if (playerMoveOrder.indexOf(currentPlayer) == playerMoveOrder.size()-1) {
			currentPlayer = playerMoveOrder.get(0);
		}
		else currentPlayer = playerMoveOrder.get(playerMoveOrder.indexOf(currentPlayer) + 1);
		if (currentPlayer.isHuman()) isHumanTurn = true;
	}
	
	
	/*=============================
	 * Next turn, called by next
	 * button in GameControlPanel
	 * when next button is clicked
	 ===============================*/
	public static void nextTurn() {
			turnOver = false;
			gameControlPanel.setTurn(currentPlayer, 0);
			if (currentPlayer.isHuman() && isHumanTurn) humanPlayerTurn();
			else currentPlayer.makeMove();
			currentPlayerUpdate();
			gamePanel.repaint();
	}
	
	
	
	/*==================================================
	 * Called to handle more complex human player turn
	 * calls humanPlayer.makeMove()
	 ==================================================*/
	private static void humanPlayerTurn() {

	    Player player = humanPlayer;
	    BoardCell startCell = board.getCell(player.getRow(), player.getCol());
	    
		Random rand = new Random();
	    int diceRoll = rand.nextInt(6) + 1; // Generate a random number between 1 and 6
	    gameControlPanel.setRoll(diceRoll);
	    // Calculate possible moves (e.g., 3 steps)
	    board.calcTargets(startCell, diceRoll);

	    // List to keep track of added listeners
	    ArrayList<MouseListener> addedListeners = new ArrayList<>();

	    //handles mouse listeners for all spots
	    invalidTargetListeners(addedListeners);
	    validTargetListeners(addedListeners);

	    isHumanTurn = true;	
	    
	}
	
	
	/*=======================================================
	 * Mouse Listener Functionality for non valid targets
	 =======================================================*/
	private static void invalidTargetListeners(ArrayList<MouseListener> addedListeners) {
		//add mouseListeneres to non valid targets
	    for (BoardCell[] row : board.getGrid()) {
	    	for (BoardCell notTarget : row) {
	    		if (!board.getTargets().contains(notTarget)) {
	    			// Create and store the listener
	    	        MouseListener listener = new MouseAdapter() {
	    	            @Override
	    	            public void mouseClicked(MouseEvent evt) {
	    	                invalidMoveSelect();
	    	            }
	    	        };
	    	        notTarget.addMouseListener(listener);
	    	        addedListeners.add(listener); // Store the listener for later removal
	    		}
	    	}
	    }
	}
	
	/*===========================================
	 * Mouse listener functionality for valid targets
	 =================================================*/
	private static void validTargetListeners(ArrayList<MouseListener> addedListeners) {
		 // Highlight valid targets and add mouse listeners 
	    for (BoardCell target : board.getTargets()) {
	        target.setHighlighted(true); // Highlight the cell

	        // Create and store the listener
	        MouseListener listener = new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent evt) {
	            	humanPlayer.setTarget(target);
	               humanPlayer.makeMove();
	            // Update the board state and repaint
	       	    isHumanTurn = false;
	       	    turnOver = true;
	       	    //System.out.println(humanPlayer.getName()+ "(human player) roller a " + roll + " and moved to (" + humanPlayer.getRow() + "," + humanPlayer.getCol() + ")");
	       	    board.repaint(); // Repaint the board to reflect the new player position
	            }
	        };
	        target.addMouseListener(listener);
	        addedListeners.add(listener); // Store the listener for later removal
	        
	        
	        target.repaint(); // Repaint the cell to show the highlight
	    }
	}
	
	
	/*===============================================
	 * prints splash frame for invalid move select
	 ==============================================*/
	private static void invalidMoveSelect() { 
		//display splash frame
		String message = "That is not a valid move";
        JOptionPane.showMessageDialog(null, message, "Message", JOptionPane.INFORMATION_MESSAGE);
	}
	
	
	/*==================================
	 * Accusation clicked - called by
	 * accusation button in 
	 * GameControlPanel when accusation
	 * button is clicked
	 ================================*/
	public static void accusationClick() {
		//System.out.println(board.revealAnswer());
	    JFrame parentFrame = frame;  // Assuming 'frame' is the main game window

	    // Gather the possible selections for people, weapons, and rooms
	    ArrayList<String> people = new ArrayList<>();
	    ArrayList<String> weapons = new ArrayList<>();
	    ArrayList<String> rooms = new ArrayList<>();

	    for (Card card : board.getCards()) {
	        if (card.getType() == Card.CardType.PERSON) people.add(card.getName());
	        if (card.getType() == Card.CardType.WEAPON) weapons.add(card.getName());
	        if (card.getType() == Card.CardType.ROOM) rooms.add(card.getName());
	    }

	    // Create and show the accusation dialog
	    AccusationDialog accusationDialog = new AccusationDialog(parentFrame, people, weapons, rooms);
	    accusationDialog.setVisible(true);

	    // Get the accusation from the dialog
	    Solution accusation = accusationDialog.getAccusation();

	    if (accusation != null) {
	        // Check if the accusation is correct
	        boolean isCorrect = board.accusationCheck(accusation);

	        // Show the result to the player
	        if (isCorrect) {
	            JOptionPane.showMessageDialog(parentFrame, "Congratulations! You've won the game!", "Accusation Correct", JOptionPane.INFORMATION_MESSAGE);
	            // Handle the game win scenario (e.g., end the game)
	        } else {
	            JOptionPane.showMessageDialog(parentFrame, "Sorry, that's not correct. You lose! \n It was the "+ board.revealAnswer(), "Accusation Incorrect was" , JOptionPane.ERROR_MESSAGE);
	            // Handle the game loss scenario (e.g., end the game for this player)
	        }
	        
	        //end the game
	        System.exit(0);
	    } else {
	        // The player canceled the accusation
	        JOptionPane.showMessageDialog(parentFrame, "Accusation canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
		
	// Getter for gameControlPanel
    public static GameControlPanel getGameControlPanel() {
        return gameControlPanel;
    }

    // Setter for gameControlPanel
    public static void setGameControlPanel(GameControlPanel panel) {
        gameControlPanel = panel;
    }
	public static CardsGUIPanel getCardsPanel() {
		return cardsPanel;
	}
	public static void setCardsPanel(CardsGUIPanel cardsPanel) {
		ClueGame.cardsPanel = cardsPanel;
	}
	
	//=============================================================================================\\
	//==============================------------- MAIN --------------==============================\\
	//=============================================================================================\\
	
	public static void main(String[] args) {
		
		ClueGame clueGame = new ClueGame();
		clueGame.displayGame();
		showSplashFrame();
	}
	
}