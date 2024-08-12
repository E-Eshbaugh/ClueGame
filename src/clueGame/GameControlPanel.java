package clueGame;

/*GameControlPanel class
 * 
 * @author Ethan Eshbaugh
 * @author Colin Meyers
 * 
 * Creates GameControlPanel GUI
 * 
 * 8/4/2024
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

//auto generated suppress warning 
@SuppressWarnings("serial")
public class GameControlPanel extends JPanel {
    private JTextField currentTurnField;
    private JTextField rollField;
    private JTextField guessField;
    private JTextField guessResultField;
    private JButton accusationButton;
    private JButton nextButton;
    private int rollValue;

    public GameControlPanel() {
        setLayout(new GridLayout(2, 0));
        createTopPanel();
        createBottomPanel();
    }

    /*==================================
     * 
     *  Create and add the top panel
     * 
     =================================*/
    private void createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 4));

        topPanel.add(createTurnPanel());
        topPanel.add(createRollPanel());
        topPanel.add(createAccusationButton());
        topPanel.add(createNextButton());

        add(topPanel);
    }

    /*==================================
     * 
     *  Create and add the bottom panel
     * 
     =================================*/
    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(0, 2));

        bottomPanel.add(createGuessPanel());
        bottomPanel.add(createGuessResultPanel());

        add(bottomPanel);
    }

    /*==================================
     * 
     *  Create the turn panel
     * 
     =================================*/
    private JPanel createTurnPanel() {
        JPanel turnPanel = new JPanel();
        turnPanel.add(new JLabel("Whose turn?"));
        currentTurnField = new JTextField(10);
        currentTurnField.setEditable(false);
        currentTurnField.setSize(10, 10);
        turnPanel.add(currentTurnField);
        return turnPanel;
    }

    /*==================================
     * 
     *  Create the roll panel
     * 
     =================================*/
    private JPanel createRollPanel() {
        JPanel rollPanel = new JPanel();
        rollPanel.add(new JLabel("Roll:"));
        rollField = new JTextField(5);
        rollField.setEditable(false);
        rollPanel.add(rollField);
        return rollPanel;
    }

    /*==================================
     * 
     *  Create the accusation button
     * 
     =================================*/
    private JButton createAccusationButton() {
        accusationButton = new JButton("Make Accusation");
        accusationButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		ClueGame.accusationClick();
        	}
        });
        return accusationButton;
    }

    /*==================================
     * 
     *  Create the next button
     * 
     =================================*/
    private JButton createNextButton() {
        nextButton = new JButton("NEXT!");
        nextButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		if (ClueGame.turnOver) ClueGame.nextTurn();
        	}
        });
        return nextButton;
    }

    /*==================================
     * 
     *  Create the guess panel
     * 
     =================================*/
    private JPanel createGuessPanel() {
        JPanel guessPanel = new JPanel();
        guessPanel.setBorder(BorderFactory.createTitledBorder("Guess"));
        guessField = new JTextField(30);
        guessField.setEditable(false);
        guessPanel.add(guessField);
        return guessPanel;
    }

    /*==================================
     * 
     *  Create the guess result panel
     * 
     =================================*/
    private JPanel createGuessResultPanel() {
        JPanel guessResultPanel = new JPanel();
        guessResultPanel.setBorder(BorderFactory.createTitledBorder("Guess Result"));
        guessResultField = new JTextField(30);
        guessResultField.setEditable(false);
        guessResultPanel.add(guessResultField);
        return guessResultPanel;
    }

    /*==================================
     * 
     *  Set the current turn and roll
     * 
     =================================*/
    public void setTurn(Player player, int roll) {
        currentTurnField.setText(player.getName());
        rollField.setText(String.valueOf(roll));
        currentTurnField.setBackground(player.getColor());
    }

    /*==================================
     * 
     *  Set the guess text
     * 
     =================================*/
    public void setGuess(String guess) {
        guessField.setText(guess);
    }
    public int getRoll() {
        return rollValue;
    }

    public void setRoll(int rollValue) {
        this.rollValue = rollValue;
        rollField.setText(String.valueOf(rollValue)); 
    }

    /*==================================
     * 
     *  Set the guess result text
     * 
     =================================*/
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

        panel.setTurn(new ComputerPlayer("Col. Mustard", Color.ORANGE, 0, 0), 5);
        panel.setGuess("I have no guess!");
        panel.setGuessResult("So you have nothing?");
        
        
        System.out.println("Press enter to continue with test");
		Scanner scn = new Scanner(System.in);
		String toContinue = scn.nextLine();
		
		if (toContinue.equals("")) {
			System.out.println("New Changes: There should be a new card of each type in each seen catagory frame");
			
			panel.setTurn(new ComputerPlayer("Player 1", Color.GREEN, 0, 0), 3);
			
			panel.revalidate();
			frame.setVisible(true);
		}
		scn.close();
    }
}