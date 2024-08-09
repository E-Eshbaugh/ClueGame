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
	
	private ClueGame() {
		initializeBoard();
		setupFrame();
		setupGamePanel();
		setupCardsPanel();
		setupControlPanel();
	}
	
	private void displayGame() {
		frame.setVisible(true);
	}
	
	private void setupFrame() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Medieval Clue");
		frame.setSize(900, 1000);
		frame.setLayout(new BorderLayout());
	}
	
	private void setupCardsPanel() {
		CardsGUIPanel cardsPanel = new CardsGUIPanel();
		cardsPanel.setPreferredSize(new Dimension(200, 0));
		frame.add(cardsPanel, BorderLayout.EAST);
	}
	
	private void setupGamePanel() {
		JPanel gamePanel = board.drawBoard();
		gamePanel.setPreferredSize(new Dimension(700,800));
		frame.add(gamePanel, BorderLayout.CENTER);
	}
	
	private void setupControlPanel() {
		GameControlPanel gameControl = new GameControlPanel();
		gameControl.setPreferredSize(new Dimension(0,200));
		frame.add(gameControl, BorderLayout.SOUTH);
	}
	
	private static void showSplashFrame() { 
		String message = "You are: The " + humanPlayer.getName() + "\nCan you find the solution\nbefore the Computer players?";
        JOptionPane.showMessageDialog(null, message, "Welcome To Clue", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void initializeBoard() {
		Board.createInstance();
        board = Board.getInstance();
        board.initialize();
        humanPlayer = Board.getHuman();
	}
	

	public static void main(String[] args) {
		
		ClueGame clueGame = new ClueGame();
		clueGame.displayGame();
		showSplashFrame();
	}
}