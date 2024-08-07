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
	}
}