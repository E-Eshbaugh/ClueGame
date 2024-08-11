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
	 * Sets up the game frame
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
	 =====================================*/
	private static void setupGamePanel() {
	    JPanel boardPanel = board.drawBoard();
	    gamePanel = new JPanel(new BorderLayout()); // Use BorderLayout to allow resizing
	    gamePanel.setPreferredSize(new Dimension(650, 700));
	    gamePanel.add(boardPanel, BorderLayout.CENTER);
	    frame.add(gamePanel, BorderLayout.CENTER);
	}
	
	
	
	/*================================
	 * sets up and creates the game 
	 * control panel at the bottom of
	 * the game frame
	 * 
	 * calls GameControlPanel()
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
	 * Displays splash frame
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

	    // Highlight valid targets and add mouse listeners 
	    for (BoardCell target : board.getTargets()) {
	        target.setHighlighted(true); // Highlight the cell

	        // Create and store the listener
	        MouseListener listener = new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent evt) {
	                handlePlayerMove(target);
	            }
	        };
	        target.addMouseListener(listener);
	        addedListeners.add(listener); // Store the listener for later removal
	        
	        
	        target.repaint(); // Repaint the cell to show the highlight
	    }

	    isHumanTurn = true;	
	}
	
	private static void handlePlayerMove(BoardCell targetCell) {
	    // Move the player to the clicked cell
		BoardCell currentCell = board.getCell(humanPlayer.getRow(), humanPlayer.getCol());
	    humanPlayer.setRow(targetCell.getRow());
	    humanPlayer.setCol(targetCell.getCol());

	    currentCell.setOccupied(false); // Mark the old cell as not occupied
	    targetCell.setOccupied(true); // Mark the new cell as occupied
	    
	    // Unhighlight all cells and remove listeners
	    for (BoardCell cell : board.getTargets()) {
	        cell.setHighlighted(false);

	        // Remove all added listeners
	        for (MouseListener listener : cell.getMouseListeners()) {
	            cell.removeMouseListener(listener);
	        }

	        cell.repaint(); // Repaint to remove the highlight
	    }

	    // Update the board state and repaint
	    isHumanTurn = false;
	    turnOver = true;
	    board.repaint(); // Repaint the board to reflect the new player position
	}
	
	
	/*==================================
	 * Accusation clicked - called by
	 * accusation button in 
	 * GameControlPanel when accusation
	 * button is clicked
	 ================================*/
	public static void accusationClick() {
		System.out.println("Accusation");
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