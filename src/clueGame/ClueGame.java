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

//auto generated suppress to hide warning about no UID long return (?) 
@SuppressWarnings("serial")
public class ClueGame extends JPanel{
	
	Random rand = new Random();
	private JFrame frame = new JFrame();
	private CardsGUIPanel cardsPanel;
	private GameControlPanel gameControlPanel;
	private JPanel gamePanel;
	private Board board;
	protected static HumanPlayer humanPlayer;
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
	}
	
	
	
	/*================================
	 * Sets up the game frame
	 ==============================*/
	private void setupFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Medieval Clue");
		frame.setSize(900, 1000);
		frame.setLayout(new BorderLayout());
	}
	
	
	
	/*=========================
	 * Creates the CardsGUIPanel
	 * on right side of game frame
	 * 
	 * calls CardsGUIPanel()
	 =============================*/
	private void setupCardsPanel() {
		cardsPanel = new CardsGUIPanel();
		cardsPanel.setPreferredSize(new Dimension(200, 0));
		frame.add(cardsPanel, BorderLayout.EAST);
	}
	
	
	
	/*===================================
	 * Sets up the panel that holds the 
	 * game board
	 * 
	 * calls board.drawBoard()
	 =====================================*/
	private void setupGamePanel() {
		JPanel boardPanel = board.drawBoard();
		gamePanel = new JPanel();
		gamePanel.setPreferredSize(new Dimension(700,800));
		gamePanel.setBackground(Color.black);
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
		gameControlPanel.setPreferredSize(new Dimension(0,175));
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
		
	
	//=============================================================================================\\
	//==============================------------- MAIN --------------==============================\\
	//=============================================================================================\\
	
	public static void main(String[] args) {
		
		ClueGame clueGame = new ClueGame();
		clueGame.displayGame();
		showSplashFrame();
		
		for (Player player : playerMoveOrder) {
			player.makeMove();
		}
	}
}