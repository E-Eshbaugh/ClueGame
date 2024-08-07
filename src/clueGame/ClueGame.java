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

public class ClueGame extends JPanel{
	

	public static void main(String[] args) {
		
		Board.createInstance();
        Board board = Board.getInstance();
        board.initialize();

        
        
        
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Medieval Clue");
		frame.setSize(900, 1000);
		frame.setLayout(new BorderLayout());
		
		
		
		CardsGUIPanel cardsPanel = new CardsGUIPanel();
		cardsPanel.setPreferredSize(new Dimension(200, 0));
		frame.add(cardsPanel, BorderLayout.EAST);
		
		GameControlPanel gameControl = new GameControlPanel();
		gameControl.setPreferredSize(new Dimension(0,200));
		frame.add(gameControl, BorderLayout.SOUTH);
		
		frame.setVisible(true);
		
		// Get the human player details the show splash screen with players character
		Player humanPlayer = Board.getHuman(); 
		showSplashScreen(humanPlayer);
	}
	
	private static void showSplashScreen(Player humanPlayer) {
        String message = "You are: The " + humanPlayer.getName() + "\nCan you find the solution\nbefore the Computer players?";
        JOptionPane.showMessageDialog(null, message, "Welcome To Clue", JOptionPane.INFORMATION_MESSAGE);
    }
}