package clueGame;

/*GameControlPanel class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * Creats GameControlPanel GUI
 * 
 * 8/4/2024
 */

import javax.swing.*;
import java.awt.*;

public class GameControlPanel extends JPanel {
	private JTextField currentTurnField;
	private JTextField rollField;
	private JTextField guessField;
	private JTextField guessResultField;
	private JButton accusationButton;
	private JButton nextButton;


	public GameControlPanel() {


		setLayout(new GridLayout(2, 0));

		// top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4));

		// Whose turn panel
		JPanel turnPanel = new JPanel();
		turnPanel.add(new JLabel("Whose turn?"));
		currentTurnField = new JTextField(10);
		currentTurnField.setEditable(false);
		currentTurnField.setSize(10, 10);
		turnPanel.add(currentTurnField);
		topPanel.add(turnPanel);

		// Roll panel
		JPanel rollPanel = new JPanel();

		
		rollPanel.add(new JLabel("Roll:"));
		rollField = new JTextField(5);
		rollField.setEditable(false);
		rollPanel.add(rollField);
		topPanel.add(rollPanel);

		// Accusation button
		accusationButton = new JButton("Make Accusation");
		topPanel.add(accusationButton);

		// Next button
		nextButton = new JButton("NEXT!");
		topPanel.add(nextButton);

		// Add the top panel to the main panel
		add(topPanel);

		// bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(0, 2));

		// Guess panel
		JPanel guessPanel = new JPanel();
		guessPanel.setBorder(BorderFactory.createTitledBorder("Guess"));
		guessField = new JTextField(30);
		guessField.setEditable(false);
		guessPanel.add(guessField);
		bottomPanel.add(guessPanel);

		// Guess result panel
		JPanel guessResultPanel = new JPanel();
		guessResultPanel.setBorder(BorderFactory.createTitledBorder("Guess Result"));
		guessResultField = new JTextField(30);
		guessResultField.setEditable(false);
		guessResultPanel.add(guessResultField);
		bottomPanel.add(guessResultPanel);

		add(bottomPanel);
	}

	public void setTurn(Player player, int roll) {
		currentTurnField.setText(player.getName());
		rollField.setText(String.valueOf(roll));
		currentTurnField.setBackground(player.getColor());
	}

	public void setGuess(String guess) {
		guessField.setText(guess);
	}

	public void setGuessResult(String guessResult) {
		guessResultField.setText(guessResult);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Clue Game Control Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 200);

		GameControlPanel panel = new GameControlPanel();
		frame.add(panel);
		frame.setVisible(true);

		// Sample usage of the setter methods
		panel.setTurn(new ComputerPlayer("Col. Mustard",Color.ORANGE, 0, 0), 5);
		panel.setGuess("I have no guess!");
		panel.setGuessResult("So you have nothing?");
	}
}

