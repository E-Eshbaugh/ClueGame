package clueGame;
/*ClueGame class - extends JPanel
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * handles the GUI aspects of the game and general game flow
 */

import java.awt.*;
import javax.swing.*;

//auto generated suppress to hide warning about no UID long return (?) 
@SuppressWarnings("serial")
public class ClueGame extends JPanel{
	
	private JFrame frame = new JFrame();
	private Board board;
	protected static HumanPlayer humanPlayer;
	
	
	//=============================================================================================\\
	//=======================------------------- SETUP METHODS -------------=======================\\
	//=============================================================================================\\
	
	/*======================
	 * Constructor
	 ========================*/
	private ClueGame() {
		initializeBoard();
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
		CardsGUIPanel cardsPanel = new CardsGUIPanel();
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
		JPanel gamePanel = new JPanel();
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
		GameControlPanel gameControl = new GameControlPanel();
		gameControl.setPreferredSize(new Dimension(0,200));
		frame.add(gameControl, BorderLayout.SOUTH);
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
	}
}